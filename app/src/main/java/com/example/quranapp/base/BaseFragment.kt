package com.example.quranapp.base

import android.content.ContextWrapper
import androidx.fragment.app.Fragment



open class BaseFragment: Fragment() {

    fun dialogShow() {
        (requireContext() as ContextWrapper).baseContext
    }
    fun dialogDismiss() {
        (requireContext() as ContextWrapper).baseContext
    }

}