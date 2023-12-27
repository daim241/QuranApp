package com.example.quranapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.data.viewModel.ChaptersViewModel
import com.example.quranapp.databinding.FragmentFavouriteBinding
import com.example.quranapp.ui.adapter.ChaptersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : BaseFragment(), ChaptersAdapter.updateListener {
    private val viewModel: ChaptersViewModel by viewModels()
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    val dataList = ArrayList<Chapters>()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ChaptersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      viewModel = ChaptersViewModel(QuranRoomDb.getQuranDB(requireContext()))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuranList()

        recyclerView = binding.favRecView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChaptersAdapter(dataList, this@FavouriteFragment, requireContext())
        recyclerView.adapter = adapter


    }


    override fun onResume() {
        super.onResume()
        Log.d("Favorite Fragment", "This is fav onResume")
//        viewModel.quranChap.observe(viewLifecycleOwner){ list->
//            dataList.clear()
//            viewModel.getAllFavChapters()
//            dataList.addAll(list)
//            adapter.notifyDataSetChanged()
//
//        }

        viewModel.getAllFavChapters()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Favorite Fragment", "This is onPause") }

    override fun onDetach() {
        super.onDetach()
        Log.d("Favorite Fragment", "This is onDetach") }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Favorite Fragment", "This is onDestroy") }


    override fun onCellClickListener(position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToSurahFragment(position + 1)
        findNavController().navigate(action) }
    override fun onFavClickListener(position: Int) {
            dataList[position].fav_id = 0
            viewModel.updateChap(position)
            dataList.removeAt(position)
            adapter.notifyDataSetChanged()
            HomeFragment.childFragment?.fragments?.forEach {
                if (it is ChapterFragment) {
                    it.updateChapList()
                }
            }
    }

    private fun observeQuranList() {
        viewModel.quranChap.observe(viewLifecycleOwner){ favlist ->
            Log.d("Fav Fragment", "Data Load size is  ${favlist.size}")
            dataList.clear()
            dataList.addAll(favlist)
            adapter.notifyDataSetChanged()
        }
    }

    fun updateList(){
        Log.d("Fav Fragment", "Func Called")
        viewModel.getAllFavChapters()
    }
}