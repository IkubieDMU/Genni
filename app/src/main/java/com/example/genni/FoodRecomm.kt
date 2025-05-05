package com.example.genni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.genni.models.FoodItem
import com.example.genni.ui.theme.deepPurple
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.HCViewModel

@Composable
fun FoodRecommScreen(nc: NavController,hcViewModel: HCViewModel, authViewModel: AuthViewModel, isMale: Boolean = true) {

    // Read saved state values passed from previous screen
    val bmi by hcViewModel.bmi.collectAsState()
    val bfp by hcViewModel.bfp.collectAsState()
    val tdee by hcViewModel.tdee.collectAsState()
    val proteinIntake by hcViewModel.proteinIntake.collectAsState()

    val recommendedCategory = remember(bmi, bfp) {
        when {
            bmi == null || bfp == null -> "Overall Wellness"
            bmi!! < 18.5 -> "Muscle Gain"
            bmi!! in 18.5..24.9 && bfp!! <= if (isMale) 20.0 else 30.0 -> "Overall Wellness"
            bmi!! > 24.9 -> "Fat Loss"
            else -> "Overall Wellness"
        }
    }

    val foodRecommendations = mapOf(
        "Muscle Gain" to listOf(
            FoodItem("🍗 Grilled Chicken Breast", 165, 31),
            FoodItem("🥚 Eggs", 78, 6),
            FoodItem("🥜 Peanut Butter (2 tbsp)", 190, 8),
            FoodItem("🍚 Brown Rice (1 cup)", 215, 5),
            FoodItem("🥦 Broccoli (1 cup)", 55, 4),
            FoodItem("🍠 Sweet Potatoes (1 medium)", 103, 2),
            FoodItem("🧀 Cottage Cheese (1/2 cup)", 90, 12),
            FoodItem("🥩 Lean Beef (100g)", 250, 26),
            FoodItem("🍌 Banana", 105, 1),
            FoodItem("🥛 Whole Milk (1 cup)", 150, 8),
            FoodItem("🌰 Mixed Nuts (1 oz)", 170, 6),
            FoodItem("🍞 Ezekiel Bread (1 slice)", 80, 4),
            FoodItem("🫘 Black Beans (1/2 cup)", 114, 8),
            FoodItem("🐟 Tuna (canned, in water)", 120, 26)
        ),
        "Fat Loss" to listOf(
            FoodItem("🥗 Leafy Greens (1 cup)", 10, 1),
            FoodItem("🐟 Salmon (100g)", 208, 20),
            FoodItem("🍳 Boiled Eggs", 78, 6),
            FoodItem("🍓 Berries (1 cup)", 65, 1),
            FoodItem("🍎 Apples (1 medium)", 95, 0),
            FoodItem("🥑 Avocados (1/2)", 120, 1),
            FoodItem("🍵 Green Tea (unsweetened)", 2, 0),
            FoodItem("🌰 Almonds (1 oz)", 160, 6),
            FoodItem("🥒 Celery sticks", 10, 0),
            FoodItem("🥬 Kale (1 cup)", 35, 3),
            FoodItem("🧊 Greek Yogurt (plain, low-fat)", 100, 10),
            FoodItem("🍄 Mushrooms (1 cup)", 15, 2),
            FoodItem("🍠 Baked Sweet Potato (100g)", 90, 2),
            FoodItem("🫘 Lentils (1/2 cup)", 115, 9)
        ),
        "Overall Wellness" to listOf(
            FoodItem("🍌 Banana", 105, 1),
            FoodItem("🍊 Orange", 62, 1),
            FoodItem("🍞 Whole Grain Bread (1 slice)", 70, 3),
            FoodItem("🥛 Low-fat Milk (1 cup)", 103, 8),
            FoodItem("🍅 Tomato (1 medium)", 22, 1),
            FoodItem("🥒 Cucumber (1 cup)", 16, 1),
            FoodItem("🥬 Mixed Veggies (1 cup)", 80, 3),
            FoodItem("🍲 Lentil Soup (1 cup)", 180, 12),
            FoodItem("🍇 Grapes (1 cup)", 62, 0),
            FoodItem("🍏 Green Apple", 95, 0),
            FoodItem("🥜 Almond Butter (1 tbsp)", 98, 3),
            FoodItem("🫐 Blueberries (1 cup)", 84, 1),
            FoodItem("🫘 Chickpeas (1/2 cup)", 134, 7),
            FoodItem("🍠 Roasted Sweet Potato", 120, 2)
        ),
        "Healthy Snacks" to listOf(
            FoodItem("🥕 Carrot sticks with hummus", 100, 2),
            FoodItem("🧀 String cheese", 80, 6),
            FoodItem("🍿 Air-popped popcorn (3 cups)", 90, 3),
            FoodItem("🍎 Apple slices with peanut butter", 190, 4),
            FoodItem("🍫 Dark chocolate (2 squares)", 120, 2),
            FoodItem("🍇 Frozen grapes (1 cup)", 62, 0),
            FoodItem("🥒 Cucumber with Greek yogurt dip", 70, 5),
            FoodItem("🥜 Trail mix (1/4 cup)", 175, 5),
            FoodItem("🥛 Protein shake (whey, water)", 150, 25),
            FoodItem("🍞 Rice cake with almond butter", 130, 3),
            FoodItem("🍳 Hard-boiled egg", 78, 6),
            FoodItem("🍐 Pear slices with cheese", 160, 6),
            FoodItem("🍫 Protein bar (low sugar)", 200, 20)
        )
    )


    LazyColumn(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328)))).padding(16.dp)) {
        item {
            Text(
                text = "Recommended Foods for You",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Goal: $recommendedCategory",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Cyan
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (tdee != null && proteinIntake != null) {
                Text(
                    text = "Target: %.0f kcal/day, %.0f–%.0f g protein/day".format(
                        tdee!!,
                        proteinIntake!!.first,
                        proteinIntake!!.second
                    ),
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        foodRecommendations[recommendedCategory]?.let {
            item { FoodCategoryCardWithNutrition(title = recommendedCategory, foods = it) }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Suggested Snacks", style = MaterialTheme.typography.titleMedium, color = Color.White, modifier = Modifier.padding(top = 16.dp))
        }

        foodRecommendations["Healthy Snacks"]?.let { item { FoodCategoryCardWithNutrition(title = "Healthy Snacks", foods = it) } }
    }
}

@Composable
fun FoodCategoryCardWithNutrition(title: String, foods: List<FoodItem>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            foods.forEach { item ->
                Text(
                    text = "• ${item.name} — ${item.calories} kcal, ${item.protein}g protein",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

