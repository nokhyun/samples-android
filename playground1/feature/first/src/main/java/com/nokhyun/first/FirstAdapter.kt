package com.nokhyun.first

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nokhyun.first.databinding.ListItemFirstBinding

class FirstAdapter(
   private val onItemClick: (iv: View, transitionName: String) -> Unit
) : RecyclerView.Adapter<FirstAdapterViewHolder>() {
    private val firstList: List<Int> = mutableListOf<Int>().apply {
        repeat(100) {
            add(it + 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstAdapterViewHolder {
        return FirstAdapterViewHolder(ListItemFirstBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = firstList.size

    override fun onBindViewHolder(holder: FirstAdapterViewHolder, position: Int) {
        holder.binding.value = firstList[position]
        holder.binding.transitionName = holder.binding.root.context.resources.getString(R.string.imageTransition, position.toString())
        holder.binding.executePendingBindings()

        holder.binding.root.setOnClickListener {
            onItemClick(holder.binding.ivArrow, holder.binding.ivArrow.transitionName)
        }
    }
}

class FirstAdapterViewHolder(val binding: ListItemFirstBinding) : RecyclerView.ViewHolder(binding.root)