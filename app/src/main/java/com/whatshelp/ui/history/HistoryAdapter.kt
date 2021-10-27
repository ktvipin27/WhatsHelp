package com.whatshelp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.data.db.entity.History
import com.whatshelp.databinding.ItemHistoryBinding
import com.whatshelp.ui.history.HistoryAdapter.HistoryViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

val diffCallback = object : DiffUtil.ItemCallback<History>() {
    override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem.id == newItem.id
    }
}

/**
 * Created by Vipin KT on 15/10/21
 */
@FragmentScoped
class HistoryAdapter @Inject constructor() :
    ListAdapter<History, HistoryViewHolder>(diffCallback) {

    private var itemClickListener: ((history:History)->Unit)? = null

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemHistoryBinding.inflate(layoutInflater, parent, false)

        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = currentList[position]
        holder.binding.history = history
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(history)
        }
    }

    override fun onViewRecycled(holder: HistoryViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }

    fun setItemClickListener(listener: (history:History)->Unit){
        itemClickListener = listener
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemClickListener = null
    }
}