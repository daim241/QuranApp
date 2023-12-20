package com.example.quranapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.databinding.FragmentFavouriteBinding
import com.example.quranapp.ui.adapter.ChaptersAdapter

class FavouriteFragment : BaseFragment(), ChaptersAdapter.updateListener {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    val dataList = ArrayList<Chapters>()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ChaptersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.favRecView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChaptersAdapter(dataList, this@FavouriteFragment, requireContext())
        recyclerView.adapter = adapter
         }

    @SuppressLint("Range")
    private fun getData(): List<Chapters> {
        val tempChapterList = ArrayList<Chapters>()
        context?.let { db -> QuranRoomDb.getQuranDB(db).let {
            val cursor = QuranRoomDb.getQuranDB(db)
            if (cursor != null) {
                val chapList = cursor.chaptersDao().getFavId()
                tempChapterList.addAll(chapList)}
        }
        }
        return tempChapterList
    }

    override fun onResume() {
        super.onResume()
        Log.d("Favorite Fragment", "This is fav onResume")
        dataList.clear()
        dataList.addAll(getData())
        adapter.notifyDataSetChanged()}

    override fun onPause() {
        super.onPause()
        Log.d("Favorite Fragment", "This is onPause") }

    override fun onDetach() {
        super.onDetach()
        Log.d("Favorite Fragment", "This is onDetach") }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Favorite Fragment", "This is onDestroy") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Favorite Fragment", "This is onCreate")
    }
    override fun onCellClickListener(position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToSurahFragment(position + 1)
        findNavController().navigate(action) }
    override fun onFavClickListener(position: Int) {
        context?.let { fav ->
            dataList[position].fav_id = 0
            QuranRoomDb.getQuranDB(fav).chaptersDao().updateChapters(dataList[position])
            dataList.removeAt(position)
            adapter.notifyDataSetChanged()
            HomeFragment.childFragment?.fragments?.forEach {
                if (it is ChapterFragment) {
                    it.updateChapList()
                }
            }
        }
    }

    fun updateList(){
        Log.d("Fav Fragment", "Func Called")
        dataList.clear()
        dataList.addAll(getData())
        adapter.notifyDataSetChanged()
    }
}