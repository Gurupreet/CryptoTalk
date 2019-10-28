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






class WalletBottomSheetDialog : BaseBottomSheetFragment() {

    private lateinit var parentView: View
    private val REQUEST_CODE = 100

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_wallet_bottom_sheet
    }

    override fun setupView(contentView: View) {
        parentView = contentView
        parentView.apply {
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
                    info_text.setTextColor(ContextCompat.getColor(context!!, R.color.red))
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