package com.example.mob_dev_portfolio

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.mob_dev_portfolio.databinding.ActivityDiaryBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class DiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryBinding
    private lateinit var viewModel: DiaryViewModel
    private lateinit var adapter: DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // Initialise the adapter with navigation so dont need extra helper func
        adapter = DiaryAdapter { entry ->
            val intent = Intent(this, DiaryEntryActivity::class.java)
            intent.putExtra("entry_id", entry.id)
            startActivity(intent)
        }

        binding.diaryEntriesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.diaryEntriesRecyclerView.adapter = adapter

        // Update search logic
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.getAllEntriesFiltered(s.toString()).observe(this@DiaryActivity) {
                    adapter.submitList(it)
                    updateTriggerSection(it)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnAddNewEntry.setOnClickListener {
            startActivity(Intent(this, DiaryEntryActivity::class.java))
        }

        viewModel.allEntries.observe(this) { entries ->
            adapter.submitList(entries)
            updateTriggerSection(entries)
        }

    }

    // Helper function to update the triggers section
    // when severe is logged
    private fun updateTriggerSection(entries: List<com.example.mob_dev_portfolio.data.entity.DiaryEntry>) {
        val severeTriggers = entries.filter { entry ->
            entry.reactionSeverity == "High"
        }.map { entry ->
            when {
                !entry.foodName.isNullOrEmpty() -> "Food: ${entry.foodName}"
                !entry.skincareName.isNullOrEmpty() -> "Product: ${entry.skincareName}"
                else -> null
            }
        }.filterNotNull().distinct()

        if (severeTriggers.isEmpty()) {
            binding.triggersTextView.text = "No high severity triggers found"
        } else {
            binding.triggersTextView.text = severeTriggers.joinToString(", ")
        }
    }

    // Refreshingg
    override fun onResume() {
        super.onResume()
        viewModel.allEntries.observe(this) { entries ->
            adapter.submitList(entries)
            updateTriggerSection(entries)
        }
    }
}