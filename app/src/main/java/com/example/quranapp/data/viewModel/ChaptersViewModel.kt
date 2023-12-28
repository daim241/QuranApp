package com.example.quranapp.data.viewModel

import android.icu.text.Transliterator.Position
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranapp.data.api.QuranApiInterface
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.data.model.Quran
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChaptersViewModel @Inject constructor(
    private val quranRoomDb: QuranRoomDb,
    private val quranApi: QuranApiInterface
) : ViewModel() {
    private val _data = MutableLiveData<List<Chapters>>()
    val quranChap: LiveData<List<Chapters>> get() = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val  isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""

    private fun getChap() {
        _isLoading.postValue(true)
        _isError.postValue(false)

        val call: Call<Quran> = quranApi.getApi(en = "")
        call.enqueue(object : Callback<Quran> {
            override fun onResponse(call: Call<Quran>, response: Response<Quran>) {
                val responseBody = response.body()!!.chapters
                insertChapters(responseBody)
                if(!response.isSuccessful || responseBody == null){
                    onError("Data Processing Error")
                    return
                }
                _isLoading.postValue(false)
                _data.postValue(responseBody)
                Log.d("Chapters View Model", "Data is fetched from Api")
            }
            override fun onFailure(call: Call<Quran>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    fun localDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val chapList = quranRoomDb.chaptersDao().getAllChaptersData()
            if (chapList.isNotEmpty()) {
                _data.postValue(chapList)
            } else{
                getChap()
            }
        }
    }

    fun insertChapters(record: List<Chapters>){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Chapter View Model", "insert record size is ${record.size}")
            quranRoomDb.chaptersDao().addAllChapters(record)
        }
    }

    fun getAllFavChapters() {
        viewModelScope.launch(Dispatchers.IO){
            val allFavId = quranRoomDb.chaptersDao().getFavId()
            _data.postValue(allFavId)
        }
    }

    fun updateChap(chapters: Chapters){
        viewModelScope.launch(Dispatchers.IO){
            quranRoomDb.chaptersDao().updateChapters(chapters)
        }
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