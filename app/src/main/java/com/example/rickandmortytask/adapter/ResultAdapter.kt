package com.example.rickandmortytask.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortytask.databinding.ItemCharacterBinding
import com.example.rickandmortytask.databinding.ItemFooterBinding
import com.example.rickandmortytask.model.Results
import java.text.SimpleDateFormat

class ResultAdapter(val startDetailsActivity: (Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_VIEW = 0
    private val ITEM_FOOTER = 1
    private var isLoaderVisible = false

    var list: ArrayList<Results> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        if (position == list.size) return ITEM_FOOTER
        return ITEM_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_FOOTER){
            val itemFooterBinding = ItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterVH(itemFooterBinding)
        }
        val itemViewBinding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultVH(itemViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResultVH){
            val results = list[position]
            holder.bind(results)
        }
    }

    override fun getItemCount() = list.size+1

    inner class ResultVH(var itemViewBinding: ItemCharacterBinding): RecyclerView.ViewHolder(itemViewBinding.root){
        @SuppressLint("SimpleDateFormat", "ResourceType")
        fun bind(results: Results){

            itemViewBinding.tvName.text = results.name

            val formatOut = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val formatIn = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
            itemViewBinding.tvCratedDate.text = formatOut.format(formatIn.parse(results.created!!)!!)

            Glide.with(itemViewBinding.root)
                .load(results.image!!).error(Color.LTGRAY).placeholder(Color.LTGRAY).into(itemViewBinding.ivProfile)

            itemViewBinding.llContainer.setOnClickListener {
                startDetailsActivity.invoke(results.id!!)
            }
        }
    }

    inner class FooterVH(var itemViewBinding: ItemFooterBinding): RecyclerView.ViewHolder(itemViewBinding.root){

    }
}