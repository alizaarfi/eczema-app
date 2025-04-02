package com.example.mob_dev_portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.databinding.ItemGoalBinding

class GoalAdapter(private val goals: MutableList<Goal>) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    data class Goal(val text: String, var isChecked: Boolean = false)

    inner class GoalViewHolder(private val binding: ItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: Goal) {
            binding.tvGoalText.text = goal.text
            binding.cbGoal.isChecked = goal.isChecked

            binding.cbGoal.setOnCheckedChangeListener { _, isChecked ->
                goal.isChecked = isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(goals[position])
    }

    override fun getItemCount(): Int = goals.size
}
