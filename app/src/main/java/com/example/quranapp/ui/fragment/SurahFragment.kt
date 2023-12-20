package com.example.quranapp.ui.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.service.VerseService
import com.example.quranapp.data.model.Verses
import com.example.quranapp.databinding.FragmentSurahBinding
import com.example.quranapp.ui.adapter.SurahAdapter

class SurahFragment : BaseFragment() {
    private var _binding: FragmentSurahBinding? = null
    private val args: SurahFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private var versesList = ArrayList<Verses>()
    lateinit var adapter: SurahAdapter

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            context?.let { con ->
                val result =  QuranRoomDb.getQuranDB(con)
                if (result != null) {
                    getVersesList()

                }
                else {
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
                }
                Log.d("Surah Service", "This is Broadcast")
                LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(this) }
        }
    }

    private fun getVersesList() {
        versesList.addAll(localDbData())
        adapter.notifyDataSetChanged()
        dialogDismiss()
    }

    @SuppressLint("Range")
    private fun localDbData(): List<Verses>{
        val tempVerseList = ArrayList<Verses>()
        context?.let { db ->
            QuranRoomDb.getQuranDB(db).let {
                val cursor = QuranRoomDb.getQuranDB(db)
                if (cursor != null) {
                    val verseList = cursor.versesDao().getAllVersesData(args.verses.toString())
                    tempVerseList.addAll(verseList) }
            }
        }
        return tempVerseList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSurahBinding.inflate(inflater, container, false)
        return  binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.versesRecView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = SurahAdapter(versesList, this@SurahFragment, requireContext())
        binding.versesRecView.adapter = adapter
        Log.d("Service", "Verses List Size is: ${versesList.size} ")
        if (versesList.isEmpty()){
            dialogShow()
            if (localDbData().isNotEmpty()) {
                getVersesList()
            }
            else {
                LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                    IntentFilter("localBroadcastForData").also {
                        it.addAction(Intent.ACTION_VIEW) })
                val intent = Intent(requireContext(), VerseService::class.java)
                intent.putExtra("ChapID", args.verses.toString())
                (requireContext() as AppCompatActivity).startService(intent)
            }
        }
    }


}