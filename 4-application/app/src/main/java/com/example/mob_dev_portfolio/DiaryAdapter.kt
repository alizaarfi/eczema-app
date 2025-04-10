package com.example.mob_dev_portfolio

import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.databinding.ItemDiaryEntryBinding
import android.view.LayoutInflater
import android.graphics.Color
import android.view.ViewGroup
import com.example.mob_dev_portfolio.data.entity.DiaryEntry
import com.google.android.material.card.MaterialCardView

class DiaryAdapter(private val onEditClick: (DiaryEntry) -> Unit) :
    RecyclerView.Adapter<DiaryAdapter.EntryViewHolder>() {

    private var entries: List<DiaryEntry> = emptyList()

    inner class EntryViewHolder(val binding: ItemDiaryEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: DiaryEntry) {
            binding.tvEntryDate.text = entry.date

            // Show either food or skincare name
            val displayName = when {
                !entry.foodName.isNullOrEmpty() -> "Food: ${entry.foodName}"
                !entry.skincareName.isNullOrEmpty() -> "Product: ${entry.skincareName}"
                else -> "Unknown item"
            }
            binding.textFoodProductName.text = displayName

            // Show reaction severity
            binding.textReactionSeverity.text = "Reaction: ${entry.reactionSeverity}"

            // Highlight severe reactions with colour
            val cardView = binding.root as? MaterialCardView
            when (entry.reactionSeverity) {
                "High" -> {
                    cardView?.strokeColor = Color.RED
                    cardView?.strokeWidth = 4
                    binding.textReactionSeverity.setTextColor(Color.RED)
                }
                "Medium" -> {
                    cardView?.strokeColor = Color.parseColor("#FFA500") // Orange
                    cardView?.strokeWidth = 2
                    binding.textReactionSeverity.setTextColor(Color.parseColor("#FFA500"))
                }
                else -> {
                    cardView?.strokeColor = Color.GREEN
                    cardView?.strokeWidth = 1
                    binding.textReactionSeverity.setTextColor(Color.GREEN)
                }
            }

            binding.buttonEdit.setOnClickListener {
                onEditClick(entry)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiaryEntryBinding.inflate(inflater, parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    fun submitList(newList: List<DiaryEntry>) {
        entries = newList
        notifyDataSetChanged()
    }
}