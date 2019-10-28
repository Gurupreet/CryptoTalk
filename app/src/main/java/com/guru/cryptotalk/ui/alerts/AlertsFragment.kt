package com.guru.cryptotalk.ui.alerts


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.guru.cryptotalk.App
import com.guru.cryptotalk.Constants
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import com.guru.cryptotalk.data.api.api.*
import com.guru.cryptotalk.data.api.firebase.FirebaseAuthManager
import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.AlertFirebaseSync
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.CryptoListFirebaseSync
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.UserFirebaseSync
import com.guru.cryptotalk.data.api.model.Alert
import com.guru.cryptotalk.data.api.model.Crypto
import com.guru.cryptotalk.data.api.model.User
import com.guru.cryptotalk.ui.adapters.AlertAdapter
import com.guru.cryptotalk.ui.wallet.ImportWalletActivity
import kotlinx.android.synthetic.main.fragment_alerts.*
import kotlinx.android.synthetic.main.row_price_layout.*
import kotlinx.android.synthetic.main.row_price_layout.bar_view
import kotlinx.android.synthetic.main.row_price_layout.day_change
import kotlinx.android.synthetic.main.row_price_layout.last_price
import kotlinx.android.synthetic.main.row_price_layout.name
import kotlinx.android.synthetic.main.row_price_layout.price_layout
import kotlinx.android.synthetic.main.row_price_layout.symbol
import kotlinx.android.synthetic.main.row_price_layout.view.*
import org.json.JSONObject
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AlertsFragment : Fragment() {
    private lateinit var adapter: AlertAdapter

    private val list = ArrayList<Alert>()
    private var alertFirebaseSync: AlertFirebaseSync? = null
    private var cryptoSync: CryptoListFirebaseSync? = null
    private var userSync: UserFirebaseSync? = null
    var user: User? = null
    var eth: Crypto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertFirebaseSync?.stopFirebaseSync()
        alertFirebaseSync = null
        cryptoSync?.stopFirebaseSync()
        cryptoSync = null
        userSync?.stopFirebaseSync()
        userSync = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        wallet.setOnClickListener {
           WalletBottomSheetDialog().show(fragmentManager!!, "")
        }
        fab.setOnClickListener {
            startActivity(AddAlertActivity.getIntent(it.context))
        }
        wallet_address.text = App.cred?.address ?: "Tap to Add Wallet"
        SharedPrefrenceManager.keyData.observe(this, Observer {
            App.cred = Credentials.create(SharedPrefrenceManager.getPrivateKey(context!!))
            FirebaseManager.getInstance().updateUser("addr", App.cred?.address!!)
            getBalance()
        })
        getBalance()
        startAlertSync()
        startCryptoSync()
        startUserFirebaseSync()
        setUpEthLayout()
    }

    fun setUpEthLayout() {
        eth?.let { crypto->
            eth_layout?.apply {
                symbol?.text = "ETH - "+crypto?.name?.replace("ETH", "")
                name?.text = "exchange: " + crypto?.exchange
                last_price?.text = crypto?.last_price.toString()
                if (crypto?.day_change!! > 0) {
                    day_change?.text = Constants.getUpArrow()+
                           App.df.format(crypto?.day_change_percentage!!).toString() + "%"
                    day_change?.setTextColor(
                        ContextCompat.getColor(
                            price_layout.context,
                            R.color.green
                        )
                    )
                    bar_view?.setBackgroundColor(
                        ContextCompat.getColor(
                            price_layout.context,
                            R.color.green
                        )
                    )
                } else {
                    day_change?.text = Constants.getDownArrow()+
                           App.df.format(crypto?.day_change_percentage!!).toString() + "%"
                    day_change?.setTextColor(
                        ContextCompat.getColor(
                            price_layout.context,
                            R.color.red
                        )
                    )
                    bar_view?.setBackgroundColor(
                        ContextCompat.getColor(
                            price_layout.context,
                            R.color.red
                        )
                    )
                }
            }
        }

    }

    private fun startCryptoSync() {
        if (cryptoSync == null) {
            cryptoSync =  CryptoListFirebaseSync()
            cryptoSync?.startCryptoFirebbaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    eth = result as Crypto
                    setUpEthLayout()

                }

                override fun onFailure(result: Any) {
                    //  reloadData()
                }
            })

        }
    }

    private fun startUserFirebaseSync() {
        if (userSync == null && FirebaseAuthManager.getInstance().getCurrentUserId().isNotEmpty()) {
            userSync =  UserFirebaseSync(FirebaseAuthManager.getInstance().getCurrentUserId())
            userSync?.startUserFirebaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    user = result as User
                    setUpBalance()

                }

                override fun onFailure(result: Any) {
                    //  reloadData()
                }
            })

        }
    }

    fun setUpBalance() {
        balance_view?.visibility  = View.VISIBLE
        user?.let {
            val list = it.balance.filter { it.substringAfter('_', "").toDouble() > 0 }.sorted()
            if (list.isNotEmpty()) {
                tkn1.visibility = View.VISIBLE
                tkn1.text = list.get(0).replace("_", ": ")
                if (list.size > 1) {
                    tkn2.visibility = View.VISIBLE
                    tkn2.text = list.get(1).replace("_", ": ")
                }
                if (list.size > 2) {
                    tkn3.visibility = View.VISIBLE
                    tkn3.text = list.get(2).replace("_", ": ")
                }
                if (list.size > 3) {
                    tkn4.visibility = View.VISIBLE
                    tkn4.text = list.get(3).replace("_", ": ")
                }
            } else {
                tkn1.visibility = View.VISIBLE
                tkn2.visibility = View.VISIBLE
                tkn3.visibility = View.VISIBLE
                tkn4.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRecyclerview() {
        adapter = AlertAdapter(list)
        val layoutManager = LinearLayoutManager(context)
        alert_recyclerview.adapter = adapter
        alert_recyclerview.layoutManager = layoutManager
    }

    private fun getBalance() {
        if (App.cred != null) {
            wallet_address.text = App.cred?.address ?: "Tap to Add Wallet"
            ApiManager.getInstance().getBalance(App.cred!!.address, object : ApiResponseHandler {
                override fun onComplete(data: Any) {
                    val apiResponse = data as ApiResponse
                    FirebaseManager.getInstance().updateUserBalance(apiResponse)
                }
                override fun onFailed(data: Any) {

                }

            })
        }
    }

    private fun startAlertSync() {
        if (alertFirebaseSync == null && FirebaseAuthManager.getInstance().getCurrentUserId().isNotEmpty()) {
            alertFirebaseSync = AlertFirebaseSync()
            list.clear()
            adapter.notifyDataSetChanged()
            alertFirebaseSync?.startAlertFirebaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    var alert = result as Alert
                    when (observerType) {
                        FirebaseObserverType.CHILD_ADDED -> {
                            list.add(alert)
                            list.reverse()
                           adapter.notifyDataSetChanged()

                            //reloadData()
                        }

                        FirebaseObserverType.CHILD_CHANGED -> {
                            val iterator :MutableListIterator<Alert> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().key == alert.key) {
                                    iterator.set(alert)
                                    adapter.notifyItemChanged(index)
                                    break
                                }
                                index++
                            }
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<Alert> = list.listIterator()
                            var index = 0
                            while (iterator.hasNext()) {
                                if (iterator.next().key == alert.key) {
                                    iterator.remove()
                                    adapter.notifyItemRemoved(index)
                                    break
                                }
                                index++
                            }
                        }
                    }

                }

                override fun onFailure(result: Any) {
                    //  reloadData()
                    adapter.notifyDataSetChanged()
                }
            })

        }
    }

}
