package com.guru.cryptotalk.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.guru.cryptotalk.Constants
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import com.guru.cryptotalk.data.api.firebase.FirebaseAuthManager
import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.guru.cryptotalk.ui.alerts.AlertsFragment
import com.guru.cryptotalk.ui.symbols.CryptoListFragment
import com.guru.cryptotalk.ui.news.NewsFragment
import com.guru.cryptotalk.ui.symbols.CryptoListParentFramgnet
import kotlinx.android.synthetic.main.activity_main.*
import org.web3j.crypto.Credentials
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletUtils
import java.io.File

class MainActivity : AppCompatActivity() {
    private var TAG = Constants.TAG_CRYPTO
    private var handler : Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.AppTheme_Dark)
        FirebaseAuthManager.getInstance().fetchUser()
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        handler = Handler()
        loadFragment(TAG)
        FirebaseManager.getInstance().updateUser("last_active", System.currentTimeMillis())
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_label -> {
                TAG = Constants.TAG_CRYPTO
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_test -> {
                TAG = Constants.TAG_ALERTS
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news -> {
                TAG = Constants.TAG_NEWS
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(type : String) {
        val fragment  = when (type) {
            Constants.TAG_CRYPTO -> AlertsFragment()
            Constants.TAG_ALERTS -> CryptoListFragment()
            Constants.TAG_NEWS -> NewsFragment()
            else -> loadFragment(Constants.TAG_CRYPTO)
        }
        handler?.post {
            try {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.containerView, fragment as Fragment)
                fragmentTransaction.commit()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Error loading fragments", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {

            } else {
                if (result.contents.isNotEmpty() && result.contents.startsWith(Constants.KEY_PREFIX)) {
                    val key = result.contents.replace(Constants.KEY_PREFIX, "")
                    if (WalletUtils.isValidPrivateKey(key)) {
                        SharedPrefrenceManager.savePrivateKey(this, key)
                    } else {
                        Toast.makeText(this, "Not a valid private key. Please scan a valid account.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
