package com.example.quranapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.quranapp.R
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.databinding.FragmentHomeBinding
import com.example.quranapp.ui.adapter.PagerAdapter

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

companion object{
     var childFragment: FragmentManager? = null
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragment = childFragmentManager
        val viewPager = binding.viewPager
        viewPager.adapter = PagerAdapter(childFragmentManager)
        val tabLayout = binding.tabLayout
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
               Log.d("Home Fragment", "On Page Position $position")
//                    childFragmentManager.fragments.forEach {
//                        if (position == 1) {
//                        if (it is FavouriteFragment)
//                        {
//                            it.updateList()
//                        }
//                    }
//                        else{
//                            if (it is ChapterFragment)
//                            {
//                                it.updateChapList()
//                            }
//                        }
//                }
            }
            override fun onPageScrollStateChanged(state: Int) {} })
    }

    override fun onPause() {
        super.onPause()
        Log.d("Home Fragment", "This is onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Home Fragment", "This is onResume")
    }

}