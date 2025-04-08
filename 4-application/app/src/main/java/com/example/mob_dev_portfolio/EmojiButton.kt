package com.example.mob_dev_portfolio

// Made a data class for every button to wrap the emoji and label
// For better performance
data class EmojiButton(
    val id: String,
    val emoji: String,
    val label: String
)