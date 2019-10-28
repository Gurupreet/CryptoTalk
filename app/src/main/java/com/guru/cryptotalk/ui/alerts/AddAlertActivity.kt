package com.guru.cryptotalk.ui.alerts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.guru.cryptotalk.App
import com.guru.cryptotalk.Constants
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.firebase.FirebaseAuthManager
import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.CryptoListFirebaseSync
import com.guru.cryptotalk.data.api.model.Alert
import com.guru.cryptotalk.data.api.model.Crypto
import com.guru.cryptotalk.ui.adapters.TokenSearchAdapter
import kotlinx.android.synthetic.main.activity_add_alert.*

class AddAlertActivity : AppCompatActivity(), TokenSearchAdapter.OnTokenSelectedListerner {

    private var alert = Alert()
    private var isPositive = true
    var rotateDown: Animation? = null
    var rotateUp: Animation? = null
    var operation = Constants.OPERATION_SWAP
    var inFocusEditText = 0;
    var tokenJustSelected = false
    var cryptoSync: CryptoListFirebaseSync? = null
    var eth: Crypto ? = null
    lateinit var tokenSearchAdapter: TokenSearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alert)
        rotateDown = AnimationUtils.loadAnimation(this, R.anim.rotate_180)
        rotateUp = AnimationUtils.loadAnimation(this, R.anim.rotate_180_up)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initUI()
        setupTokenRecyclerview()

    }

    fun initUI() {
        done.setOnClickListener { addAlert() }
        percent_arrow.setOnClickListener { toggleArrows() }
        radio_group.setOnCheckedChangeListener { group, checkedId ->
        operation = if (checkedId == R.id.swap) {
                Constants.OPERATION_SWAP
            } else {
                Constants.OPERATION_LEND
            }
            changeVisibilitiesForLend()
        }
        setUpTimeUI()
    }

    fun setupTokenRecyclerview() {
        tokenSearchAdapter = TokenSearchAdapter(this, ArrayList())
        val layoutManager = LinearLayoutManager(this)
        token_recyclerview.layoutManager = layoutManager
        token_recyclerview.adapter = tokenSearchAdapter
        setupTokenTextWatchers()
    }

    fun setupTokenTextWatchers() {
        from_token.afterTextChanged {
            if (it.isNotEmpty() && !tokenJustSelected) {
                tokenSearchAdapter.setData(Constants.arrayOfTokens.filter { token ->
                    token.startsWith(
                        it
                    )
                })
                token_recyclerview.visibility = View.VISIBLE
            } else {
                token_recyclerview.visibility = View.GONE
            }
            inFocusEditText = 0
            tokenJustSelected = false
        }
        to_token.afterTextChanged {
            if (it.isNotEmpty() && !tokenJustSelected) {
                tokenSearchAdapter.setData(Constants.arrayOfTokens.filter { token ->
                    token.startsWith(
                        it
                    )
                })
                token_recyclerview.visibility = View.VISIBLE
            } else {
                token_recyclerview.visibility = View.GONE
            }
            inFocusEditText = 1
            tokenJustSelected = false
        }
        from_token.setOnFocusChangeListener { v, hasFocus ->
            token_recyclerview.visibility = View.GONE
        }
        to_token.setOnFocusChangeListener { v, hasFocus ->
            token_recyclerview.visibility = View.GONE
        }
    }

    private fun startCryptoSync() {
        if (cryptoSync == null) {
            cryptoSync =  CryptoListFirebaseSync()
            cryptoSync?.startCryptoFirebbaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    eth = result as Crypto

                }

                override fun onFailure(result: Any) {
                    //  reloadData()
                }
            })

        }
    }

    fun changeVisibilitiesForLend() {
        if (operation == Constants.OPERATION_LEND) {
            divider.visibility = View.GONE
            to_amount.visibility = View.GONE
            to_token.visibility = View.GONE
            to_token_logo.visibility = View.GONE
            divider_line.visibility = View.GONE
        } else {
            divider.visibility = View.VISIBLE
            to_amount.visibility = View.VISIBLE
            to_token.visibility = View.VISIBLE
            to_token_logo.visibility = View.VISIBLE
            divider_line.visibility = View.VISIBLE
        }
    }

    fun toggleArrows() {
        isPositive = !isPositive
        if (isPositive) {
            percent_arrow.startAnimation(rotateUp)
            percent_arrow.setColorFilter(
                ContextCompat.getColor(this, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            percent_arrow.startAnimation(rotateDown)
            percent_arrow.setColorFilter(
                ContextCompat.getColor(this, R.color.red),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun addAlert() {
        if (checkErrors()) {
            return
        }
        alert.addr = App.cred?.address ?: ""
        alert.request.addr = App.cred?.address ?: ""

        alert.request.amt = from_amount.text.toString().toDouble()
        alert.request.from_tkn = from_token.text.toString()
        alert.request.to_tkn = to_token.text.toString()
        alert.request.operation = operation
        alert.request.user_id = FirebaseAuthManager.getInstance().getCurrentUserId()

        alert.created_at = System.currentTimeMillis()
        alert.operation = operation
        alert.percentage_change = percent_change.text.toString().toDouble()
        alert.player_ids.add(FirebaseAuthManager.getInstance().getUserPlayerId())
        alert.ticker = "ETH"+from_token.text.toString()

        if (!isPositive) {
            alert.percentage_change = -alert.percentage_change
        }
        FirebaseManager.getInstance().addALert(alert)
        finish()
    }

    fun checkErrors(): Boolean {
        var errorText = ""
        if (from_token.text.isNullOrEmpty() || (to_token.text.isNullOrEmpty() && operation == Constants.OPERATION_SWAP)) {
            errorText = "Enter a valid Token"
        }
        if (from_amount.text.isNullOrEmpty()) {
            errorText = "Enter a valid amount for token."
        }
        if (percent_change.text.isNullOrEmpty()) {
            errorText = "Enter a valid percentage"
        }
        error.text = errorText
        if (errorText.isNotEmpty()) {
            return true
        }
        return false
    }

    fun setUpTimeUI() {
        min_5.setOnClickListener {
            resetButtons()
            min_5.background = ContextCompat.getDrawable(this@AddAlertActivity, R.drawable.bg_button_blue_solid)
            min_5.setTextColor(ContextCompat.getColor(this@AddAlertActivity, R.color.colorPrimary))
            alert.time_in_seconds = 300
        }

        min_15.setOnClickListener {
            resetButtons()
            min_15.background = ContextCompat.getDrawable(this@AddAlertActivity, R.drawable.bg_button_blue_solid)
            min_15.setTextColor(ContextCompat.getColor(this@AddAlertActivity, R.color.colorPrimary))
            alert.time_in_seconds = 900
        }

        min_30.setOnClickListener {
            resetButtons()
            min_30.background = ContextCompat.getDrawable(this@AddAlertActivity, R.drawable.bg_button_blue_solid)
            min_30.setTextColor(ContextCompat.getColor(this@AddAlertActivity, R.color.colorPrimary))
            alert.time_in_seconds = 1800
        }

        min_60.setOnClickListener {
            resetButtons()
            min_60.background = ContextCompat.getDrawable(this@AddAlertActivity, R.drawable.bg_button_blue_solid)
            min_60.setTextColor(ContextCompat.getColor(this@AddAlertActivity, R.color.colorPrimary))
            alert.time_in_seconds = 3600
        }
    }

    private fun resetButtons() {
        min_5.background = ContextCompat.getDrawable(this, R.drawable.bg_button_blue_stroke)
        min_5.setTextColor(ContextCompat.getColor(this, R.color.grey))
        min_15.background = ContextCompat.getDrawable(this, R.drawable.bg_button_blue_stroke)
        min_15.setTextColor(ContextCompat.getColor(this, R.color.grey))
        min_30.background = ContextCompat.getDrawable(this, R.drawable.bg_button_blue_stroke)
        min_30.setTextColor(ContextCompat.getColor(this, R.color.grey))
        min_60.background = ContextCompat.getDrawable(this, R.drawable.bg_button_blue_stroke)
        min_60.setTextColor(ContextCompat.getColor(this, R.color.grey))
    }

    override fun OnTokenSelected(token: String) {
        token_recyclerview.visibility = View.GONE
        tokenJustSelected = true
        if (inFocusEditText == 0) {
            from_token.setText(token)
            try {
                Glide.with(this)
                    .load(resources.getIdentifier(token.toLowerCase(), "drawable",packageName))
                    .into(from_token_logo)
            } catch (e: Exception) {

            }
        } else {
            to_token.setText(token)
            try {
                Glide.with(this)
                    .load(resources.getIdentifier(token.toLowerCase(), "drawable",packageName))
                    .into(to_token_logo)
            } catch (e: Exception) {

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            return false
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context,AddAlertActivity::class.java)
        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }
    }
}
