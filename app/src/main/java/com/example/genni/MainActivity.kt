package com.example.genni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.genni.ui.theme.GenniTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.genni.adminScreens.AdminHomeScreen
import com.example.genni.adminScreens.AdminLoginScreen
import com.example.genni.adminScreens.AdminSignUpScreen
import com.example.genni.viewmodels.AdminViewModel
import com.example.genni.viewmodels.AppSettingsViewModel
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
            GenniTheme { Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { App() } }
        }
    }
}

@Composable
fun App() {
    val navigationController = rememberNavController()

    val userViewModel = remember { UserViewModel() }
    val authViewModel = remember { AuthViewModel() }
    val workoutViewModel = remember { WorkoutViewModel() }
    val appSettingsViewModel = remember { AppSettingsViewModel() }
    val adminViewModel = remember { AdminViewModel() }
    val homeViewModel = remember { HomeViewModel() }
    val hcViewModel = remember { HCViewModel() }
    val fpViewModel = remember { ForgetPasswordViewModel() }
    val beViewModel = remember { BEViewModel() }

    val isDarkTheme = appSettingsViewModel.isDarkTheme

    GenniTheme(darkTheme = isDarkTheme) {
        NavHost(navController = navigationController, startDestination = Screens.LoginScreen.screen) {
            composable(Screens.LoginScreen.screen) { LoginScreen(navigationController, authViewModel, userViewModel) }
            composable(Screens.HomeScreen.screen) { HomeScreen(navigationController, homeViewModel, authViewModel, workoutViewModel) }
            composable(Screens.SignUpScreen.screen) { SignUpScreen(navigationController, userViewModel) }
            composable(Screens.ForgetPasswordScreen.screen) { ForgotPasswordScreen(navigationController,fpViewModel) }
            composable(Screens.GeneratedWorkoutScreen.screen) { GeneratedWorkoutScreen(workoutViewModel,authViewModel, navigationController) }
            composable(Screens.WorkoutSimulatorScreen.screen) { WorkoutSimulatorScreen(workoutViewModel, onWorkoutCompleted = { navigationController.popBackStack() }) }
            composable(Screens.HealthCalculationsScreen.screen) { HealthStatusScreen(navigationController,hcViewModel,authViewModel) }
            composable(Screens.BreathingExercisesScreen.screen) { BreathingExercisesScreen(navigationController, beViewModel) }
            composable(Screens.BreathingExercisesSimulatorScreen.screen) { BESimulatorScreen(beViewModel) { } }
            composable(Screens.SettingsScreen.screen) { SettingsScreen(navigationController, authViewModel, appSettingsViewModel) }
            composable(Screens.AboutScreen.screen) { AboutScreen() }
            composable(Screens.ProfileScreen.screen) { ProfileScreen(authViewModel) }
            composable(Screens.FoodRecommScreen.screen) { FoodRecommScreen(navigationController,hcViewModel,authViewModel) }
            composable(Screens.FPContdScreen.screen) { FPContd(fpViewModel,navigationController) }
            composable(Screens.SavedWorkoutScreen.screen) { SavedWorkoutsScreen(workoutViewModel,authViewModel,navigationController) }

            // Admin's Composables
            composable(Screens.AdminHomeScreen.screen) { AdminHomeScreen() }
            composable(Screens.AdminLoginScreen.screen) { AdminLoginScreen(navigationController, adminViewModel, authViewModel) }
            composable(Screens.AdminSignUpScreen.screen) { AdminSignUpScreen(navigationController,adminViewModel) }
            composable(Screens.HCExplanationScreen.screen) { HealthMetricsExplanationScreen(navigationController) }

        }
    }
}
