package com.guru.cryptotalk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.guru.cryptotalk.Constants
import com.guru.cryptotalk.R
import com.guru.cryptotalk.ui.alerts.AlertsFragment
import com.guru.cryptotalk.ui.symbols.CryptoListFragment
import com.guru.cryptotalk.ui.news.NewsFragment
import com.guru.cryptotalk.ui.symbols.CryptoListParentFramgnet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var TAG = Constants.TAG_CRYPTO
    private var handler : Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.AppTheme_Dark)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        handler = Handler()
        loadFragment(TAG)
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
            Constants.TAG_CRYPTO -> CryptoListParentFramgnet()
            Constants.TAG_ALERTS -> AlertsFragment()
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
}
