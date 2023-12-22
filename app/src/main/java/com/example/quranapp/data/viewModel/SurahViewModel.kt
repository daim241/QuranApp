package com.example.quranapp.data.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quranapp.data.api.ApiQuran
import com.example.quranapp.data.api.QuranApiInterface
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.data.model.Quran
import com.example.quranapp.data.model.Verses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurahViewModel(private val quranRoomDb: QuranRoomDb,  chapId: String): ViewModel() {
    private val _data = MutableLiveData<List<Verses>>()
    val quranVerses: LiveData<List<Verses>> get() = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""

    init {
        localDB(chapId)
    }

    fun getVerses(chapId: String){
        _isLoading.postValue(true)
        _isError.postValue(false)
        val apiInterface = ApiQuran.getInstance().create(QuranApiInterface::class.java)
        val call: Call<Quran> = apiInterface.getVersesapi(chapterNumber = chapId)
        call.enqueue(object : Callback<Quran> {
            override fun onResponse(call: Call<Quran>, response: Response<Quran>) {
                val responseBody = response.body()!!.verses
                responseBody.forEach { index ->
                    index.surahIndex = chapId
                }
                insertVerses(responseBody)
                if(!response.isSuccessful || responseBody == null){
                    onError("Data Processing Error")
                    return
                }
                _isLoading.postValue(false)
                _data.postValue(responseBody)
                Log.d("Surah View Model", "Data is fetched from Api")
            }
            override fun onFailure(call: Call<Quran>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun localDB(chapId: String){
        val versesList = quranRoomDb.versesDao().getAllVersesData(chapId)
        if (versesList != null){
            _data.postValue(versesList)
        }
        else{
            getVerses(chapId)
        }
    }

    fun insertVerses(record: List<Verses>){
        Log.d("Chap View Model", "insert record size is ${record.size}")
        quranRoomDb.versesDao().insertVerse(record)
    }

    private fun onError(inputMessage: String?){
        val message = if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
        else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isLoading.postValue(false)
        _isError.postValue(true)
    }
}