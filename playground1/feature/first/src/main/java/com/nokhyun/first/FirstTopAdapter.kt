package com.nokhyun.first

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nokhyun.first.databinding.ListTopItemBinding

data class Top(
    val text: String
)

class FirstTopAdapter : ListAdapter<Top, FirstTopViewHolder>(object : DiffUtil.ItemCallback<Top>() {
    override fun areItemsTheSame(oldItem: Top, newItem: Top): Boolean {
        return oldItem.text == newItem.text
    }

    override fun areContentsTheSame(oldItem: Top, newItem: Top): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstTopViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FirstTopViewHolder(ListTopItemBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: FirstTopViewHolder, position: Int) {
        holder.binding.item = getItem(position)

        if(position > 0){
            logger { "holder.binding.root: ${holder.binding.root as ConstraintLayout}" }
            holder.binding.tvTop.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(16, 0,0,0)
            }
        }

        holder.binding.executePendingBindings()
    }
}

class FirstTopViewHolder(val binding: ListTopItemBinding) : RecyclerView.ViewHolder(binding.root)