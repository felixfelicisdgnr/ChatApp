package com.doganur.mychatapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doganur.mychatapp.databinding.RecyclerRowBinding
import com.doganur.mychatapp.databinding.RecyclerRowRightBinding
import com.google.firebase.auth.FirebaseAuth

class ChatRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderLeft(private val binding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(chatData: ChatData){
                binding.chatRecyclerTextView.text = chatData.text
            }
    }
    inner class ViewHolderRight(private val bindingTwo: RecyclerRowRightBinding) :
        RecyclerView.ViewHolder(bindingTwo.root) {

            fun bind(chatData: ChatData){
                bindingTwo.chatRecyclerRightTextView.text = chatData.text
            }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ChatData>() {
        override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var chats : List<ChatData>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {

        val chat = chats[position]

        return if (chat.user == FirebaseAuth.getInstance().currentUser?.email.toString()) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderLeft(binding)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                val binding = RecyclerRowRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderRight(binding)
            }
            else -> throw java.lang.IllegalArgumentException("blabla")
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chats[position]
            when (holder) {
            is ViewHolderLeft -> holder.bind(chat)
            is ViewHolderRight -> holder.bind(chat)
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT= 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }
}
