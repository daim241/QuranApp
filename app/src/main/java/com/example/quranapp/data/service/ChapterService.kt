package com.example.quranapp.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.api.ApiQuran
import com.example.quranapp.data.api.QuranApiInterface
import com.example.quranapp.data.model.Quran
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterService: Service() {
    private var binder: IBinder = LocalService()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getChap()
        Log.d("Service","onStartCommand")
        return START_STICKY
    }

    private fun getChap() {
        val apiInterface = ApiQuran.getInstance().create(QuranApiInterface::class.java)
        val call: Call<Quran> = apiInterface.getApi(en = "")
        call.enqueue(object : Callback<Quran> {
            override fun onResponse(call: Call<Quran>, response: Response<Quran>) {
                val record = response.body()!!.chapters
                QuranRoomDb.getQuranDB(this@ChapterService).chaptersDao().addAllChapters(record)
                sendLocalBroadcast()
            }
            override fun onFailure(call: Call<Quran>, t: Throwable) {
                Toast.makeText(this@ChapterService,t.message, Toast.LENGTH_LONG).show()
                sendLocalBroadcast()
            }
        })
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    inner class LocalService : Binder()
    private fun sendLocalBroadcast() {
        val intent = Intent("localBroadcastForData").apply {
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        stopSelf()
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.e("Service", "onDestroy")
    }
}