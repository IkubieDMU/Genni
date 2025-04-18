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
            "BMI (Body Mass Index)" to """
    📏 BMI is a simple calculation that helps determine whether a person is underweight, normal weight, overweight, or obese.
    ⚖️ It's calculated by dividing a person’s weight in kilograms by the square of their height in meters (kg/m²).
    🧠 While it's a quick and useful screening tool, it doesn’t measure body fat directly or take muscle mass into account.
    🏋️‍♂️ For example, athletes with high muscle mass might have a high BMI but low body fat.
""".trimIndent(),

            "BMR (Basal Metabolic Rate)" to """
    🔥 BMR represents the number of calories your body needs to maintain basic functions like breathing, circulation, and cell production while at complete rest.
    🛌 It’s essentially the energy your body needs just to keep you alive if you did nothing but lie in bed all day.
    📊 Knowing your BMR is useful for understanding how many calories you need daily to maintain, lose, or gain weight when combined with your activity level.
""".trimIndent(),

            "Body Fat Percentage (BFP)" to """
    ⚖️ BFP tells you what percentage of your total body weight is made up of fat.
    📐 Unlike BMI, which looks at weight relative to height, BFP gives a clearer picture of body composition.
    👥 A healthy body fat percentage depends on age and gender.
    ❗ Too much fat, especially around the abdomen, increases the risk of health issues like ❤️ heart disease and 🍬 diabetes.
    ⚠️ Too little body fat can also be harmful, especially for 🧬 hormone function and 🔋 energy levels.
""".trimIndent(),

            "TDEE (Total Daily Energy Expenditure)" to """
    🔥 TDEE is an estimate of how many calories you burn in a full day, including all activities — from 🛌 sleeping and 🍽️ eating to 🚶 walking, 🏋️‍♂️ exercising, and even 🤸 fidgeting.
    📊 It's calculated using your BMR multiplied by an activity factor based on how active your lifestyle is.
    🎯 Understanding your TDEE helps you create a plan for weight management:
    ✅ To maintain your current weight, eat about your TDEE.
    ⬇️ To lose weight, eat fewer calories than your TDEE.
    ⬆️ To gain weight, eat more than your TDEE.
""".trimIndent(),

            "Protein Intake Recommendation" to """
    💪 Protein is essential for building and repairing muscles, supporting the 🛡️ immune system, and producing 🔬 enzymes and 🧬 hormones.
    ⚖️ The recommended daily intake depends on your weight, age, activity level, and fitness goals.
    
    📌 For example:
    - 🪑 Sedentary individuals may need around 0.8g of protein per kg of body weight.
    - 🏃 Active people or those aiming to build muscle may need 1.2–2.2g per kg.
    
    🛠️ Getting enough protein helps with recovery after workouts and promotes a healthy body composition.
""".trimIndent(),

            "Water Intake Recommendation" to """
    💧 Water is crucial for every function in your body — from 🍽️ digestion and 🧪 nutrient absorption to 🌡️ temperature regulation and 🦵 joint lubrication.
    📏 A general rule of thumb is to drink about 2–3 liters (8–12 cups) of water per day, but your needs can vary based on your ⚖️ weight, 🌤️ climate, and 🏃 activity level.
    
    🚰 Staying well-hydrated helps with 🧠 focus, ⚡ energy, and even ⚖️ weight management.
    🔍 A good indicator is the color of your urine — 💛 light yellow means you're likely well-hydrated.
""".trimIndent(),

            "Waist-to-Hip Ratio (WHR)" to """
    📏 WHR is a measure of fat distribution in your body — it's calculated by dividing the circumference of your 🧍 waist by the circumference of your 🍑 hips.
    
    ❤️ It helps assess your risk for heart disease, type 2 diabetes, and other ⚠️ metabolic conditions.
    
    📉 A high WHR (more fat around the waist) is associated with increased health risks, even if your BMI is in a normal range.
    
    ✅ Healthy WHR values are typically:
    - 👨 Less than 0.90 for men
    - 👩 Less than 0.85 for women
""".trimIndent(),

            "Resting Heart Rate (RHR)" to """
    ❤️ RHR is the number of times your heart beats per minute while you're completely at rest.
    📊 It reflects your cardiovascular health and fitness level.
    
    ✅ Normal ranges:
    - 🧍‍♂️ Adults: 60–100 bpm
    - 🏋️‍♀️ Athletes: Often 40–60 bpm
    
    📉 Lower RHR generally indicates better cardiovascular fitness.
    💡 Tracking it over time can help spot health trends or improvements.
""".trimIndent(),

            "VO2 Max" to """
    🫁 VO2 Max measures the maximum amount of oxygen your body can use during intense exercise.
    🔬 It's one of the best indicators of aerobic endurance and cardiovascular fitness.
    
    📈 Higher VO2 Max means your body can produce more energy during workouts, making you more efficient and fit.
    
    🏃‍♂️ Elite endurance athletes typically have a very high VO2 Max.
    💪 Beginners can gradually improve theirs through consistent aerobic exercise.
""".trimIndent(),

            "Lean Body Mass (LBM)" to """
    🏋️‍♀️ LBM represents everything in your body except fat — including muscles, bones, organs, and fluids.
    🧠 It's important because it helps estimate your true muscle mass and overall health.
    
    ⚖️ Having more LBM means better metabolism and strength.
    📏 LBM is often used with BFP to design more accurate fitness and nutrition plans.
""".trimIndent()
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
fun RecommendationCard(bmi: Float?, bfp: Float?, bmr: Float?, tdee: Float?, proteinIntake: Pair<Float, Float>?, waterIntake: Float?) {
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
        else -> "Unable to determine your BMI. An Unexpected Error has occurred"
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
                Text("🧍‍♂️⚖️ Body Fat Percentage (BFP): %.1f%%".format(bfp))

                Text(
                    when {
                        bfp > 25f -> "📈 High body fat detected. 🏋️ Try mixing strength + cardio, and 🍽️ focus on balanced nutrition!"
                        bfp < 10f -> "📉 Very low body fat. 🥗 Make sure you're eating enough to support energy and hormone levels!"
                        else -> "✅ Your BFP is in a healthy range! 🎯 Keep up the great work with your training and nutrition!"
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text("🔍 Tip: BFP offers a better view of body composition than weight or BMI alone.")
            }

            if (proteinIntake != null) {
                Spacer(modifier = Modifier.height(12.dp))

                val minA = proteinIntake.first * 0.2
                val minB = proteinIntake.second * 0.2
                val maxA = proteinIntake.first * 0.3
                val maxB = proteinIntake.second * 0.3

                Text(
                    "🍽️ Per Meal Protein Aim:\n" +
                            "🔹 Minimum: %.1f–%.1f g\n🔹 Maximum: %.1f–%.1f g"
                                .format(minA, maxA, minB, maxB)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val (min, max) = proteinIntake
                val proportions = listOf(0.25, 0.30, 0.30, 0.15)
                val mealNames = listOf("🍳 Breakfast", "🥗 Lunch", "🍲 Dinner", "🥤 Snack/Shake")
                val mealRanges = proportions.map { prop -> "%.1f–%.1f g".format(min * prop, max * prop) }

                Text("💪 Personalized Protein Breakdown:")

                for (i in mealNames.indices) {
                    Text("- ${mealNames[i]}: ${mealRanges[i]}")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("📌 Tip: Distributing protein across meals improves muscle recovery and satiety throughout the day!")
            }



            if (waterIntake != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("💧 Daily Water Goal: %.1f liters/day".format(waterIntake))
                Text("💦 Breakdown Suggestion:")
                Text("🌅 Morning (8–11am): 🧊 0.5L")
                Text("🌞 Afternoon (12–4pm): 💦 1.0L")
                Text("🌇 Evening (5–8pm): 🚰 0.7L")
                Text("🌙 Before bed: 💧 0.3L (light sip)")

                Spacer(modifier = Modifier.height(4.dp))
                Text("💡 Tip: Keep a reusable bottle with you to stay hydrated throughout the day!")
            }

        }
    }
}