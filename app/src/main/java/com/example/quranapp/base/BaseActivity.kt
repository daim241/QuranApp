package com.example.quranapp.base

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quranapp.R

open class BaseActivity: AppCompatActivity() {
    lateinit var builder: AlertDialog
    fun dialogShow(){
        builder = AlertDialog.Builder(this)
            .create()
        val view = layoutInflater.inflate(R.layout.dialog,null)
        builder.setView(view)
        builder.show()
    }
    fun dialogDismiss(){
        builder.dismiss()
    }
}