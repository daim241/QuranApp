package com.example.quranapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quranapp.R
import com.example.quranapp.data.model.Verses
import com.example.quranapp.databinding.ChaptersBinding
import com.example.quranapp.databinding.VersesBinding
import com.example.quranapp.ui.fragment.SurahFragment

class SurahAdapter(
     var versesList: List<Verses>,
    private val clickListener: SurahFragment,
    context: Context
): RecyclerView.Adapter<SurahAdapter.versesViewHolder>() {
    inner class versesViewHolder(val binding: VersesBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): versesViewHolder {
        val binding = VersesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return versesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahAdapter.versesViewHolder, position: Int) {
        with(holder){
            with(versesList[position]){
                binding.versesId.text = "${position + 1}"
                binding.textIndopak.text = this.text_indopak
            }
        }
    }

    override fun getItemCount(): Int {
        return versesList.size
    }

}
