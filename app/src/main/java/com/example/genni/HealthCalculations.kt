package com.example.genni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthCalculationsScreen(viewModel: HCViewModel) {
    val bmi by viewModel.bmi.collectAsState()
    val muscleMass by viewModel.muscleMass.collectAsState()
    val bmr by viewModel.bmr.collectAsState()
    val bfp by viewModel.bfp.collectAsState()
    val tdee by viewModel.tdee.collectAsState()
    val proteinIntake by viewModel.proteinIntake.collectAsState()
    val waterIntake by viewModel.waterIntake.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328)))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.healthicon),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(text = "Health Calculations", color = white, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

        MyCustomTF(value = viewModel.weightInput.collectAsState().value, updatedValue = { viewModel.onWeightChange(it) }, labelText = "Weight (kg)", leading_Icon = Icons.Default.FitnessCenter, iconDesc = "Weight Icon")
        MyCustomTF(value = viewModel.heightInput.collectAsState().value, updatedValue = { viewModel.onHeightChange(it) }, labelText = "Height (cm)", leading_Icon = Icons.Default.Height, iconDesc = "Height Icon")
        MyCustomTF(value = viewModel.ageInput.collectAsState().value, updatedValue = { viewModel.onAgeChange(it) }, labelText = "Age", leading_Icon = Icons.Default.CalendarToday, iconDesc = "Age Icon")
        MyCustomTF(value = viewModel.muscleMassPercentageInput.collectAsState().value, updatedValue = { viewModel.onMuscleMassPercentageChange(it) }, labelText = "Muscle Mass Percentage (%)", leading_Icon = Icons.Default.BarChart, iconDesc = "Muscle Mass Icon")

        Button(onClick = { viewModel.calculate() }, colors = ButtonDefaults.buttonColors(containerColor = emeraldGreen), modifier = Modifier.size(120.dp,90.dp).padding(top = 16.dp)) {
            Text(text = "Calculate", color = white, fontWeight = FontWeight.Bold)
        }

        bmi?.let { Text(text = "BMI: %.2f".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
        muscleMass?.let { Text(text = "Muscle Mass: %.2f kg".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
        bmr?.let { Text(text = "BMR: %.2f kcal/day".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
        bfp?.let { Text(text = "BFP: %.2f %%".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
        tdee?.let { Text(text = "TDEE: %.2f kcal/day".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
        proteinIntake?.let { Text(text = "Protein Intake: %.2f g/day".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
        waterIntake?.let { Text(text = "Water Intake: %.2f L/day".format(it), modifier = Modifier.padding(top = 8.dp), color = white) }
    }
}

@Preview(showBackground = true)
@Composable
fun HCPreview() {
    GenniTheme { HealthCalculationsScreen(HCViewModel()) }
}