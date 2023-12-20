package com.example.quranapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.databinding.FragmentUserProfileBinding


class UserProfileFragment : BaseFragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return  binding.root
    }


}