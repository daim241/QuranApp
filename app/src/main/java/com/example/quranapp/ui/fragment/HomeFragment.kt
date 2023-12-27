package com.example.quranapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
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