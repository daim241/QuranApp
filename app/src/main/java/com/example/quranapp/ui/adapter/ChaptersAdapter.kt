package com.example.quranapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quranapp.R
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.databinding.ChaptersBinding

class ChaptersAdapter(
    var dataList: List<Chapters>,
    private val clicklistener: updateListener,
    context: Context
): RecyclerView.Adapter<ChaptersAdapter.chapterViewHolder>() {

    inner class chapterViewHolder(val binding: ChaptersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chapterViewHolder {
        val binding = ChaptersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return chapterViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: chapterViewHolder, position: Int) {
        with(holder) {
            with(dataList[position]) {
                binding.id.text = this.id.toString()
                binding.revelationPlace.text = this.revelation_place
                binding.nameSimple.text = this.name_simple
                binding.nameArabic.text = this.name_arabic
                binding.versesCount.text = this.verses_count + " Verses"
            }
            if (dataList[position].fav_id == 0) {
                binding.favId.setImageResource(R.drawable.baseline_favorite_border_24) }

            else {
                binding.favId.setImageResource(R.drawable.baseline_favorite_24) }

            holder.itemView.setOnClickListener {
                clicklistener.onCellClickListener(position) }

            holder.binding.favId.setOnClickListener {
                clicklistener.onFavClickListener(position)
                Log.d("Service", "Hold") }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    interface updateListener{
        fun onCellClickListener(position: Int)
        fun onFavClickListener(position: Int)
    }
}