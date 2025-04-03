package com.example.genni

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.genni.ui.theme.GenniTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.BEViewModel
import com.example.genni.viewmodels.ForgetPasswordViewModel
import com.example.genni.viewmodels.HCViewModel
import com.example.genni.viewmodels.HomeViewModel
import com.example.genni.viewmodels.UserViewModel
import com.example.genni.viewmodels.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GenniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    // * App Navigation Logic *
    val navigationController = rememberNavController()
    val userViewModel = remember { UserViewModel() }
    val authViewModel = remember { AuthViewModel() }
    val workoutViewModel = remember { WorkoutViewModel() } // Creating a single instance of the WorkoutViewModel

    NavHost(navController = navigationController, startDestination = Screens.LoginScreen.screen) {
        composable(Screens.LoginScreen.screen) { LoginScreen(navigationController,authViewModel,userViewModel) }
        composable(Screens.HomeScreen.screen) { HomeScreen(navigationController, HomeViewModel()) }
        composable(Screens.SignUpScreen.screen) { SignUpScreen(navigationController, userViewModel)}
        composable(Screens.ForgetPasswordScreen.screen) { ForgotPasswordScreen(navigationController, ForgetPasswordViewModel())}
        composable(Screens.GeneratedWorkoutScreen.screen) { GeneratedWorkoutScreen(workoutViewModel,navigationController) }
        composable(Screens.WorkoutSimulatorScreen.screen) {
            WorkoutSimulatorScreen(
                viewModel = workoutViewModel,
                onWorkoutCompleted = {
                    navigationController.popBackStack() // Go back to previous page....
                }
            )
        }
        composable(Screens.HealthCalculationsScreen.screen) { HealthCalculationsScreen(HCViewModel())}
        composable(Screens.BreathingExercisesScreen.screen) { BreathingExercisesScreen(navigationController, BEViewModel())}
        composable(Screens.BreathingExercisesSimulatorScreen.screen) { BESimulatorScreen(BEViewModel()) { } }
        composable(Screens.SettingsScreen.screen) { SettingsScreen()}
        composable(Screens.AboutScreen.screen) { AboutScreen()}
    }
}