package com.example.genni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen

@Composable
fun AboutScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(deepPurple, emeraldGreen)))
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(emeraldGreen.copy(alpha = 0.2f))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(R.drawable.genniappiconnb), contentDescription = null, /*modifier = Modifier.size(80.dp)*/)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Genni",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Your AI-Powered Workout Partner",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Description Section
        Text(
            text = "About Genni",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Genni is an AI-powered workout application designed to help you achieve your fitness goals with personalized workout routines, health calculations, breathing exercises, and nutritional recommendations. Whether you're a beginner or a seasoned athlete, Genni adapts to your needs and helps you push your limits.",
            color = Color.White.copy(alpha = 0.8f),
            lineHeight = 22.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Features List
        Text(text = "Features", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 12.dp))

        val features = listOf(
            "✔️ Generate personalized workout plans based on your goals.",
            "✔️ Track your progress with sets, reps, and rest timers.",
            "✔️ Perform health calculations like BMI, FFMI, and daily nutritional needs.",
            "✔️ Enjoy guided breathing exercises for relaxation and focus.",
            "✔️ Save your workouts and revisit them anytime.",
            "✔️ Sleek and modern user interface for a smooth experience."
        )

        features.forEach { feature ->
            Text(
                text = feature,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Credits & Version Info
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(emeraldGreen.copy(alpha = 0.2f)).padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Developed by Your Team", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Version 1.0.0", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() { GenniTheme{ AboutScreen() } }