package com.example.mob_dev_portfolio

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.mob_dev_portfolio.databinding.FragmentDailyLogDialogBinding

class DailyLog_Dialog_Fragment : DialogFragment() {

    companion object {
        const val TAG = "DailyLogDialogFragment"
        fun newInstance() = DailyLog_Dialog_Fragment()
    }

    private val viewModel: DailyLog_Dialog_ViewModel by viewModels()
    private var _binding: FragmentDailyLogDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyLogDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up close button
        binding.btnClose.setOnClickListener {
            dismiss()
        }


        // Set up day selector buttons
        setupDaySelectors()

        // Set up physical sensations emoji buttons
        setupPhysicalSensationsButtons()

        // Observe ViewModel data
        observeViewModel()
    }

    private fun setupDaySelectors() {
        binding.btnMonday.setOnClickListener { viewModel.toggleDay(0) }
        binding.btnTuesday.setOnClickListener { viewModel.toggleDay(1) }
        binding.btnWednesday.setOnClickListener { viewModel.toggleDay(2) }
        binding.btnThursday.setOnClickListener { viewModel.toggleDay(3) }
        binding.btnFriday.setOnClickListener { viewModel.toggleDay(4) }
        binding.btnSaturday.setOnClickListener { viewModel.toggleDay(5) }
        binding.btnSunday.setOnClickListener { viewModel.toggleDay(6) }
    }

    private fun setupPhysicalSensationsButtons() {
        // List of emoji buttons to set up
        val emojiButtons = listOf(
            binding.btnItchy,
            binding.btnBurning,
            binding.btnDry,
            binding.btnFlaky,
            binding.btnInflamed
        )

        // Emojis and their descriptions
        val sensations = listOf(
            "Itchy" to getString(R.string.emoji_itchy),
            "Burning" to "🔥",
            "Dry" to "💧",
            "Flaky" to "🍃",
            "Inflamed" to "😖"
        )

        // Set up click listeners for each emoji button
        emojiButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // Toggle selection (you can modify this logic as needed)
                val sensation = sensations[index].first
                Toast.makeText(context, "$sensation sensation selected", Toast.LENGTH_SHORT).show()

                // Add your selection logic here
                viewModel.selectPhysicalSensation(sensation)
            }
        }
    }

//    private fun setUpMoodSelectors() {
//        val emojiButtons = listOf(
//            binding.btnVerySad,
//            binding.btnSad,
//            binding.btnNeutral,
//            binding.btnHappy,
//            binding.btnVeryHappy
//        )
//
//        // Emojis and their descriptions
//        val sensations = listOf(
//            "Very Sad" to "android:text=\"\uD83D\uDE2D\"",
//            "Sad" to "🔥",
//            "Neutral" to "💧",
//            "Happy" to "🍃",
//            "Very Happy" to "😖"
//        )
//
//        // Set up click listeners for each emoji button
//        emojiButtons.forEachIndexed { index, button ->
//            button.setOnClickListener {
//                // Toggle selection (you can modify this logic as needed)
//                val sensation = sensations[index].first
//                Toast.makeText(context, "$sensation sensation selected", Toast.LENGTH_SHORT).show()
//
//                // Add your selection logic here
//                viewModel.selectPhysicalSensation(sensation)
//            }
//        }
//    }

    private fun observeViewModel() {
        // Day selector observation
        viewModel.selectedDays.observe(viewLifecycleOwner) { selectedDays ->
            binding.btnMonday.isChecked = selectedDays.contains(0)
            binding.btnTuesday.isChecked = selectedDays.contains(1)
            binding.btnWednesday.isChecked = selectedDays.contains(2)
            binding.btnThursday.isChecked = selectedDays.contains(3)
            binding.btnFriday.isChecked = selectedDays.contains(4)
            binding.btnSaturday.isChecked = selectedDays.contains(5)
            binding.btnSunday.isChecked = selectedDays.contains(6)
        }
    }

    private fun saveLogEntry() {
        // Update to match new layout and emoji selections
        val physicalSensations = viewModel.selectedPhysicalSensations.value?.joinToString(", ") ?: ""
//        val mood = binding.moodSeekBar.progress

//        // Pass data to ViewModel to save
//        viewModel.saveLogEntry(
//            physicalSensations,
//            mood,
//            treatment,
//            environment,
//            diet,
//            other
//        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}