package com.guru.cryptotalk.ui.symbols

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.guru.cryptotalk.ui.symbols.CryptoListFragment

class CryptoPagerAdapter(val titles: ArrayList<String>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return CryptoListFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    override fun getItemPosition(@NonNull `object`: Any): Int {
        return POSITION_NONE
    }

    override fun saveState(): Parcelable? {
        return null
    }

}