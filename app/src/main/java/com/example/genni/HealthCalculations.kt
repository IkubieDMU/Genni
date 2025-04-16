package com.example.genni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.mintGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.HCViewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthCalculationsScreen(nc: NavController, hcViewModel: HCViewModel, authViewModel: AuthViewModel, isMale: Boolean = true) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val bmi by hcViewModel.bmi.collectAsState()
    val bmr by hcViewModel.bmr.collectAsState()
    val bfp by hcViewModel.bfp.collectAsState()
    val tdee by hcViewModel.tdee.collectAsState()
    val proteinIntake by hcViewModel.proteinIntake.collectAsState()
    val waterIntake by hcViewModel.waterIntake.collectAsState()

    // When the screen loads, use user data to calculate values
        currentUser?.let { user ->
            if (!hcViewModel.calculated.value) {
                hcViewModel.onWeightChange(user.weight.toString())
                hcViewModel.onHeightChange(user.height.toString())
                hcViewModel.onAgeChange(user.age.toString())
                hcViewModel.calculate()
            }
        }

    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328)))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(R.drawable.healthicon),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Health Calculations",
            color = white,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        currentUser?.let { user ->
            InfoRow("Weight", "${user.weight} kg")
            InfoRow("Height", "${user.height} cm")
            InfoRow("Age", "${user.age}")
        } ?: run {
            Text("No user data available", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = white.copy(alpha = 0.3f))

        // Results Section
        bmi?.let { BmiResult(it) }
        bfp?.let { BodyFatResult(it, isMale = isMale) }
        bmr?.let { BmrResult(it) }

        tdee?.let {
            Text(
                text = "TDEE: %.2f kcal/day".format(it),
                color = white,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        proteinIntake?.let { (minProtein, maxProtein) ->
            Text(
                text = "Protein Intake: %.2f - %.2f g/day".format(minProtein, maxProtein),
                color = white,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        waterIntake?.let {
            Text(
                text = "Water Intake: %.2f L/day".format(it),
                color = white,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        TextButton(onClick = {
            nc.currentBackStackEntry?.savedStateHandle?.apply {
                set("bmi", bmi)
                set("bmr", bmr)
                set("bfp", bfp)
                set("tdee", tdee)
                set("proteinIntake", proteinIntake)
                set("waterIntake", waterIntake)
            } // Pass the calculated results to the new screen
            nc.navigate(Screens.HCExplanationScreen.screen) // Redirect to the explanation screen
        }) {
            Text(
                text = "What do these results mean?",
                color = Color.Cyan,
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }
}


// Result Composables
@Composable
fun BmiResult(bmi: Double) {
    val (status, color) = when {
        bmi < 18.5 -> "Underweight" to Color.Red
        bmi < 24.9 -> "Healthy" to Color.Green
        bmi < 29.9 -> "Overweight" to Color.Yellow
        else -> "Obese" to Color.Red
    }

    Text(
        text = "BMI: %.1f → %s".format(bmi, status),
        color = color,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun BmrResult(bmr: Double) {
    Text(
        text = "BMR: %.0f kcal/day".format(bmr),
        color = white,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun BodyFatResult(bodyFat: Double, isMale: Boolean) {
    val (status, color) = if (isMale) {
        when {
            bodyFat < 6 -> "Too Low" to Color.Red
            bodyFat <= 13 -> "Athletic" to Color.Green
            bodyFat <= 17 -> "Fit" to Color.Green
            bodyFat <= 24 -> "Average" to Color.Yellow
            else -> "Obese" to Color.Red
        }
    } else {
        when {
            bodyFat < 14 -> "Too Low" to Color.Red
            bodyFat <= 20 -> "Athletic" to Color.Green
            bodyFat <= 24 -> "Fit" to Color.Green
            bodyFat <= 31 -> "Average" to Color.Yellow
            else -> "Obese" to Color.Red
        }
    }

    Text(
        text = "Body Fat: %.1f%% → %s".format(bodyFat, status),
        color = color,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun WaistToHipResult(ratio: Double, isMale: Boolean) {
    val (status, color) = if (isMale) {
        if (ratio > 0.90) "High Risk" to Color.Red else "Low Risk" to Color.Green
    } else {
        if (ratio > 0.85) "High Risk" to Color.Red else "Low Risk" to Color.Green
    }

    Text(
        text = "Waist-to-Hip Ratio: %.2f → %s".format(ratio, status),
        color = color,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun LeanBodyMassResult(lbm: Double) {
    Text(
        text = "Lean Body Mass: %.1f kg".format(lbm),
        color = white,
        style = MaterialTheme.typography.titleMedium
    )
}


@Preview(showBackground = true)
@Composable
fun HCPreview() {
    val nc = rememberNavController()
    GenniTheme { HealthCalculationsScreen(nc,HCViewModel(), AuthViewModel()) }
}