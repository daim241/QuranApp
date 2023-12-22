package com.example.quranapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quranapp.base.BaseFragment
import com.example.quranapp.data.database.QuranRoomDb
import com.example.quranapp.data.model.Verses
import com.example.quranapp.data.viewModel.SurahViewModel
import com.example.quranapp.databinding.FragmentSurahBinding
import com.example.quranapp.ui.adapter.SurahAdapter

class SurahFragment : BaseFragment() {
    private lateinit var viewModel: SurahViewModel
    private var _binding: FragmentSurahBinding? = null
    private val args: SurahFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private var versesList = ArrayList<Verses>()
    lateinit var adapter: SurahAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = SurahViewModel(QuranRoomDb.getQuranDB(requireContext()), args.verses.toString())
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
        getVerses()
        binding.versesRecView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = SurahAdapter(versesList, this@SurahFragment, requireContext())
        binding.versesRecView.adapter = adapter
        Log.d("Service", "Verses List Size is: ${versesList.size} ")
        if (versesList.isEmpty()){
            if (localDbData().isNotEmpty()) {
                viewModel.quranVerses.observe(viewLifecycleOwner){ list ->
                    Log.d("Chapter Fragment", "Data Cant Load ${list.size}")
                    versesList.clear()
                    versesList.addAll(list)
                    adapter.notifyDataSetChanged()
                }
            }
            else {
               viewModel.getVerses(args.verses.toString())
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getVerses(){
        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it)
                Log.d("Surah Fragment", "isLoading $it")
            if (it) {
                dialogShow() }
            else {
                dialogDismiss() }
        }
        viewModel.isError.observe(viewLifecycleOwner){
            if (it)
                Toast.makeText(requireContext(), "${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
            Log.d("Chapter Fragment", "Data Can't Load")
        }
        viewModel.quranVerses.observe(viewLifecycleOwner){ list ->
            Log.d("Chapter Fragment", "Data Cant Load ${list.size}")
            versesList.clear()
            versesList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}