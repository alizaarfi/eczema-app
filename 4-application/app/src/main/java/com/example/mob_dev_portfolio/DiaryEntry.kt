package com.example.mob_dev_portfolio

// This is for creating each entry using the adapter
data class DiaryEntry(
    val id: Long = System.currentTimeMillis(),
    val date: Long,
    val title: String = "",
    val content: String,
    val mood: String? = null // Could include if have time for more customisation
)