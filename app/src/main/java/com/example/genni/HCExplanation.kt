package com.example.genni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genni.ui.theme.deepPurple

@Composable
fun HealthMetricsExplanationScreen(nc: NavController) {
    val bmi = (nc.previousBackStackEntry?.savedStateHandle?.get<Double>("bmi"))?.toFloat()
    val bfp = (nc.previousBackStackEntry?.savedStateHandle?.get<Double>("bfp"))?.toFloat()
    val bmr = (nc.previousBackStackEntry?.savedStateHandle?.get<Double>("bmr"))?.toFloat()
    val tdee = (nc.previousBackStackEntry?.savedStateHandle?.get<Double>("tdee"))?.toFloat()
    val waterIntake = (nc.previousBackStackEntry?.savedStateHandle?.get<Double>("waterIntake"))?.toFloat()
    val proteinIntakeDouble = nc.previousBackStackEntry?.savedStateHandle?.get<Pair<Double, Double>>("proteinIntake")
    val proteinIntake = proteinIntakeDouble?.let { Pair(it.first.toFloat(), it.second.toFloat()) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328)))).padding(16.dp)) {
        item {
            Text(
                text = "Health Metric Explanations",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        val metrics = listOf(
            "BMI (Body Mass Index)" to "BMI is a widely used measure to categorize a person's weight relative to their height...",
            "BMR (Basal Metabolic Rate)" to "BMR is the number of calories your body needs to perform basic functions at rest...",
            "Body Fat Percentage (BFP)" to "BFP indicates what percentage of your total body weight is fat...",
            "TDEE (Total Daily Energy Expenditure)" to "TDEE estimates the total calories you burn daily, including all physical activity...",
            "Protein Intake Recommendation" to "Estimated daily protein needs based on your weight and activity level...",
            "Water Intake Recommendation" to "Recommended daily water consumption to stay hydrated and support body functions...",
            "Waist-to-Hip Ratio (WHR)" to "WHR indicates fat distribution and risk of metabolic and heart-related issues..."
        )

        items(metrics) { (title, explanation) ->
            MetricExplanationCard(title, explanation)
        }

        item {
            Text(
                text = "Personalized Recommendations",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        item {
            RecommendationCard(bmi, bfp, bmr, tdee, proteinIntake, waterIntake)
        }
    }
}

@Composable
fun MetricExplanationCard(title: String, explanation: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(explanation, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun RecommendationCard(
    bmi: Float?,
    bfp: Float?,
    bmr: Float?,
    tdee: Float?,
    proteinIntake: Pair<Float, Float>?,
    waterIntake: Float?
) {
    val bmiStatus = when {
        bmi == null -> "Unknown"
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }

    val bmiTip = when (bmiStatus) {
        "Underweight" -> "You're underweight. Consider increasing calorie intake and focusing on strength training."
        "Normal" -> "You're in a healthy range! Keep up the great work with a balanced diet and regular exercise."
        "Overweight" -> "You're slightly above the healthy range. Try incorporating more cardio and tracking your meals."
        "Obese" -> "Consider working with a professional to manage weight. Start with low-impact exercise and dietary adjustments."
        else -> "Unable to determine your BMI. Double-check your inputs."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("BMI Status: $bmiStatus", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(bmiTip, modifier = Modifier.padding(top = 8.dp))

            if (bmr != null && tdee != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("🧬 BMR: %.2f kcal/day".format(bmr))
                Text("🔥 TDEE: %.2f kcal/day".format(tdee))
                Text("➡️ Tip: Consume less than your TDEE for weight loss or more for bulking.")
            }

            if (bfp != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("⚖️ BFP: %.1f%%".format(bfp))
                Text(
                    when {
                        bfp > 25f -> "High body fat. Combine strength + cardio workouts and monitor nutrition."
                        bfp < 10f -> "Very low body fat. Ensure proper nutrition to maintain hormonal balance."
                        else -> "Your body fat percentage is within a healthy range!"
                    }
                )
            }

            if (proteinIntake != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("💪 Protein Intake: %.1f–%.1f g/day".format(proteinIntake.first, proteinIntake.second))
                Text("Breakdown: Aim for 20–30g per meal. Example:")
                Text("- Breakfast: 25g • Lunch: 30g • Dinner: 30g • Snack/Shake: 15g")
            }

            if (waterIntake != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("💧 Water Intake: %.1f liters/day".format(waterIntake))
                Text("Breakdown Suggestion:")
                Text("- Morning (8–11am): 0.5L • Afternoon (12–4pm): 1.0L")
                Text("- Evening (5–8pm): 0.7L • Before bed: 0.3L")
            }
        }
    }
}