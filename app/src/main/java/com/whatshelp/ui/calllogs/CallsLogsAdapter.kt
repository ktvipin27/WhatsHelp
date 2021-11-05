package com.whatshelp.ui.calllogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.data.model.CallLog
import com.whatshelp.databinding.ItemCallLogBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

val diffCallback = object : DiffUtil.ItemCallback<CallLog>() {
    override fun areItemsTheSame(oldItem: CallLog, newItem: CallLog): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: CallLog, newItem: CallLog): Boolean {
        return oldItem == newItem
    }
}

/**
 * Created by Vipin KT on 04/11/21
 */
@FragmentScoped
class CallLogsAdapter @Inject constructor() :
    ListAdapter<CallLog, CallLogsAdapter.CallViewHolder>(diffCallback) {

    private var itemClickListener: ((callLog: CallLog) -> Unit)? = null

    inner class CallViewHolder(val binding: ItemCallLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemCallLogBinding.inflate(layoutInflater, parent, false)

        return CallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = currentList[position]
        holder.binding.call = call
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(call)
        }
    }

    override fun onViewRecycled(holder: CallViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }

    fun setItemClickListener(listener: (callLog: CallLog) -> Unit) {
        itemClickListener = listener
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemClickListener = null
    }
}