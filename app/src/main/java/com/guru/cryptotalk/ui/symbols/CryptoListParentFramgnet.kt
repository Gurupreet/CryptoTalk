package com.guru.cryptotalk.ui.symbols


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.guru.cryptotalk.R
import kotlinx.android.synthetic.main.fragment_crypto_list_parent_framgnet.*

class CryptoListParentFramgnet : Fragment() {
    private lateinit var cryptoPagerAdapter: CryptoPagerAdapter
    private val pagerTitles = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerTitles.clear()
        pagerTitles.add("BTC")
        pagerTitles.add("ETH")
        pagerTitles.add("EOS")
        pagerTitles.add("All")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crypto_list_parent_framgnet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cryptoPagerAdapter = CryptoPagerAdapter(pagerTitles, childFragmentManager)
        main_viewpager.adapter = cryptoPagerAdapter
        main_tab_layout.setupWithViewPager(main_viewpager)
    }


}
