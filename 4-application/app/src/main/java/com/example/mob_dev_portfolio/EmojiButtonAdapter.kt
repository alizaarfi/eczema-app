package com.example.mob_dev_portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.databinding.ItemEmojiButtonBinding

class EmojiButtonAdapter(
    private val buttons: List<EmojiButton>,
    private val onItemClick: (EmojiButton) -> Unit
) : RecyclerView.Adapter<EmojiButtonAdapter.EmojiButtonViewHolder>() {

    inner class EmojiButtonViewHolder(
        private val binding: ItemEmojiButtonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(button: EmojiButton) {
            binding.emojiText.text = button.emoji
            binding.labelText.text = button.label
            binding.root.setOnClickListener { onItemClick(button) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiButtonViewHolder {
        val binding = ItemEmojiButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmojiButtonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmojiButtonViewHolder, position: Int) {
        holder.bind(buttons[position])
    }

    override fun getItemCount() = buttons.size
}