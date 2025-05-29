package com.example.genni.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

class HCViewModel : ViewModel() {

    // MutableStateFlow to hold the user's weight input as a String.
    private val _weightInput = MutableStateFlow("")
    // Publicly exposed StateFlow for observing changes to the weight input from the UI.
    val weightInput: StateFlow<String> = _weightInput.asStateFlow()

    // MutableStateFlow to hold the user's height input as a String.
    private val _heightInput = MutableStateFlow("")
    // Publicly exposed StateFlow for observing changes to the height input from the UI.
    val heightInput: StateFlow<String> = _heightInput.asStateFlow()

    // MutableStateFlow to hold the user's muscle mass percentage input as a String.
    private val _muscleMassPercentageInput = MutableStateFlow("")
    // Publicly exposed StateFlow for observing changes to the muscle mass percentage input from the UI.
    val muscleMassPercentageInput: StateFlow<String> = _muscleMassPercentageInput.asStateFlow()

    // MutableStateFlow to hold the user's age input as a String.
    private val _ageInput = MutableStateFlow("")
    // Publicly exposed StateFlow for observing changes to the age input from the UI.
    val ageInput: StateFlow<String> = _ageInput.asStateFlow()

    // MutableStateFlow to hold the user's selected gender, defaulting to "Male".
    private val _genderInput = MutableStateFlow("Male")
    // Publicly exposed StateFlow for observing changes to the gender input from the UI.
    val genderInput: StateFlow<String> = _genderInput.asStateFlow()

    // MutableStateFlow to hold the user's selected activity level as a String, defaulting to "1.2".
    // This value will be used as a multiplier in TDEE calculation.
    private val _activityLevelInput = MutableStateFlow("1.2")
    // Publicly exposed StateFlow for observing changes to the activity level input from the UI.
    val activityLevelInput: StateFlow<String> = _activityLevelInput.asStateFlow()

    // MutableStateFlow to store the calculated Body Mass Index (BMI). It's nullable as it's not calculated initially.
    private val _bmi = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the calculated BMI.
    val bmi: StateFlow<Double?> = _bmi.asStateFlow()

    // MutableStateFlow to store the calculated Fat-Free Mass Index (FFMI). It's nullable.
    // Note: FFMI is declared but not calculated in the provided `calculate()` function.
    private val _ffmi = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the calculated FFMI.
    val ffmi: StateFlow<Double?> = _ffmi.asStateFlow()

    // MutableStateFlow to store the calculated muscle mass in kilograms. It's nullable.
    private val _muscleMass = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the calculated muscle mass.
    val muscleMass: StateFlow<Double?> = _muscleMass.asStateFlow()

    // MutableStateFlow to store the calculated Basal Metabolic Rate (BMR). It's nullable.
    private val _bmr = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the calculated BMR.
    val bmr: StateFlow<Double?> = _bmr.asStateFlow()

    // MutableStateFlow to store the calculated Body Fat Percentage (BFP). It's nullable.
    private val _bfp = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the calculated BFP.
    val bfp: StateFlow<Double?> = _bfp.asStateFlow()

    // MutableStateFlow to store the calculated Total Daily Energy Expenditure (TDEE). It's nullable.
    private val _tdee = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the calculated TDEE.
    val tdee: StateFlow<Double?> = _tdee.asStateFlow()

    // MutableStateFlow to store the recommended protein intake range as a Pair (min, max) in grams. It's nullable.
    private val _proteinIntake = MutableStateFlow<Pair<Double, Double>?>(null)
    // Publicly exposed StateFlow for observing the recommended protein intake.
    val proteinIntake: StateFlow<Pair<Double, Double>?> = _proteinIntake.asStateFlow()

    // MutableStateFlow to store the recommended water intake in liters. It's nullable.
    private val _waterIntake = MutableStateFlow<Double?>(null)
    // Publicly exposed StateFlow for observing the recommended water intake.
    val waterIntake: StateFlow<Double?> = _waterIntake.asStateFlow()

    // MutableStateFlow to indicate whether the calculations have been performed.
    private val _calculated = MutableStateFlow(false)
    // Publicly exposed StateFlow for observing the calculation status.
    val calculated: StateFlow<Boolean> = _calculated.asStateFlow()

    /**
     * Updates the [_weightInput] with the new value from the UI.
     * @param newWeight The new weight string.
     */
    fun onWeightChange(newWeight: String) { _weightInput.value = newWeight }

    /**
     * Updates the [_heightInput] with the new value from the UI.
     * @param newHeight The new height string.
     */
    fun onHeightChange(newHeight: String) { _heightInput.value = newHeight }

    /**
     * Updates the [_muscleMassPercentageInput] with the new value from the UI.
     * @param newPercentage The new muscle mass percentage string.
     */
    fun onMuscleMassPercentageChange(newPercentage: String) { _muscleMassPercentageInput.value = newPercentage }

    /**
     * Updates the [_ageInput] with the new value from the UI.
     * @param newAge The new age string.
     */
    fun onAgeChange(newAge: String) { _ageInput.value = newAge }

    /**
     * Updates the [_genderInput] with the new value from the UI.
     * @param newGender The new gender string.
     */
    fun onGenderChange(newGender: String) { _genderInput.value = newGender }

    /**
     * Updates the [_activityLevelInput] with the new value from the UI.
     * @param newLevel The new activity level string.
     */
    fun onActivityLevelChange(newLevel: String) { _activityLevelInput.value = newLevel }

    /**
     * Performs various health-related calculations (BMI, BMR, BFP, TDEE, Muscle Mass, Protein Intake, Water Intake)
     * based on the current input values.
     * It parses string inputs to numeric types and updates the corresponding result StateFlows.
     * Calculations are only performed if they haven't been done already in the current session.
     */
    fun calculate() {
        // If calculations have already been performed, exit to prevent recalculation.
        if (_calculated.value) return

        // Attempt to convert input strings to their respective numeric types.
        // Height is converted from centimeters to meters by dividing by 100.
        // Muscle mass percentage is converted to a decimal by dividing by 100.
        val weight = weightInput.value.toDoubleOrNull()
        val height = heightInput.value.toDoubleOrNull()?.div(100) // Convert cm to meters
        val age = ageInput.value.toIntOrNull()
        val muscleMassPercentage = muscleMassPercentageInput.value.toDoubleOrNull()?.div(100)
        val activityLevel = activityLevelInput.value.toDoubleOrNull()

        // Exit early if any essential required input value is null (i.e., not a valid number).
        if (weight == null || height == null || age == null || activityLevel == null) return

        // --- BMI (Body Mass Index) Calculation ---
        // Formula: weight (kg) / (height (m))^2
        val bmiValue = weight / height.pow(2)
        _bmi.value = bmiValue

        // --- BMR (Basal Metabolic Rate) Calculation ---
        // Uses the Mifflin-St Jeor Equation, which varies by gender.
        val bmrValue = if (genderInput.value == "Male") {
            // Male BMR formula
            88.362 + (13.397 * weight) + (4.799 * height * 100) - (5.677 * age)
        } else {
            // Female BMR formula
            447.593 + (9.247 * weight) + (3.098 * height * 100) - (4.330 * age)
        }
        _bmr.value = bmrValue

        // --- BFP (Body Fat Percentage) Calculation ---
        // Uses a simplified formula incorporating BMI and age, which varies by gender.
        val bfpValue = if (genderInput.value == "Male") {
            // Male BFP formula
            1.20 * bmiValue + 0.23 * age - 16.2
        } else {
            // Female BFP formula
            1.20 * bmiValue + 0.23 * age - 5.4
        }
        _bfp.value = bfpValue

        // --- TDEE (Total Daily Energy Expenditure) Calculation ---
        // Formula: BMR * Activity Level Multiplier
        _tdee.value = bmrValue * activityLevel

        // --- Muscle Mass Calculation (Optional) ---
        // Only calculates if a valid muscleMassPercentage was provided.
        // Formula: weight (kg) * muscle mass percentage (as decimal)
        muscleMassPercentage?.let { _muscleMass.value = weight * it }

        // --- Protein Intake Range Recommendation ---
        // Recommends a range of 1.6g to 2.2g of protein per kg of body weight.
        _proteinIntake.value = Pair(weight * 1.6, weight * 2.2)

        // --- Water Intake Recommendation ---
        // Recommends 0.033 liters of water per kg of body weight.
        _waterIntake.value = weight * 0.033

        // Set the calculated flag to true to indicate that calculations have been completed.
        _calculated.value = true
    }
}