package com.example.genni

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.genni.models.User
import com.example.genni.ui.theme.*
import com.example.genni.viewmodels.UserViewModel
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.app.DatePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(nc: NavController, userViewModel: UserViewModel) {
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var yearsOfTraining by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }

    // Error states
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var middleNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var ageError by remember { mutableStateOf<String?>(null) }
    var yearsOfTrainingError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var heightError by remember { mutableStateOf<String?>(null) }
    var weightError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            profilePicUri = it
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    val goalOptions = listOf("Build Muscle", "Burn Fat", "Improve Cardio")
    val selectedGoals = remember { mutableStateMapOf<String, Boolean>() }
    goalOptions.forEach { goal -> if (selectedGoals[goal] == null) selectedGoals[goal] = false }

    fun isValidCredential(value: String): Boolean {
        val hasUpper = value.any { it.isUpperCase() }
        val hasSymbol = value.any { !it.isLetterOrDigit() }
        return value.length >= 8 && hasUpper && hasSymbol
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple)))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a new Genni Account", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = white)
        Spacer(modifier = Modifier.height(10.dp))

        // First Name
        MyCustomTF(firstName, {
            firstName = it
            firstNameError = if (it.length > 20) "Max 20 characters" else null
        }, "First Name", Icons.Default.Person, "First Name Icon", isError = firstNameError != null)
        firstNameError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Middle Name
        MyCustomTF(middleName, {
            middleName = it
            middleNameError = if (it.length > 20) "Max 20 characters" else null
        }, "Middle Name (Optional)", Icons.Default.Person, "Middle Name Icon", isError = middleNameError != null)
        middleNameError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Last Name
        MyCustomTF(lastName, {
            lastName = it
            lastNameError = if (it.length > 20) "Max 20 characters" else null
        }, "Last Name", Icons.Default.Person, "Last Name Icon", isError = lastNameError != null)
        lastNameError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Age
        MyCustomTF(age, {
            age = it
            val ageNum = it.toIntOrNull()
            ageError = when {
                ageNum == null -> "Enter a valid number"
                ageNum !in 6..105 -> "Age must be between 6 and 105"
                else -> null
            }
        }, "Age", Icons.Default.DateRange, "Age Icon", isError = ageError != null)
        ageError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Gender dropdown
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
                    containerColor = Color.Transparent,
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
                modifier = Modifier.background(Color.Transparent)
            ) {
                genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption, color = mintGreen) },
                        onClick = {
                            gender = selectionOption
                            genderExpanded = false
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Goals Checkboxes
        Text("Select Your Goals", fontSize = 18.sp, color = white, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        goalOptions.forEach { goal ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Checkbox(
                    checked = selectedGoals[goal] ?: false,
                    onCheckedChange = { checked -> selectedGoals[goal] = checked },
                    colors = CheckboxDefaults.colors(checkedColor = mintGreen, uncheckedColor = Color.White)
                )
                Text(goal, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Years of Training
        MyCustomTF(yearsOfTraining, {
            yearsOfTraining = it
            val yot = it.toIntOrNull()
            yearsOfTrainingError = when {
                yot == null -> "Enter a valid number"
                yot !in 0..80 -> "Must be between 0 and 80"
                else -> null
            }
        }, "Years of Training", Icons.Default.FitnessCenter, "Training Icon", isError = yearsOfTrainingError != null)
        yearsOfTrainingError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Username
        MyCustomTF(username, {
            username = it
            usernameError = if (!isValidCredential(it)) "8+ chars, 1 capital & symbol" else null
        }, "Username", Icons.Default.Person, "Username Icon", isError = usernameError != null)
        usernameError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Email
        MyCustomTF(email, {
            email = it
            emailError = if (!it.contains("@") || !it.contains(".")) "Invalid email format" else null
        }, "Email", Icons.Default.Email, "Email Icon", isError = emailError != null)
        emailError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Password
        MyCustomPasswordTF(password, {
            password = it
            passwordError = if (!isValidCredential(it)) "8+ chars, 1 capital & symbol" else null
        }, "Password", Icons.Default.Lock, "Password Icon", isError = passwordError != null)
        passwordError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Weight
        MyCustomTF(weight, {
            weight = it
            val w = it.toDoubleOrNull()
            weightError = when {
                w == null -> "Enter a valid weight"
                w !in 15.0..300.0 -> "Weight must be 15–300 kg"
                else -> null
            }
        }, "Weight (kg)", Icons.Default.Scale, "Weight Icon", isError = weightError != null)
        weightError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(10.dp))

        // Height
        MyCustomTF(height, {
            height = it
            val h = it.toDoubleOrNull()
            heightError = when {
                h == null -> "Enter a valid height"
                h !in 100.0..300.0 -> "Height must be 100–300 cm!!"
                else -> null
            }
        }, "Height (cm)", Icons.Default.Height, "Height Icon", isError = heightError != null)
        heightError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Upload Profile Picture")
        }

        Spacer(modifier = Modifier.height(10.dp))

        profilePicUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = ImageRequest.Builder(context).data(uri).crossfade(true).build()),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val selectedGoalList = selectedGoals.filterValues { it }.keys.toList()

            val allValid = listOf(
                firstNameError, middleNameError, lastNameError, ageError, yearsOfTrainingError,
                usernameError, passwordError, emailError, heightError, weightError
            ).all { it == null }

            if (!allValid) {
                Toast.makeText(context, "Please fix the highlighted errors", Toast.LENGTH_SHORT).show()
                return@Button
            }

            val user = User(
                userID = 0,
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                age = age.toIntOrNull() ?: 0,
                gender = gender.ifBlank { "M" },
                goals = selectedGoalList,
                yearsOfTraining = yearsOfTraining.toIntOrNull() ?: 0,
                username = username,
                password = password,
                email = email,
                weight = weight.toDoubleOrNull() ?: 0.0,
                height = height.toDoubleOrNull() ?: 0.0,
                profilePic = profilePicUri?.toString(),
                foodRecommendations = emptyList()
            )

            userViewModel.registerUser(user,
                onSuccess = {
                    Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                    nc.navigate(Screens.LoginScreen.screen)
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
