package com.example.quranapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.quranapp.ui.fragment.ChapterFragment
import com.example.quranapp.ui.fragment.FavouriteFragment

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return ChapterFragment()
            }

            1 -> {
                return FavouriteFragment()
            }

            else -> {

                return ChapterFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position){
            0 -> {
                return "Surah"
            }
            1 -> {
                return  "Favourites"
            }
        }
        return super.getPageTitle(position)
    }
}