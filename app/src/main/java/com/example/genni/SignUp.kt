package com.example.genni

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.genni.models.User
import com.example.genni.ui.theme.*
import com.example.genni.viewmodels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(nc: NavController, userViewModel: UserViewModel) {
    // User Input Variables
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
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current.applicationContext // For Toasts
    val scrollState = rememberScrollState() //Scroll functionality
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePicUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, deepPurple)))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
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

        // Gender DropDownList (M/F)
        var genderExpanded by remember { mutableStateOf(false) }
        val genderOptions = listOf("M", "F")

        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Transparent Background
                    focusedIndicatorColor = mintGreen,
                    unfocusedIndicatorColor = mintGreen,
                    focusedLabelColor = mintGreen,
                    cursorColor = mintGreen,
                    focusedTextColor = mintGreen,
                    unfocusedTextColor = mintGreen
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.menuAnchor().fillMaxWidth().padding(vertical = 4.dp)
            )

            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false },
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                selectionOption,
                                color = mintGreen,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            gender = selectionOption
                            genderExpanded = false
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        MyCustomTF(goal, { goal = it }, "Goals (Comma separated)", Icons.AutoMirrored.Default.List, "Goals Icon")
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

        // Profile Picture Upload Button
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Upload Profile Picture")
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Display selected image (if any)
        profilePicUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(onClick = {
            val user = User(
                userID = 0, // Firestore auto-generates document IDs
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                age = age.toIntOrNull() ?: 0,
                gender = gender.ifBlank { "M" },
                goals = goal.split(",").map { it.trim() },
                yearsOfTraining = yearsOfTraining.toIntOrNull() ?: 0,
                username = username,
                password = password,
                email = email,
                weight = weight.toDoubleOrNull() ?: 0.0,
                height = height.toDoubleOrNull() ?: 0.0,
                profilePic = profilePicUri?.toString(),
                foodRecommendations = emptyList()
            )

            userViewModel.registerUser(
                user,
                onSuccess = {
                    Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                    nc.navigate("LoginScreen")
                },
                onFailure = { error ->
                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                }
            )
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
