package com.example.my11.Matches

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class PageAdapter(fm: FragmentManager, var tabcount: Int) :
    FragmentPagerAdapter(fm, tabcount) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FutureMatchFragment()
            1 -> LiveMatchFragment()
            2 -> PlayedMatchFragment()

            else -> FutureMatchFragment()
        }
    }

    override fun getCount(): Int {
        return tabcount
    }


}