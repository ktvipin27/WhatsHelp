package com.whatshelp.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatshelp.data.db.entity.Message
import com.whatshelp.databinding.ItemMessageBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

val diffCallback = object : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}

/**
 * Created by Vipin KT on 15/10/21
 */
@FragmentScoped
class MessagesAdapter @Inject constructor() :
    ListAdapter<Message, MessagesAdapter.MessageViewHolder>(diffCallback) {

    private var itemClickListener: ((message:Message)->Unit)? = null

    inner class MessageViewHolder(val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemMessageBinding.inflate(layoutInflater, parent, false)

        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = currentList[position]
        holder.binding.message = message
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(message)
        }
    }

    override fun onViewRecycled(holder: MessageViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.unbind()
    }

    fun setItemClickListener(listener: (message:Message)->Unit){
        itemClickListener = listener
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemClickListener = null
    }
}