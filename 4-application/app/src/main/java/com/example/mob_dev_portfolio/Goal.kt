package com.example.mob_dev_portfolio

data class Goal(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    var isChecked: Boolean = false
)
