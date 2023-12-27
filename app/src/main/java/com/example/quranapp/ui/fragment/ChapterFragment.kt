package com.example.quranapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.data.viewModel.ChaptersViewModel
import com.example.quranapp.databinding.FragmentChapterBinding
import com.example.quranapp.ui.adapter.ChaptersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChapterFragment : BaseFragment(), ChaptersAdapter.updateListener {

    private val viewModel: ChaptersViewModel by viewModels()
    private var _binding: FragmentChapterBinding? = null
    private val binding get() = _binding!!
    val dataList = ArrayList<Chapters>()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ChaptersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ChaptersViewModel(QuranRoomDb.getQuranDB(requireContext()))
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
        getChap()
        recyclerView = binding.recView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChaptersAdapter(dataList, this@ChapterFragment, requireContext())
        recyclerView.adapter = adapter
        Log.d("Service", "Data List: ${dataList.size}")
        if (dataList.isNotEmpty()) {
            dataList.clear()
            dataList.addAll(viewModel.quranChap.value!!)
            adapter.notifyDataSetChanged()
        } else {
            viewModel.localDB()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getChap() {
        viewModel.isLoading.observe(viewLifecycleOwner){ it ->
            Log.d("Chapter Fragment", "isLoading $it")
            if (it) {
                dialogShow()
            } else {
                dialogDismiss()
            }
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            if (it)
                Toast.makeText(requireContext(), "${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
                Log.d("Chapter Fragment", "Data Can't Load")

        }
        viewModel.quranChap.observe(viewLifecycleOwner) { list ->
            Log.d("Chapter Fragment", "Data Load size is ${list.size}")
            dataList.clear()
            dataList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }


    override fun onCellClickListener(position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToSurahFragment(position + 1)
        findNavController().navigate(action)
    }

    override fun onFavClickListener(position: Int) {
            val type = if (dataList[position].fav_id == 0) {
                1
            } else {
                0
            }
            dataList[position].fav_id = type
            adapter.notifyDataSetChanged()
            viewModel.updateChap(type)
            HomeFragment.childFragment?.fragments?.forEach {
                if (it is FavouriteFragment) {
                    it.updateList()
                }
            }

    }

    override fun onResume() {
        super.onResume()
        Log.d("Service", "This is on Resume:")
    }

    fun updateChapList(){
        Log.d("Chapter Fragment", "Update Chap List")
        viewModel.localDB()
    }
}