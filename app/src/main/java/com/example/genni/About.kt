package com.example.genni

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(deepPurple, emeraldGreen))).verticalScroll(scrollState).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Section
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp).clip(RoundedCornerShape(20.dp)).background(emeraldGreen.copy(alpha = 0.2f)).padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(R.drawable.genniappiconnb), contentDescription = null, /*modifier = Modifier.size(80.dp)*/)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Genni", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Your AI-Powered Workout Partner", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Description Section
        Text("About Genni", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
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
            "🏋️‍♂️ Personalized Workouts: Generate custom workout plans based on your fitness goals, experience level, and equipment availability.",
            "🧮 Health Metrics: Calculate BMI, FFMI, BMR, body fat %, and daily calorie needs with helpful insights.",
            "🧘‍♀️ Guided Breathing: Follow calming breathing exercises designed for stress relief and improved focus.",
            "💾 Save & Reuse Workouts: Store your favorite routines and easily access them for future workouts.",
            "🎨 Modern UI: Navigate through a sleek, intuitive interface designed for a smooth and motivating experience.",
            "⏱️ Smart Timers: Get visual and audio cues for rest, work, and breathing cycles without missing a beat.",
            "🎯 Goal-Oriented: Choose from strength, endurance, flexibility, or general wellness objectives.",
            "📱 All-in-One: A fitness tracker, planner, and coach — all in your pocket!"
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
                Text(text = "Developed by Ikubie Nemieboka", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Version 8.8.0", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() { GenniTheme{ AboutScreen() } }