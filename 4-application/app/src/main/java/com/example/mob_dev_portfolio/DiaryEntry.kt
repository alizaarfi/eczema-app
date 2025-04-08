package com.example.mob_dev_portfolio


// This is for creating each entry using the adapter
data class DiaryEntry(
    val id: Long = System.currentTimeMillis(),
    val date: Long, // Timestamp
    val title: String = "",
    val content: String,
    val mood: String? = null // Optional mood tag
)