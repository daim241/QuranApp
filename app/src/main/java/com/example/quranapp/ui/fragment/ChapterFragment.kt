package com.example.quranapp.ui.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.service.ChapterService
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.databinding.FragmentChapterBinding
import com.example.quranapp.ui.adapter.ChaptersAdapter

class ChapterFragment : BaseFragment(), ChaptersAdapter.updateListener {

    private var _binding: FragmentChapterBinding? = null
    private val binding get() = _binding!!
    val dataList = ArrayList<Chapters>()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ChaptersAdapter

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            context?.let { con ->
                val result = QuranRoomDb.getQuranDB(con)
                if (result != null) {
                    getChapList()
                } else {
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
                }
                Log.d("Service", "This is Broadcast")
                LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(this)
            }
        }
    }

    private fun getChapList() {
        dataList.addAll(getData())
        adapter.notifyDataSetChanged()
        dialogDismiss()
    }

    @SuppressLint("Range")
    private fun getData(): List<Chapters> {
        val tempChapterList = ArrayList<Chapters>()
        context?.let { db ->
            QuranRoomDb.getQuranDB(db).let {
                val cursor = QuranRoomDb.getQuranDB(db)
                if (cursor != null) {
                    val chapList = cursor.chaptersDao().getAllChaptersData()
                    tempChapterList.addAll(chapList)
                }
            }
        }
        return tempChapterList
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChaptersAdapter(dataList, this@ChapterFragment, requireContext())
        recyclerView.adapter = adapter
        Log.d("Service", "Data List: ${dataList.size}")
        if (dataList.isEmpty()) {
            dialogShow()
            if (getData().isNotEmpty()) {
                getChapList()
            } else {
                LocalBroadcastManager.getInstance(requireContext())
                    .registerReceiver(mMessageReceiver,
                        IntentFilter("localBroadcastForData").also {
                            it.addAction(Intent.ACTION_VIEW)
                        })
                val intent = Intent(requireContext(), ChapterService::class.java)
                (requireContext() as AppCompatActivity).startService(intent)
            }
        }
    }

    override fun onCellClickListener(position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToSurahFragment(position + 1)
        findNavController().navigate(action)
    }

    override fun onFavClickListener(position: Int) {
        context?.let { fav ->
            val type = if (dataList[position].fav_id == 0) {
                1
            } else {
                0
            }
            dataList[position].fav_id = type
            adapter.notifyDataSetChanged()
            QuranRoomDb.getQuranDB(fav).chaptersDao().updateChapters(dataList[position])
            HomeFragment.childFragment?.fragments?.forEach {
                    if (it is FavouriteFragment) {
                        it.updateList()
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Service", "This is on Resume:")
    }

    fun updateChapList(){
        Log.d("Chapter Fragment", "Update Chap List")
        dataList.clear()
        dataList.addAll(getData())
        adapter.notifyDataSetChanged()
    }
}