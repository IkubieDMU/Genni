package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.models.User
import com.example.genni.ui.theme.*
import com.example.genni.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(nc: NavController, userViewModel: UserViewModel) {
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var yearsOfTraining by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val context = LocalContext.current.applicationContext
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, deepPurple)))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a new Genni Account", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = white)
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(firstName, { firstName = it }, "First Name", Icons.Default.Person, "First Name Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(middleName, { middleName = it }, "Middle Name (Optional)", Icons.Default.Person, "Middle Name Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(lastName, { lastName = it }, "Last Name", Icons.Default.Person, "Last Name Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(age, { age = it }, "Age", Icons.Default.DateRange, "Age Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(gender, { gender = it }, "Gender (M/F)", Icons.Default.Person, "Gender Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(goal, { goal = it }, "Goals (Comma separated)", Icons.Default.List, "Goals Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(yearsOfTraining, { yearsOfTraining = it }, "Years of Training", Icons.Default.FitnessCenter, "Training Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(username, { username = it }, "Username", Icons.Default.Person, "Username Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(email, { email = it }, "Email", Icons.Default.Email, "Email Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomPasswordTF(password, { password = it }, "Password", Icons.Default.Lock, "Password Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(weight, { weight = it }, "Weight (kg)", Icons.Default.Scale, "Weight Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomTF(height, { height = it }, "Height (cm)", Icons.Default.Height, "Height Icon")
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val user = User(
                userID = userViewModel.users.value.size + 1,
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                age = age.toIntOrNull() ?: 0,
                gender = gender.firstOrNull() ?: 'M',
                goal = goal.split(",").map { it.trim() },
                yearsOfTraining = yearsOfTraining.toIntOrNull() ?: 0,
                username = username,
                password = password,
                email = email,
                weight = weight.toDoubleOrNull() ?: 0.0,
                height = height.toDoubleOrNull() ?: 0.0,
                foodRecommendations = emptyList()
            )
            userViewModel.registerUser(user)
            Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
            nc.navigate("LoginScreen")
        }) {
            Text("Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { SignUpScreen(nc, UserViewModel()) }
}
