package com.example.mob_dev_portfolio

import androidx.recyclerview.widget.DiffUtil
import com.example.mob_dev_portfolio.Goal


class GoalItemDiffCallback : DiffUtil.ItemCallback<Goal>() {
    override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem.text == newItem.text // Compare based on unique identifier (e.g., text)
    }

    override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem == newItem  // Compare the full content of the goal
    }
}
// DiffUtil: https://www.geeksforgeeks.org/diffutil-in-recyclerview-in-android/