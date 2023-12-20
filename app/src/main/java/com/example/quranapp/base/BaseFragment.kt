package com.example.quranapp.base

import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    fun dialogShow() {
        (requireContext() as BaseActivity).dialogShow()
    }
    fun dialogDismiss() {
        (requireContext() as BaseActivity).dialogDismiss()
    }

}