package com.example.mob_dev_portfolio

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mob_dev_portfolio.data.entity.DiaryEntry
import com.example.mob_dev_portfolio.databinding.ActivityDiaryEntryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEntryBinding
    private lateinit var viewModel: DiaryViewModel
    private var entryId: Int = 0
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // Check if editing an existing entry
        entryId = intent.getIntExtra("entry_id", 0)
        isEditMode = entryId > 0

        if (isEditMode) {
            title = "Edit Diary Entry"
            // If have time
        } else {
            title = "New Diary Entry"
            binding.editTextDate.setText(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
        }

        binding.buttonSave.setOnClickListener {
            saveEntry()
        }
    }

    private fun saveEntry() {
        val foodName = binding.editTextFoodName.text.toString().trim()
        val skincareName = binding.editTextSkincareName.text.toString().trim()
        val reactionSeverity = binding.spinnerSeverity.selectedItem.toString()
        val date = binding.editTextDate.text.toString()

        // Make sure at least one of food or skincare is provided, but not both
        if (foodName.isEmpty() && skincareName.isEmpty()) {
            Toast.makeText(this, "Please enter either a food or skincare product", Toast.LENGTH_SHORT).show()
            return
        }

        if (foodName.isNotEmpty() && skincareName.isNotEmpty()) {
            Toast.makeText(this, "Please enter either food OR skincare product, not both", Toast.LENGTH_SHORT).show()
            return
        }

        val entry = DiaryEntry(
            id = if (isEditMode) entryId else 0,
            foodName = foodName.takeIf { it.isNotEmpty() },
            skincareName = skincareName.takeIf { it.isNotEmpty() },
            reactionSeverity = reactionSeverity,
            date = date
        )

        if (isEditMode) {
            viewModel.update(entry)
            Toast.makeText(this, "Entry updated", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insert(entry)
            Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}