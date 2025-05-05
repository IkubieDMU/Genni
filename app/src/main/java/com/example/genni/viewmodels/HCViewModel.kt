package com.example.genni.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

class HCViewModel : ViewModel() {

    private val _weightInput = MutableStateFlow("")
    val weightInput: StateFlow<String> = _weightInput.asStateFlow()

    private val _heightInput = MutableStateFlow("")
    val heightInput: StateFlow<String> = _heightInput.asStateFlow()

    private val _muscleMassPercentageInput = MutableStateFlow("")
    val muscleMassPercentageInput: StateFlow<String> = _muscleMassPercentageInput.asStateFlow()

    private val _ageInput = MutableStateFlow("")
    val ageInput: StateFlow<String> = _ageInput.asStateFlow()

    private val _genderInput = MutableStateFlow("Male")
    val genderInput: StateFlow<String> = _genderInput.asStateFlow()

    private val _activityLevelInput = MutableStateFlow("1.2")
    val activityLevelInput: StateFlow<String> = _activityLevelInput.asStateFlow()

    private val _bmi = MutableStateFlow<Double?>(null)
    val bmi: StateFlow<Double?> = _bmi.asStateFlow()

    private val _ffmi = MutableStateFlow<Double?>(null)
    val ffmi: StateFlow<Double?> = _ffmi.asStateFlow()

    private val _muscleMass = MutableStateFlow<Double?>(null)
    val muscleMass: StateFlow<Double?> = _muscleMass.asStateFlow()

    private val _bmr = MutableStateFlow<Double?>(null)
    val bmr: StateFlow<Double?> = _bmr.asStateFlow()

    private val _bfp = MutableStateFlow<Double?>(null)
    val bfp: StateFlow<Double?> = _bfp.asStateFlow()

    private val _tdee = MutableStateFlow<Double?>(null)
    val tdee: StateFlow<Double?> = _tdee.asStateFlow()

    private val _proteinIntake = MutableStateFlow<Pair<Double, Double>?>(null)
    val proteinIntake: StateFlow<Pair<Double, Double>?> = _proteinIntake.asStateFlow()

    private val _waterIntake = MutableStateFlow<Double?>(null)
    val waterIntake: StateFlow<Double?> = _waterIntake.asStateFlow()

    private val _calculated = MutableStateFlow(false)
    val calculated: StateFlow<Boolean> = _calculated.asStateFlow()


    fun onWeightChange(newWeight: String) { _weightInput.value = newWeight }
    fun onHeightChange(newHeight: String) { _heightInput.value = newHeight }
    fun onMuscleMassPercentageChange(newPercentage: String) { _muscleMassPercentageInput.value = newPercentage }
    fun onAgeChange(newAge: String) { _ageInput.value = newAge }
    fun onGenderChange(newGender: String) { _genderInput.value = newGender }
    fun onActivityLevelChange(newLevel: String) { _activityLevelInput.value = newLevel }

    fun calculate() {
        if (_calculated.value) return // Already calculated

        val weight = weightInput.value.toDoubleOrNull()
        val height = heightInput.value.toDoubleOrNull()?.div(100) // Convert cm to meters
        val age = ageInput.value.toIntOrNull()
        val muscleMassPercentage = muscleMassPercentageInput.value.toDoubleOrNull()?.div(100)
        val activityLevel = activityLevelInput.value.toDoubleOrNull()

        // Exit early if any required value is missing
        if (weight == null || height == null || age == null || activityLevel == null) return

        // BMI
        val bmiValue = weight / height.pow(2)
        _bmi.value = bmiValue

        // BMR
        val bmrValue = if (genderInput.value == "Male") {
            88.362 + (13.397 * weight) + (4.799 * height * 100) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height * 100) - (4.330 * age)
        }
        _bmr.value = bmrValue

        // BFP
        val bfpValue = if (genderInput.value == "Male") {
            1.20 * bmiValue + 0.23 * age - 16.2
        } else {
            1.20 * bmiValue + 0.23 * age - 5.4
        }
        _bfp.value = bfpValue

        // TDEE
        _tdee.value = bmrValue * activityLevel

        // Muscle Mass (optional)
        muscleMassPercentage?.let { _muscleMass.value = weight * it }

        // Protein Intake Range (1.6g - 2.2g per kg)
        _proteinIntake.value = Pair(weight * 1.6, weight * 2.2)

        // Water Intake (0.033L per kg)
        _waterIntake.value = weight * 0.033

        _calculated.value = true // Mark as done
    }
}