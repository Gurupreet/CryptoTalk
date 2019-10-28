package com.guru.cryptotalk.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat


import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.guru.cryptotalk.R


abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    abstract fun getLayoutResourceId(): Int
    abstract fun setupView(contentView: View)

    private val container: LinearLayout by lazy {
        parentView.findViewById<LinearLayout>(R.id.container)
    }

    private var mBehavior: BottomSheetBehavior<View>? = null
    private lateinit var contentView: View
    private lateinit var parentView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        //init dialog
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        parentView = View.inflate(context, R.layout.fragment_base_bottom_sheet, null)
        dialog.setContentView(parentView)

        //init UI elements
        (parentView.parent as View?)?.setBackgroundColor(ContextCompat.getColor(dialog.context, android.R.color.transparent))
        mBehavior = BottomSheetBehavior.from(parentView.parent as View?)
        contentView = inflater.inflate(getLayoutResourceId(), container)
        setupView(contentView)

        //setup sliding behavior
        mBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                }

            }
        })

        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
