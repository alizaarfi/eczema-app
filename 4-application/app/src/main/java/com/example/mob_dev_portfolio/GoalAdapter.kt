package com.example.mob_dev_portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.databinding.ItemGoalBinding
import com.example.mob_dev_portfolio.Goal

class GoalAdapter(
    private val onCheckedChange: (Goal) -> Unit
) : ListAdapter<Goal, GoalAdapter.GoalViewHolder>(GoalItemDiffCallback()) {

    inner class GoalViewHolder(private val binding: ItemGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val goal = getItem(position)
            binding.tvGoalText.text = goal.text
            binding.cbGoal.isChecked = goal.isChecked

            binding.cbGoal.setOnCheckedChangeListener { _, isChecked ->
                val updatedGoal = goal.copy(isChecked = isChecked)
                onCheckedChange(updatedGoal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(position)
    }

}

