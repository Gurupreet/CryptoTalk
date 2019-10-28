package com.guru.cryptotalk.ui.alerts

import android.view.View
import com.guru.cryptotalk.R
import com.guru.cryptotalk.ui.base.BaseBottomSheetFragment
import kotlinx.android.synthetic.main.dialog_wallet_bottom_sheet.*
import android.content.Intent
import androidx.core.content.ContextCompat
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import org.web3j.crypto.WalletUtils

import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.dialog_wallet_bottom_sheet.view.*
import android.widget.Toast
import com.guru.cryptotalk.App
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent


class WalletBottomSheetDialog : BaseBottomSheetFragment() {

    private lateinit var parentView: View
    private val REQUEST_CODE = 100

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_wallet_bottom_sheet
    }

    override fun setupView(contentView: View) {
        parentView = contentView
        parentView.apply {
            App.cred?.let { cred ->
                title.text = "Change Wallet"
                wallet_layout.visibility = View.VISIBLE
                addr.text = cred.address
                copy_addr.setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                    val clip = ClipData.newPlainText("addr", cred.address)
                    clipboard!!.setPrimaryClip(clip)
                }
                view_ethersacn.setOnClickListener {
                    openHash(cred.address)
                }
            }
            scan.setOnClickListener {
                scanIntent()
            }
            done.setOnClickListener {
                if (private_key.text.toString().isNotEmpty() && WalletUtils.isValidPrivateKey(
                        private_key.text.trim().toString()
                    )
                ) {
                    SharedPrefrenceManager.savePrivateKey(
                        context!!,
                        private_key.text.trim().toString()
                    )
                    dialog?.dismiss()
                } else {
                    info_text.text = "Enter a valid Private key"
                    info_text.setTextColor(ContextCompat.getColor(context!!, com.guru.cryptotalk.R.color.red))
                }
            }
        }

    }

    private fun scanIntent() {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan QR") // Use a specific camera of the device
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    fun openHash(hash: String) {
        val url = "https://etherscan.io/address/"+hash
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}