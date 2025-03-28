package com.example.genni

sealed class Screens(val screen: String) {
    data object LoginScreen:Screens ("loginscreen")
    data object HomeScreen:Screens ("homescreen")
    data object SignUpScreen:Screens ("signupscreen")
    data object ForgetPasswordScreen:Screens("forgetpasswordscreen")
    data object GeneratedWorkoutScreen:Screens("generatedworkoutscreen")
    data object WorkoutSimulatorScreen:Screens("workoutsimulatorscreen")
}