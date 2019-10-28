package com.guru.cryptotalk.ui.wallet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import kotlinx.android.synthetic.main.activity_import_wallet.*
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletUtils
import java.io.File
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import java.security.Security.insertProviderAt
import java.security.Security.removeProvider
import java.security.Security


class ImportWalletActivity : AppCompatActivity() {
    var file : File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_wallet)
        getWalletDiractory()
        initUI()
    }

    private fun initUI() {
        done.setOnClickListener {
            setupBouncyCastle()
            if (private_key.text.isNotEmpty() && WalletUtils.isValidPrivateKey(private_key.text.trim().toString())) {
                SharedPrefrenceManager.savePrivateKey(this, private_key.text.trim().toString())
                val wallet = WalletUtils.generateLightNewWalletFile(private_key.text.trim().toString(),file)
                SharedPrefrenceManager.saveWalletFileName(this, wallet)
                Toast.makeText(this, "Wallet added $wallet", Toast.LENGTH_LONG).show()
                Log.d("path: %s", file?.absolutePath+"/"+SharedPrefrenceManager.getWalletFileName(this))
                Handler().postDelayed({
                   val cred = Credentials.create(SharedPrefrenceManager.getPrivateKey(this))
                    // val cred = WalletUtils.(SharedPrefrenceManager.getPrivateKey(this), file?.absolutePath+"/"+SharedPrefrenceManager.getWalletFileName(this))
                    Toast.makeText(this, "Wallet added ${cred.address}", Toast.LENGTH_LONG).show()

                }, 4000)
                // finish()
            } else {
                key_input_layout.error = "Please enter valid private key."
            }
        }
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        insertProviderAt(BouncyCastleProvider(), 1)
    }


    private fun getWalletDiractory() {
        if (!File(filesDir.absolutePath).mkdir()) {
            File(filesDir.absolutePath).parentFile.mkdir()
            file = File(filesDir.absolutePath)
        }
        file =  File(filesDir.absolutePath)
    }
    companion object {
        fun getIntent(context: Context) = Intent(context, ImportWalletActivity::class.java)
    }
}
