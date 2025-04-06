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

// Interacts with the view model for the daily log dialog
// Handles UI elements and user interactions
// Contains layout setup, button click handling, and lifecycle management
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

        // Set up the different input buttons
        setupDaySelectors()
        setupPhysicalSensationsButtons()
        setUpMoodSelectors()
        setupBodyAreas()

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

        // Sensation names from string resources
        val sensations = listOf(
            getString(R.string.sensation_itchy),
            getString(R.string.sensation_burning),
            getString(R.string.sensation_dry),
            getString(R.string.sensation_flaky),
            getString(R.string.sensation_inflamed)
        )

        // Set up click listeners for each emoji button
        emojiButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val sensation = sensations[index]
                Toast.makeText(context, "Sensation: $sensation selected", Toast.LENGTH_SHORT).show()

                // Pass to ViewModel
                viewModel.selectPhysicalSensation(sensation)
            }
        }
    }

    private fun setUpMoodSelectors() {
        val moodButtons = listOf(
            binding.btnVerySad,
            binding.btnSad,
            binding.btnNeutral,
            binding.btnHappy,
            binding.btnVeryHappy
        )

        // Mood values using string resources
        val moods = listOf(
            getString(R.string.text_very_sad),
            getString(R.string.text_sad),
            getString(R.string.text_neutral),
            getString(R.string.text_happy),
            getString(R.string.text_very_happy)
        )

        // Set up click listeners for each mood button
        moodButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val mood = moods[index]
                Toast.makeText(context, "Mood: $mood selected", Toast.LENGTH_SHORT).show()
                viewModel.selectMood(mood)
            }
        }
    }

    private fun setupBodyAreas() {
        // Get all body area chips
        val bodyAreaChips = listOf(
            binding.chipFace,
            binding.chipChest,
            binding.chipBack,
            binding.chipTorso,
            binding.chipArmHand,
            binding.chipLegFeet
        )

        // Body area names from string resources
        val bodyAreas = listOf(
            getString(R.string.text_face),
            getString(R.string.text_chest),
            getString(R.string.text_back),
            getString(R.string.text_torso),
            getString(R.string.text_arm_hand),
            getString(R.string.text_leg_feet)
        )

        // Current selected body area for intensity slider
        var currentSelectedArea: String? = null

        // Set up each body area chip's click listener
        bodyAreaChips.forEachIndexed { index, chip ->
            chip.setOnClickListener {
                val bodyArea = bodyAreas[index]
                val isSelected = chip.isChecked

                // Show or hide intensity slider when a body area is selected
                if (isSelected) {
                    // Update currently selected area for intensity adjustment
                    currentSelectedArea = bodyArea

                    // Show intensity slider and update label
                    binding.bodyAreaIntensityLayout.visibility = View.VISIBLE
                    binding.bodyAreaIntensityLabel.text = "${getString(R.string.intensity_label)}: $bodyArea"

                    // Set slider to default or existing value
                    val currentIntensity = viewModel.selectedBodyAreas.value?.get(bodyArea) ?: 1
                    binding.bodyAreaIntensitySlider.value = currentIntensity.toFloat()

                    // Notify the ViewModel
                    viewModel.selectBodyArea(bodyArea, true, currentIntensity)
                } else {
                    // If the current selected area is being unselected, hide intensity slider
                    if (bodyArea == currentSelectedArea) {
                        binding.bodyAreaIntensityLayout.visibility = View.GONE
                        currentSelectedArea = null
                    }

                    // Notify the ViewModel that this area is deselected
                    viewModel.selectBodyArea(bodyArea, false)
                }

                Toast.makeText(
                    context,
                    if (isSelected) "$bodyArea selected" else "$bodyArea deselected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Intensity slider change listener
        binding.bodyAreaIntensitySlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser && currentSelectedArea != null) {
                val intensity = value.toInt()
                viewModel.updateBodyAreaIntensity(currentSelectedArea!!, intensity)
            }
        }

        // Observe selected body areas from ViewModel
        viewModel.selectedBodyAreas.observe(viewLifecycleOwner) { selectedAreas ->
            // Update chips to match the ViewModel state
            bodyAreaChips.forEachIndexed { index, chip ->
                val bodyArea = bodyAreas[index]
                chip.isChecked = selectedAreas.containsKey(bodyArea)
            }
        }
    }

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