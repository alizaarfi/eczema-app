package com.example.mob_dev_portfolio

import android.icu.text.SimpleDateFormat
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.databinding.FragmentDailyLogDialogBinding
import java.util.Date
import java.util.Locale
import android.graphics.Typeface
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

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
        val editTextDate : EditText = binding.editTextDate

        // Set the current date in the EditText
        editTextDate.setText(getFormattedDate())

        // Set up close button
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // Set up the different input buttons
        setupDaySelectors()
        // further date picker stuff
        setupWeekNavigation()
        setupPhysicalSensationsRecyclerView()
        setupMoodRecyclerView()
        setupBodyAreas()
        setupTreatmentRecyclerView()
        setupEnvironmentRecyclerView()
        setupDietHydrationRecyclerView()
        setupExerciseSleepRecyclerView()

        // Set up save button
        binding.saveButton.setOnClickListener {
            saveLogEntry()
            Toast.makeText(context, getString(R.string.log_saved), Toast.LENGTH_SHORT).show()
            dismiss()
        }

        // Observe ViewModel data
        observeViewModel()
    }

    private fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        // Change to format so its like 8 July
        val currentDateWithoutYear = SimpleDateFormat("d MMMM", Locale.getDefault()).format(Date())

        // Check if today's date
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return if (currentDate == todayDate) {
            "Today: $currentDateWithoutYear"
        } else {
            currentDateWithoutYear
        }
    }

    private fun setupWeekNavigation() {
        // Add these buttons to your layout or create them programmatically
        binding.btnPreviousWeek.setOnClickListener {
            viewModel.goToPreviousWeek()
        }

        binding.btnNextWeek.setOnClickListener {
            viewModel.goToNextWeek()
        }

        // Update week indicator text
        viewModel.currentWeekOffset.observe(viewLifecycleOwner) { offset ->

            // Only enable next week button if we're not in the current week
            binding.btnNextWeek.isEnabled = offset < 0
        }
    }

    private fun setupDaySelectors() {
        // Get day buttons
        val dayButtons = listOf(
            binding.btnMonday,
            binding.btnTuesday,
            binding.btnWednesday,
            binding.btnThursday,
            binding.btnFriday,
            binding.btnSaturday,
            binding.btnSunday
        )

        // Set click listeners
        dayButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.selectDay(index)
                updateDayButtonStyles(index)
            }
        }

        // Observe the current selected day
        viewModel.selectedDay.observe(viewLifecycleOwner) { day ->
            updateDayButtonStyles(day)
        }

        // Observe today's day of week to highlight it
        viewModel.todayDayOfWeek.observe(viewLifecycleOwner) { todayDayOfWeek ->
            // Update visual indicator for today
            dayButtons.forEachIndexed { index, button ->
                if (index == todayDayOfWeek) {
                    // Add a special indicator for today
                    button.setTypeface(null, Typeface.BOLD)
                    // Could also add an underline or other visual cue
                } else {
                    button.setTypeface(null, Typeface.NORMAL)
                }
            }
        }

        // Observe current week offset to adjust today highlight
        viewModel.currentWeekOffset.observe(viewLifecycleOwner) { offset ->
            // Only show "today" indicator in current week
            val showTodayIndicator = offset == 0
            val todayDayOfWeek = viewModel.todayDayOfWeek.value ?: -1

            if (showTodayIndicator && todayDayOfWeek >= 0) {
                dayButtons[todayDayOfWeek].setTypeface(null, Typeface.BOLD)
            } else {
                dayButtons.forEach { it.setTypeface(null, Typeface.NORMAL) }
            }
        }

        // Observe the current date display
        viewModel.currentDateDisplay.observe(viewLifecycleOwner) { dateText ->
            binding.editTextDate.setText(dateText)
        }
    }

    private fun updateDayButtonStyles(selectedDayIndex: Int) {
        // Get day buttons
        val dayButtons = listOf(
            binding.btnMonday,
            binding.btnTuesday,
            binding.btnWednesday,
            binding.btnThursday,
            binding.btnFriday,
            binding.btnSaturday,
            binding.btnSunday
        )

        // Update the checked state for all buttons
        dayButtons.forEachIndexed { index, button ->
            button.isChecked = index == selectedDayIndex
        }
    }

    private fun setupPhysicalSensationsRecyclerView() {
        // Create list of sensation emoji buttons
        val sensationButtons = listOf(
            EmojiButton(
                "itchy",
                getString(R.string.emoji_itchy),
                getString(R.string.sensation_itchy)
            ),
            EmojiButton(
                "burning",
                getString(R.string.emoji_burning),
                getString(R.string.sensation_burning)
            ),
            EmojiButton(
                "dry",
                getString(R.string.emoji_dry),
                getString(R.string.sensation_dry)
            ),
            EmojiButton(
                "flaky",
                getString(R.string.emoji_flaky),
                getString(R.string.sensation_flaky)
            ),
            EmojiButton(
                "inflamed",
                getString(R.string.emoji_inflamed),
                getString(R.string.sensation_inflamed)
            )
        )

        // Create RecyclerView for physical sensations
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiButtonAdapter(sensationButtons) { button ->
                Toast.makeText(
                    context,
                    getString(R.string.sensation_selected_format, button.label),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.selectPhysicalSensation(button.label)
            }
        }

        // Add RecyclerView to the container
        binding.emojiScrollView.removeAllViews()
        binding.emojiScrollView.addView(recyclerView)
    }

    private fun setupMoodRecyclerView() {
        // Create list of mood emoji buttons
        val moodButtons = listOf(
            EmojiButton(
                "very_sad",
                getString(R.string.emoji_very_sad),
                getString(R.string.text_very_sad)
            ),
            EmojiButton(
                "sad",
                getString(R.string.emoji_sad),
                getString(R.string.text_sad)
            ),
            EmojiButton(
                "neutral",
                getString(R.string.emoji_neutral),
                getString(R.string.text_neutral)
            ),
            EmojiButton(
                "happy",
                getString(R.string.emoji_happy),
                getString(R.string.text_happy)
            ),
            EmojiButton(
                "very_happy",
                getString(R.string.emoji_very_happy),
                getString(R.string.text_very_happy)
            )
        )

        // Create RecyclerView for mood selection
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiButtonAdapter(moodButtons) { button ->
                Toast.makeText(
                    context,
                    getString(R.string.mood_selected_format, button.label),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.selectMood(button.label)
            }
        }

        // Add RecyclerView to the container
        binding.moodEmojiScrollView.removeAllViews()
        binding.moodEmojiScrollView.addView(recyclerView)
    }


    private fun setupTreatmentRecyclerView() {
        // Create list of treatment emoji buttons
        val treatmentButtons = listOf(
            EmojiButton(
                "bath",
                getString(R.string.emoji_bath),
                getString(R.string.label_bath)
            ),
            EmojiButton(
                "moisturiser",
                getString(R.string.emoji_moisturiser),
                getString(R.string.label_moisturiser)
            ),
            EmojiButton(
                "non_steroid",
                getString(R.string.emoji_non_steroid),
                getString(R.string.label_non_steroid)
            ),
            EmojiButton(
                "steroids",
                getString(R.string.emoji_steroids),
                getString(R.string.label_steroids)
            ),
            EmojiButton(
                "other_treatment",
                getString(R.string.emoji_other_treatment),
                getString(R.string.label_other_treatment)
            )
        )

        // Create RecyclerView for treatments
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiButtonAdapter(treatmentButtons) { button ->
                Toast.makeText(
                    context,
                    getString(R.string.treatment_selected_format, button.label),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.selectTreatment(button.label)
            }
        }

        // Add RecyclerView to the container
        binding.treatmentEmojiScrollView.removeAllViews()
        binding.treatmentEmojiScrollView.addView(recyclerView)
    }

    private fun setupEnvironmentRecyclerView() {
        // Create list of environment emoji buttons
        val environmentButtons = listOf(
            EmojiButton(
                "cold",
                getString(R.string.emoji_cold),
                getString(R.string.label_cold)
            ),
            EmojiButton(
                "hot",
                getString(R.string.emoji_hot),
                getString(R.string.label_hot)
            ),
            EmojiButton(
                "moderate",
                getString(R.string.emoji_moderate),
                getString(R.string.label_moderate)
            ),
            EmojiButton(
                "humid",
                getString(R.string.emoji_humid),
                getString(R.string.label_humid)
            ),
            EmojiButton(
                "pollen",
                getString(R.string.emoji_pollen),
                getString(R.string.label_pollen)
            ),
            EmojiButton(
                "chlorine",
                getString(R.string.emoji_chlorine),
                getString(R.string.label_chlorine)
            ),
            EmojiButton(
                "dust",
                getString(R.string.emoji_dust),
                getString(R.string.label_dust)
            )
        )

        // Create RecyclerView for environment
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiButtonAdapter(environmentButtons) { button ->
                Toast.makeText(
                    context,
                    getString(R.string.environment_selected_format, button.label),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.selectEnvironment(button.label)
            }
        }

        // Add RecyclerView to the container
        binding.environmentScrollView.removeAllViews()
        binding.environmentScrollView.addView(recyclerView)
    }

    private fun setupDietHydrationRecyclerView() {
        // Create list of diet/hydration emoji buttons
        val dietButtons = listOf(
            EmojiButton(
                "hydrated",
                getString(R.string.emoji_hydrated),
                getString(R.string.label_hydrated)
            ),
            EmojiButton(
                "alcohol",
                getString(R.string.emoji_alcohol),
                getString(R.string.label_alcohol)
            ),
            EmojiButton(
                "five_a_day",
                getString(R.string.emoji_5_a_day),
                getString(R.string.label_5_a_day)
            ),
            EmojiButton(
                "allergy_trigger",
                getString(R.string.emoji_allergy_trigger),
                getString(R.string.label_allergy_trigger)
            )
        )

        // Create RecyclerView for diet/hydration
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiButtonAdapter(dietButtons) { button ->
                Toast.makeText(
                    context,
                    getString(R.string.diet_selected_format, button.label),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.selectDietHydration(button.label)
            }
        }

        // Add RecyclerView to the container
        binding.dietHydrationScrollView.removeAllViews()
        binding.dietHydrationScrollView.addView(recyclerView)
    }

    private fun setupExerciseSleepRecyclerView() {
        // Create list of exercise/sleep emoji buttons
        val exerciseButtons = listOf(
            EmojiButton(
                "exercise",
                getString(R.string.emoji_exercise),
                getString(R.string.label_exercise)
            ),
            EmojiButton(
                "sleep",
                getString(R.string.emoji_sleep),
                getString(R.string.label_sleep)
            )
        )

        // Create RecyclerView for exercise/sleep
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiButtonAdapter(exerciseButtons) { button ->
                Toast.makeText(
                    context,
                    getString(R.string.exercise_selected_format, button.label),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.selectExerciseSleep(button.label)
            }
        }

        // Add RecyclerView to the container
        binding.exerciseSleepScrollView.removeAllViews()
        binding.exerciseSleepScrollView.addView(recyclerView)
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
                    if (isSelected)
                        getString(R.string.body_area_selected_format, bodyArea)
                    else
                        getString(R.string.body_area_deselected_format, bodyArea),
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
        // Selected day observation
        viewModel.selectedDay.observe(viewLifecycleOwner) { selectedDay ->
            updateDayButtonStyles(selectedDay)
        }

        // We also need to observe other data for the current day
        viewModel.selectedPhysicalSensations.observe(viewLifecycleOwner) { /* Update UI if needed */ }
        viewModel.selectedMood.observe(viewLifecycleOwner) { /* Update UI if needed */ }
        viewModel.selectedBodyAreas.observe(viewLifecycleOwner) { /* Already handled in setupBodyAreas */ }
        viewModel.selectedTreatments.observe(viewLifecycleOwner) { /* Update UI if needed */ }
        viewModel.selectedEnvironments.observe(viewLifecycleOwner) { /* Update UI if needed */ }
        viewModel.selectedDietHydration.observe(viewLifecycleOwner) { /* Update UI if needed */ }
        viewModel.selectedExerciseSleep.observe(viewLifecycleOwner) { /* Update UI if needed */ }
    }

    private fun saveLogEntry() {
        // Collect data from ViewModel
        val physicalSensations = viewModel.selectedPhysicalSensations.value?.joinToString(", ") ?: ""
        val mood = viewModel.selectedMood.value ?: ""
        val treatments = viewModel.selectedTreatments.value?.joinToString(", ") ?: ""
        val environments = viewModel.selectedEnvironments.value?.joinToString(", ") ?: ""
        val dietHydration = viewModel.selectedDietHydration.value?.joinToString(", ") ?: ""
        val exerciseSleep = viewModel.selectedExerciseSleep.value?.joinToString(", ") ?: ""

        // Create a complete log entry
        viewModel.saveLogEntry(
            physicalSensations,
            mood,
            treatments,
            environments,
            dietHydration,
            exerciseSleep
        )
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