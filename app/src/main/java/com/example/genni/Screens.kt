package com.example.genni

sealed class Screens(val screen: String) {
    data object LoginScreen:Screens ("loginscreen")
    data object HomeScreen:Screens ("homescreen")
    data object SignUpScreen:Screens ("signupscreen")
    data object ForgetPasswordScreen:Screens("forgetpasswordscreen")
    data object GeneratedWorkoutScreen:Screens("generatedworkoutscreen")
    data object WorkoutSimulatorScreen:Screens("workoutsimulatorscreen")
    data object HealthCalculationsScreen:Screens("healthcalculationsscreen")
    data object BreathingExercisesScreen:Screens("breathingexercisesscreen")
    data object BreathingExercisesSimulatorScreen:Screens("besimulatorscreen")
    data object SettingsScreen:Screens("settingsscreen")
    data object AboutScreen:Screens("aboutscreen")
    data object ProfileScreen:Screens("profilescreen")
    data object AdminHomeScreen:Screens("adminhomescreen")
    data object AdminLoginScreen:Screens("adminloginscreen")
    data object AdminSignUpScreen:Screens("adminsignupscreen")
    data object HCExplanationScreen:Screens("hcexplanationscreen")
    data object SavedWorkoutScreen:Screens("savedworkoutscreen")
    data object FoodRecommScreen:Screens("foodreccomscreen")
    data object FPContdScreen:Screens("fdcontdscreen")
}