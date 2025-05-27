# 💪 Genni – Generative Workout Android Mobile App

**Genni** is an Android application designed to help users generate personalised workout routines based on their fitness goals and available equipment. Whether you're at home or in the gym, Genni provides tailored exercises to keep you on track.

---

## 🚀 Features

- 🎯 **Personalised Workouts** – Generate routines based on muscle groups, equipment, and fitness levels.
- 📚 **Extensive Exercise Library** – Access various exercises targeting various muscle groups.
- 🏋️ **Equipment Flexibility** – Choose workouts based on available equipment, from bodyweight to gym machines.
- 🧭 **User-Friendly Interface** – Navigate through workouts with an intuitive and clean design.

---

## ⚙️ Installation

```bash
git clone https://github.com/IkubieDMU/Genni.git
```

Here is the comprehensive, simple, and easy-to-follow documentation for your entire Genni project, based on all the code you've provided! I'll break down everything clearly and use plenty of emojis to make it fun. 🚀✨

---

# Genni App: Full Code Documentation 📱💪

Welcome to the complete documentation for your Genni Android application! This guide will walk you through every part of the code, explaining what each piece does and how it contributes to the overall app experience. Let's get started!

---

## 1. 🏠 Core Application Setup

This section covers the foundational parts of your app, including where it starts and how it navigates between different sections.

### 1.1. `MainActivity.kt` - The App's Launchpad 🚀

This is the very first file that runs when your app starts. It's like the main control center!

* **`onCreate(savedInstanceState: Bundle?)`**:
    * `enableEdgeToEdge()`: Makes your app's content stretch nicely across the whole screen, even under the status bar and navigation bar. Super immersive! 📱✨
    * `setContent { ... }`: This is where your entire Jetpack Compose UI is built. Think of it as the canvas for your app's look and feel. 🎨
    * `GenniTheme { App() }`: Applies your custom Genni design theme to everything and then calls the `App()` function to set up the rest of the application.

* **`App()` Composable**:
    * **`rememberNavController()`**: This is your app's navigator! It helps you move between different screens and keeps track of where you've been (the "back stack"). 🗺️
    * **ViewModel Initialization**: You'll see a bunch of `remember { ... ViewModel() }` lines. These create and hold onto your app's "brains" (ViewModels) that manage data and logic for different parts of the app. They stick around even when screens change! 🧠💡
        * `UserViewModel`, `AuthViewModel`, `WorkoutViewModel`, `AppSettingsViewModel`, `AdminViewModel`, `HomeViewModel`, `HCViewModel`, `ForgetPasswordViewModel`, `BEViewModel`.
    * **`GenniTheme(darkTheme = isDarkTheme)`**: The app's overall look (light or dark mode) is set here, based on user preferences. 🌓
    * **`NavHost(...) { ... }`**: This defines all the possible paths and destinations within your app.
        * `startDestination = Screens.LoginScreen.screen`: When the app first opens, it always lands on the login screen. ➡️
        * **`composable(Screens.ScreenName.screen) { ScreenComposable(...) }`**: Each `composable` block maps a specific route (like "loginscreen") to a Composable function (like `LoginScreen`). This is how your navigator knows which UI to show for each path! ✅

### 1.2. `Screens.kt` - The App's Map 🗺️📌

This file contains a special type of class called a `sealed class` that lists *all* the possible screens in your app. It's like a table of contents for your navigation!

* **`sealed class Screens(val screen: String)`**:
    * This class ensures that only the screens you define can exist. It makes your navigation safe and easy to manage, preventing typos in screen names. 🔒
    * **`data object LoginScreen:Screens("loginscreen")`**: Each line here is a unique screen, with a clear name (e.g., `LoginScreen`) and a unique string identifier (e.g., `"loginscreen"`) that the `NavController` uses.
    * You have routes for:
        * Authentication: `LoginScreen`, `SignUpScreen`, `ForgetPasswordScreen`, `FPContdScreen` 🔐
        * Main Features: `HomeScreen`, `GeneratedWorkoutScreen`, `WorkoutSimulatorScreen`, `SavedWorkoutScreen` 🏋️‍♀️
        * Health Tools: `HealthCalculationsScreen`, `HCExplanationScreen`, `FoodRecommScreen` ❤️
        * Mindfulness: `BreathingExercisesScreen`, `BreathingExercisesSimulatorScreen` 🧘‍♀️
        * User & App Info: `SettingsScreen`, `AboutScreen`, `ProfileScreen` ⚙️
        * Admin Features: `AdminHomeScreen`, `AdminLoginScreen`, `AdminSignUpScreen` 👑

---

## 2. 🧩 Reusable UI Components (Composables)

These are small, independent building blocks that you can use across many different screens to keep your UI consistent and your code clean!

### 2.1. Input Fields (`MyCustomTF`, `MyCustomPasswordTF`) ⌨️

These are custom text input fields with a consistent look and feel.

* **`MyCustomTF(value, updatedValue, labelText, leading_Icon, iconDesc, isError)`**:
    * **Purpose:** A general-purpose text input field. 📝
    * **Features:**
        * **`value` & `updatedValue`**: Holds the text and updates it when you type.
        * **`labelText`**: The hint text that floats when you type. 🏷️
        * **`leading_Icon`**: An icon at the start of the text field (e.g., a person icon for username). 🖼️
        * **`isError`**: If `true`, the field turns red to show an error. 🚫
        * **Styling**: Has rounded corners, transparent background, and custom colors for icons, labels, and text, making it look sleek! 🎨

* **`MyCustomPasswordTF(value, updatedValue, labelText, leading_Icon, iconDesc, isError)`**:
    * **Purpose:** Specifically for password input. 🔑
    * **Features:** All the good stuff from `MyCustomTF`, plus:
        * **Password Visibility Toggle:** A little eye icon at the end that you can tap to show or hide the password characters. Super handy for security and usability! 👁️‍🗨️

### 2.2. `ClickableText` - Tappable Text 👆

```kotlin
@Composable
fun ClickableText(text: String, color: Color, fontsize: TextUnit, onClick: () -> Unit) {
    Text(
        text,
        color = color,
        fontSize = fontsize,
        modifier = Modifier.clickable{ onClick() }
    )
}
```

* **Purpose:** Makes any piece of text tappable, turning it into a clickable link or button. 🔗
* **How it works:** You give it some `text`, a `color`, a `fontsize`, and a special `onClick` action that happens when the user taps it.

### 2.3. `InfoRow` - Displaying Information Neatly 📊

```kotlin
@Composable
fun InfoRow(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).background(Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(12.dp)).padding(12.dp)
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}
```

* **Purpose:** Presents a label and its corresponding value in a clean, organized row. Perfect for profile details or health metrics. 📝
* **Styling:** Each `InfoRow` has a slightly transparent white background with rounded corners, making individual pieces of information stand out nicely. ✨

### 2.4. `SettingToggle` - On/Off Switches 🔛

```kotlin
@Composable
fun SettingToggle(label: String, state: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White, fontSize = 16.sp)
        Switch(checked = state, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = emeraldGreen))
    }
}
```

* **Purpose:** A reusable component for creating toggle switches for app settings. ⚙️
* **Features:** Displays a `label` (e.g., "Dark Mode") and a `Switch` that reflects the current `state` (on/off). When toggled, it calls `onToggle` to update the setting.

### 2.5. `WorkoutBox` - Interactive Workout Category Boxes 🏋️‍♂️

```kotlin
@Composable
fun WorkoutBox(title: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.05f))))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
    }
}
```

* **Purpose:** Used on the `HomeScreen` to represent different workout customization options (e.g., "Muscle Groups").
* **Features:** These are square boxes with a `title`, a subtle gradient background, a white border, and rounded corners. They are `clickable` to open a bottom sheet for selection. ✨

### 2.6. `PlayButton` & `StartButton` - Action Triggers ▶️

These are large, visually appealing buttons to start workouts or breathing sessions.

* **`PlayButton(context, navController, workouts)` (in `GeneratedWorkoutScreen`)**:
    * **Purpose:** Starts the generated workout session. 🚀
    * **Features:** A large, circular `IconButton` with a gradient background and a white border. It has a `PlayArrow` icon. Tapping it navigates to the `WorkoutSimulatorScreen` if there are workouts available.
* **`StartButton(context, navController)` (in `BreathingExercisesScreen`)**:
    * **Purpose:** Starts the breathing exercise session. 🧘‍♀️
    * **Features:** Very similar to `PlayButton`, it's a large, circular `IconButton` that navigates to the `BreathingExercisesSimulatorScreen`.

### 2.7. `DropdownMenuDurationSelector` - Time Selection ⏰

```kotlin
@Composable
fun DropdownMenuDurationSelector(selectedDuration: Int, onDurationSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = (5..10).toList()

    Box {
        Button(onClick = { expanded = true }) {
            Text("$selectedDuration min")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { duration ->
                DropdownMenuItem(
                    text = { Text("$duration minutes") },
                    onClick = {
                        onDurationSelected(duration)
                        expanded = false
                    }
                )
            }
        }
    }
}
```

* **Purpose:** Allows users to select a duration (e.g., for breathing exercises) from a dropdown menu.
* **Features:** Displays a button showing the `selectedDuration`. When clicked, a `DropdownMenu` appears with options (5 to 10 minutes). Selecting an option updates the `selectedDuration`.

### 2.8. `BreathingExerciseCard` - Exercise Details 🌬️

```kotlin
@Composable
fun BreathingExerciseCard(exercise: BreathingExercise, exerciseNumber: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).shadow(8.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp)),
        backgroundColor = white
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color = white, shape = CircleShape)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = deepPurple, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Air, contentDescription = null, modifier = Modifier.size(50.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Exercise ${exerciseNumber}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Inhale - ${exercise.inhaleTime}s |  Hold - ${exercise.holdTime}s  |  Exhale - ${exercise.exhaleTime}s",
                    fontSize = 16.sp,
                    color = deepPurple
                )
            }
        }
    }
}
```

* **Purpose:** Displays a single breathing exercise with its details.
* **Features:** A `Card` with rounded corners. Inside, it shows an `Air` icon, the exercise number, and the inhale, hold, and exhale times. 🧘‍♀️

### 2.9. `WorkoutCard` - Workout Exercise Details 🏋️‍♀️

```kotlin
@Composable
fun WorkoutCard(workout: Workout) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).shadow(8.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // ... image and workout name/sets/reps/rest time
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Muscles: ${workout.muscleGroupWorked.joinToString()}", fontSize = 14.sp, color = Color.Black.copy(alpha = 0.8f))
            Text(text = "Equipment: ${workout.equipmentUsed.joinToString()}", fontSize = 14.sp, color = Color.Black.copy(alpha = 0.8f))
        }
    }
}
```

* **Purpose:** Displays a single workout exercise with its details.
* **Features:** A `Card` with rounded corners. It shows the exercise image, name, sets, reps, rest time, and lists the muscle groups worked and equipment used. 💪

### 2.10. `FoodCategoryCardWithNutrition` - Food Recommendations 🍎

```kotlin
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
```

* **Purpose:** Displays a category of food recommendations with their calorie and protein content.
* **Features:** A `Card` with rounded corners and a title (e.g., "Muscle Gain"). It then lists each food item with its name, calories, and protein. 🥦🍗

### 2.11. `MetricExplanationCard` & `RecommendationCard` - Health Insights 💡

* **`MetricExplanationCard(title: String, explanation: String)`**:
    * **Purpose:** Provides detailed explanations for various health metrics (BMI, BMR, etc.).
    * **Features:** A `Card` that displays a `title` (e.g., "BMI") and a multi-line `explanation` for that metric. 🧠
* **`RecommendationCard(bmi, bfp, bmr, tdee, proteinIntake, waterIntake)`**:
    * **Purpose:** Summarizes personalized health recommendations based on calculated metrics.
    * **Features:** A `Card` that shows your BMI status (Underweight, Normal, Overweight, Obese) with a tip, and then lists your BMR, TDEE, personalized protein intake breakdown, and daily water goal with helpful suggestions. ❤️💧

### 2.12. `AnimatedSavedWorkoutItem` - Animated Saved Workout Item ✨

```kotlin
@Composable
fun AnimatedSavedWorkoutItem(savedWorkout: SavedWorkout, onWorkoutClick: (SavedWorkout) -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isClicked) 0.95f else 1f, animationSpec = tween(durationMillis = 150))
    val alpha by animateFloatAsState(targetValue = if (isClicked) 0.8f else 1f, animationSpec = tween(durationMillis = 150))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .alpha(alpha)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                isClicked = true
                onWorkoutClick(savedWorkout)
            }
            .animateContentSize(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        // ... content of the card (workout name, timestamp, icon)
    }
}
```

* **Purpose:** Displays a single saved workout in a list with a nice click animation.
* **Features:** When you tap it, the card subtly scales down and becomes slightly transparent, then returns to normal. It shows the workout's `name` and the `timestamp` it was saved, along with a fitness icon. 💾

---

## 3. 🖥️ App Screens (Detailed Breakdown)

Now let's look at the main screens of your application and what they do.

### 3.1. Authentication & User Management

#### 3.1.1. `LoginScreen.kt` - Welcome Back! 🔐

This is where users sign into their Genni account.

* **UI Elements:**
    * "Welcome Back to Genni" title.
    * `MyCustomTF` for Username.
    * `MyCustomPasswordTF` for Password.
    * `ClickableText` for "Forget Password?" (navigates to `ForgetPasswordScreen`).
    * **Login Button:** A green, rounded button. It shows a loading spinner (`CircularProgressIndicator`) when authentication is in progress. 🔄
    * `ClickableText` for "Don't have an account? Sign Up" (navigates to `SignUpScreen`).
* **Logic (via `AuthViewModel` & `UserViewModel`):**
    * Collects username and password input.
    * Calls `authViewModel.authenticateUser()` to verify credentials.
    * Displays `Toast` messages for success or error. 🎉🚫
    * Navigates to `HomeScreen` on successful login.

#### 3.1.2. `SignUpScreen.kt` - Create Your Account! 📝

This screen allows new users to register for the app.

* **UI Elements:**
    * Various `MyCustomTF` fields for: First Name, Middle Name (Optional), Last Name, Age, Years of Training, Username, Email, Weight, Height.
    * `MyCustomPasswordTF` for Password.
    * **Gender Dropdown:** Uses `ExposedDropdownMenuBox` for selecting "M" or "F". 🔽
    * **Goals Checkboxes:** Allows users to select multiple fitness goals (Build Muscle, Burn Fat, Improve Cardio). ☑️
    * **"Upload Profile Picture" Button:** Opens a gallery picker to select an image. 📸
    * **Sign Up Button:** Triggers the registration process.
* **Logic (via `UserViewModel`):**
    * **Input Validation:** Each input field has real-time validation (e.g., max characters for names, age range, email format, password strength). Error messages appear below the fields. 🚫
    * **Profile Picture Handling:** Uses `rememberLauncherForActivityResult` to pick an image and `AsyncImage` (from Coil library) to display it. It also requests persistent URI permission. 🔒
    * **User Object Creation:** Gathers all input data into a `User` data class. 📦
    * Calls `userViewModel.registerUser()` to save the new user's data.
    * Displays `Toast` messages for success or error and navigates to `LoginScreen` on success. 🎉

#### 3.1.3. `FPContd.kt` & `ForgotPasswordScreen.kt` - Password Reset 🔑

These screens handle the process of resetting a user's password.

* **`ForgotPasswordScreen`**:
    * **Purpose:** Initiates the password reset process by collecting the user's email.
    * **UI Elements:** Email input field, "Reset Password" button, and "Back to Login" link.
    * **Logic (via `ForgetPasswordViewModel`):**
        * Collects email.
        * Calls `fpViewModel.resetPassword()` (which would typically send a reset link/code).
        * Navigates to `FPContdScreen` if the email is found and the reset process can continue.
        * Displays loading indicator or error messages. 🔄🚫
* **`FPContd`**:
    * **Purpose:** Allows the user to set a new password after a successful reset initiation.
    * **UI Elements:** "New Password" and "Confirm Password" input fields, "Update Password" button.
    * **Logic (via `ForgetPasswordViewModel`):**
        * Validates that new passwords match and meet strength requirements (8+ chars, 1 uppercase, 1 symbol). 🚫
        * Calls `fpViewModel.updatePassword()` to change the password.
        * Navigates back to `LoginScreen` on successful update. 🎉

#### 3.1.4. `ProfileScreen.kt` - Your Personal Dashboard 👤

This screen displays all the registered information about the current user.

* **UI Elements:**
    * User's profile picture (loads from URI or shows a placeholder). 🖼️
    * Full name, username, and email.
    * A `HorizontalDivider` for visual separation.
    * Multiple `InfoRow` components displaying: Age, Gender, Height, Weight, Years of Training, and Goals. 📊
* **Logic (via `AuthViewModel`):**
    * Collects `currentUser` data from `AuthViewModel` to populate the UI. If no user is logged in, it shows a "No user data found" message. 😔

#### 3.1.5. `SettingsScreen.kt` - Customize Your App ⚙️

This screen allows users to adjust app-wide settings.

* **UI Elements:**
    * "Settings" title.
    * (Commented out) `SettingToggle` components for "Dark Mode", "Enable Notifications", "Use Metric Units", "Auto-start Workouts". These show the intended functionality. 💡
    * **Logout Button:** A prominent red button to sign out of the app. 🚪
* **Logic (via `AuthViewModel` & `AppSettingsViewModel`):**
    * Observes settings states from `AppSettingsViewModel`.
    * The "Logout" button calls `authViewModel.logout()`, then navigates to `LoginScreen` and shows a `Toast`. 👋

### 3.2. Workout Generation & Simulation

#### 3.2.1. `HomeScreen.kt` - Design Your Workout! 🏠

This is the main screen where users can customize and generate their workout plans.

* **UI Elements:**
    * Top app bar with a "Genni" title and a menu icon to open the navigation drawer. ☰
    * "Welcome Back \[User Name]!" message and Genni's logo. 👋
    * Four `WorkoutBox` components for selecting workout parameters: "Muscle Groups", "Sets & Reps", "Equipment", "Duration". Tapping these opens a bottom sheet.
    * A large, circular `PlayButton` to generate and start the workout. 🚀
* **Logic (via `HomeViewModel`, `AuthViewModel`, `WorkoutViewModel`):**
    * **Navigation Drawer (`ModalNavigationDrawer`)**: Allows access to other app sections (Health Calculations, Breathing Exercises, Profile, Settings, About). 🗺️
    * **Bottom Sheets (`ModalBottomSheet`)**: When a `WorkoutBox` is tapped, a bottom sheet appears with specific options:
        * **`MuscleGroupSelector`**: Checkboxes for selecting target muscle groups (e.g., Chest, Back, Legs). ☑️
        * **`SetsRepsInput`**: Allows choosing a training goal (Hypertrophy, Strength, Endurance) which auto-suggests rep ranges, or manually inputting sets and reps. Includes sliders for rest times. 🔢⏳
        * **`EquipmentSelector`**: Checkboxes for selecting available equipment (e.g., Bodyweight, Dumbbells, Barbell). ☑️
        * **`DurationSelector`**: A slider to select the desired workout duration in minutes. ⏰
    * **Workout Generation**: The large `PlayButton` calls `homeViewModel.generateWorkout()`, which uses the selected parameters to create a workout plan. It then navigates to `GeneratedWorkoutScreen`.

#### 3.2.2. `GeneratedWorkoutScreen.kt` - Your Custom Plan! 📋

This screen displays the workout plan that was just generated based on the user's selections.

* **UI Elements:**
    * "Generated Workout" title.
    * A list of `WorkoutCard` components, each detailing an exercise with its image, name, sets, reps, rest time, muscles worked, and equipment used. 🏋️‍♀️
    * A large `PlayButton` to start the workout simulation. 🚀
    * **"Save This Workout" Button:** Opens a bottom sheet to name and save the workout. 💾
* **Logic (via `WorkoutViewModel`, `AuthViewModel`):**
    * Collects the generated `workouts` list from `WorkoutViewModel`.
    * **Save Workout Functionality:**
        * When "Save This Workout" is clicked, a `ModalBottomSheet` appears asking for a workout name.
        * The "Save" button calls `workoutViewModel.saveCurrentWorkout()`, passing the entered name and the current user's ID.
        * Displays `Toast` messages for success or error. 🎉🚫

#### 3.2.3. `WorkoutSimulatorScreen.kt` - Time to Sweat! 🏃‍♂️

This is the interactive screen where users perform their generated workout.

* **UI Elements:**
    * **Linear Progress Bar:** Shows the overall progress of the workout with a smooth animation. 📈
    * Current exercise image. 🖼️
    * Exercise name, current set/total sets, reps.
    * Rest time for the current exercise. ⏳
    * A large countdown timer for the current phase (exercise or rest). ⏱️
    * **Control Buttons:**
        * "Skip Set": Jumps to the next set. ⏭️
        * "Skip Exercise": Moves to the next exercise. ⏩
        * "Pause/Resume": Toggles the workout's running state, changing button text and color. ⏸️▶️
        * "Start": Appears if the workout hasn't started yet, to begin the session. 🏁
* **Logic (via `WorkoutViewModel`):**
    * Observes `currentExerciseIndex`, `currentSet`, `timeLeft`, `isPaused`, and `progress` from `WorkoutViewModel` to update the UI in real-time. 📊
    * Calls `viewModel.skipSet()`, `viewModel.skipExercise()`, `viewModel.pauseWorkout()`, `viewModel.unpauseWorkout()`, and `viewModel.startWorkout()` based on button clicks.
    * When all exercises are completed, `onWorkoutCompleted()` is called to navigate away.

#### 3.2.4. `SavedWorkoutsScreen.kt` - Your Workout History 💾

This screen displays all the workouts a user has previously saved.

* **UI Elements:**
    * "Saved Workouts" title.
    * A `LazyColumn` (efficient list) of `AnimatedSavedWorkoutItem` components.
    * If no workouts are saved, a message "No saved workouts yet. Start generating and save some!" is displayed. 💬
* **Logic (via `WorkoutViewModel`, `AuthViewModel`):**
    * Uses `LaunchedEffect` to automatically load saved workouts for the current user when the screen is opened or the user changes. 🚀
    * Each `AnimatedSavedWorkoutItem` is clickable. Tapping it loads that specific workout into `WorkoutViewModel` and navigates to `GeneratedWorkoutScreen` so the user can review or start it again.

#### 3.2.5. `UploadWorkoutScreen.kt` - (Currently Unused) ⬆️

* As mentioned, this screen is currently commented out. Its original purpose was likely for an admin or developer to upload default workout data to the backend. 🚧

### 3.3. Health & Wellness

#### 3.3.1. `HealthStatusScreen.kt` - Your Health Snapshot ❤️

This screen displays calculated health metrics for the user.

* **UI Elements:**
    * A "Health Calculations" title with a health icon. ⚕️
    * `InfoRow` components showing the user's input data: Weight, Height, Age.
    * `HorizontalDivider` for separation.
    * `Text` components displaying calculated values: BMI, Body Fat Percentage (BFP), Basal Metabolic Rate (BMR), Total Daily Energy Expenditure (TDEE), Protein Intake, and Water Intake. These results are color-coded (e.g., green for healthy, red for concerns). 🟢🔴
    * "What do these results mean?" `TextButton` (navigates to `HCExplanationScreen`).
    * "Would you like some food recommendations" `TextButton` (navigates to `FoodRecommScreen`).
* **Logic (via `HCViewModel`, `AuthViewModel`):**
    * Uses `LaunchedEffect` to automatically populate the `HCViewModel` with the `currentUser`'s data (weight, height, age) and then triggers the `hcViewModel.calculate()` function to compute the metrics. 🧠
    * The results are observed from `HCViewModel`'s `StateFlow`s.

#### 3.3.2. `HealthMetricsExplanationScreen.kt` - Understanding Your Health 💡

This screen provides detailed explanations for each health metric.

* **UI Elements:**
    * "Health Metric Explanations" title.
    * A `LazyColumn` containing multiple `MetricExplanationCard` components.
    * A "Personalized Recommendations" section with a `RecommendationCard`.
* **Logic:**
    * Retrieves calculated metric values (BMI, BFP, BMR, TDEE, Protein Intake, Water Intake) from the `NavController`'s `savedStateHandle`, which were passed from `HealthStatusScreen`.
    * Each `MetricExplanationCard` provides a detailed text explanation for a specific metric (e.g., what BMI is, how BMR works). 📖
    * The `RecommendationCard` summarizes personalized tips based on the user's calculated BMI, BFP, and other metrics. It even suggests protein distribution across meals and water intake breakdown! 🎯💧

#### 3.3.3. `FoodRecommScreen.kt` - What to Eat! 🍎

This screen provides food recommendations based on the user's health metrics and goals.

* **UI Elements:**
    * "Recommended Foods for You" title.
    * "Goal: \[Category]" (e.g., "Muscle Gain", "Fat Loss", "Overall Wellness") and target calorie/protein intake.
    * `FoodCategoryCardWithNutrition` components displaying lists of recommended foods for the main goal and for "Healthy Snacks." 🥗
    * (Commented out) A "Generate Food PDF" button, indicating a future feature to export recommendations. 📄
* **Logic (via `HCViewModel`, `AuthViewModel`):**
    * Collects calculated metrics (`bmi`, `bfp`, `tdee`, `proteinIntake`) from `HCViewModel`.
    * Determines the `recommendedCategory` (Muscle Gain, Fat Loss, Overall Wellness) based on BMI and BFP.
    * Displays a curated list of `FoodItem`s (name, calories, protein) for the recommended category and for general healthy snacks.

#### 3.3.4. `BreathingExercisesScreen.kt` - Calm Your Mind 🌬️

This screen allows users to generate and view breathing exercise sessions.

* **UI Elements:**
    * "Generated Breathing Exercise Session" title.
    * **Duration Selector:** A `DropdownMenuDurationSelector` to choose the session length (5-10 minutes). ⏰
    * A list of `BreathingExerciseCard` components, each detailing an exercise with its inhale, hold, and exhale times. 🧘‍♀️
    * A large, circular `StartButton` to begin the breathing session. ▶️
* **Logic (via `BEViewModel`):**
    * Uses `LaunchedEffect` to `generateBreathingExercises()` whenever the `selectedDuration` changes.
    * Displays the generated exercises from `beViewModel.breathingExercises`.
    * The `StartButton` navigates to `BESimulatorScreen`.

#### 3.3.5. `BESimulatorScreen.kt` - Breathe In, Breathe Out 🧘‍♀️

This screen provides an interactive guided breathing exercise experience.

* **UI Elements:**
    * "Breathing Exercise \[Number]" title.
    * Current phase name (e.g., "Inhale", "Hold", "Exhale").
    * A large countdown timer for the current phase. ⏱️
    * "Pause/Resume" button (changes color based on state). ⏸️▶️
    * "Start" button (to begin the session). 🏁
* **Logic (via `BEViewModel`):**
    * Collects `breathingExercises`, `currentExerciseIndex`, `timeLeft`, `currentPhase`, and `isPaused` from `BEViewModel`.
    * If no exercises are loaded, it shows a `CircularProgressIndicator`.
    * The "Start" button calls `beViewModel.startBreathingSession()`.
    * The "Pause/Resume" button calls `beViewModel.pause()` or `beViewModel.resume()`.
    * When the session is completed, `onBreathingSessionCompleted()` is called.

### 3.4. `AboutScreen.kt` - Learn About Genni ℹ️

This screen provides information about the Genni application.

* **UI Elements:**
    * Header with Genni's logo, name, and tagline. 🖼️
    * A detailed description of the app's purpose.
    * A list of key features (e.g., Personalized Workouts, Health Metrics, Guided Breathing). 🏋️‍♂️🧮🧘‍♀️
    * Credits and version information.
* **Styling:** Uses a gradient background and transparent rounded boxes for sections, giving it a modern and clean look. ✨

---

## 4. 🧠 ViewModels (The App's Brains)

ViewModels are crucial for separating UI logic from the UI itself. They hold and manage data, making sure it survives screen rotations and other configuration changes.

### 4.1. `AuthViewModel.kt` - User Authentication 🔐

* **Purpose:** Handles user login, logout, and manages the current authenticated user's state.
* **Key States:**
    * `username`, `password`: Input fields for login.
    * `authState`: Tracks the authentication process (`Loading`, `Success`, `Error`).
    * `currentUser`: Stores the currently logged-in `User` object.
* **Key Functions:**
    * `authenticateUser()`: Attempts to log in a user using provided credentials.
    * `logout()`: Signs the user out.

### 4.2. `UserViewModel.kt` - User Data Management 👤

* **Purpose:** Manages user registration and authentication details.
* **Key Functions:**
    * `registerUser()`: Adds a new user to the database (likely Firestore, given the code). It generates a `userID` and saves the user's data.
    * `authenticateUser()`: Verifies user credentials against the database.

### 4.3. `HomeViewModel.kt` - Workout Customization Logic 🏠

* **Purpose:** Manages the user's selections for workout generation.
* **Key States:**
    * `selectedWorkout`: Which workout type is selected.
    * `selectedMuscleGroups`: List of chosen muscle groups.
    * `sets`, `reps`, `restTimePerExercise`, `restTimePerSet`: Workout intensity parameters.
    * `selectedEquipment`: List of chosen equipment.
    * `duration`: Desired workout duration.
* **Key Functions:**
    * `selectWorkout()`: Updates the selected workout type.
    * `setMuscleGroups()`, `setSetsAndReps()`, `setEquipment()`, `setDuration()`: Update the respective workout parameters based on user input.
    * `generateWorkout()`: Uses the selected parameters to generate a workout plan (by interacting with `WorkoutViewModel`).

### 4.4. `WorkoutViewModel.kt` - Workout Engine 🏋️‍♀️

* **Purpose:** Manages the generation, execution, and saving of workout routines.
* **Key States:**
    * `allWorkouts`: A master list of all available exercises (fetched from Firestore).
    * `workouts`: The currently generated workout plan.
    * `currentGeneratedWorkout`: The list of workouts that were just generated.
    * `savedWorkouts`: A list of workouts saved by the user.
    * `currentExerciseIndex`, `currentSet`, `currentState`, `timeLeft`, `isPaused`, `progress`: Real-time states for the workout simulator.
* **Key Functions:**
    * `fetchWorkoutsFromFirestore()`: Loads all available exercises from your database. ☁️
    * `getImageResId()`: A helper to get the correct image resource ID for an exercise based on its name.
    * `filterExercisesBy()`: Filters exercises based on muscle groups and equipment.
    * `generateWorkouts()`: Creates a personalized workout routine based on user input.
    * `startWorkout()`: Initiates the workout simulation, managing timers and states. ⏱️
    * `skipSet()`, `skipExercise()`, `pauseWorkout()`, `unpauseWorkout()`: Control functions for the simulator.
    * `saveCurrentWorkout()`: Saves the currently generated workout to the user's saved workouts in Firestore. 💾
    * `loadSavedWorkouts()`: Fetches all saved workouts for the current user.
    * `setCurrentWorkout()`, `loadWorkout()`: Functions to set or load a specific workout.
    * `deleteSavedWorkout()`: Removes a saved workout.

### 4.5. `HCViewModel.kt` - Health Calculations 🧠

* **Purpose:** Performs various health calculations (BMI, BMR, BFP, TDEE, protein/water intake) and manages their input/output states.
* **Key States:**
    * `weightInput`, `heightInput`, `muscleMassPercentageInput`, `ageInput`, `genderInput`, `activityLevelInput`: Input fields for calculations.
    * `bmi`, `ffmi`, `muscleMass`, `bmr`, `bfp`, `tdee`, `proteinIntake`, `waterIntake`: Calculated results.
    * `calculated`: A boolean to indicate if calculations have been performed.
* **Key Functions:**
    * `onWeightChange()`, `onHeightChange()`, etc.: Update input states.
    * `calculate()`: Performs all the health metric calculations based on the input data. This is where the magic happens! ✨

### 4.6. `BEViewModel.kt` - Breathing Exercises 🌬️

* **Purpose:** Manages the generation and simulation of breathing exercises.
* **Key States:**
    * `breathingExercises`: The list of exercises for the current session.
    * `currentExerciseIndex`, `timeLeft`, `currentPhase`, `isPaused`: Real-time states for the simulator.
* **Key Functions:**
    * `generateBreathingExercises()`: Creates a sequence of breathing exercises based on a desired duration.
    * `startBreathingSession()`: Initiates the breathing exercise timer and phases. ⏱️
    * `pause()`, `resume()`: Control functions for the breathing session.

### 4.7. `ForgetPasswordViewModel.kt` - Password Reset Logic 🔑

* **Purpose:** Handles the logic for the "Forgot Password" flow.
* **Key States:**
    * `email`: Input for the email to reset.
    * `uiState`: Tracks the state of the reset process (`Loading`, `Success`, `Error`).
    * `navigateToResetPage`: A flag to trigger navigation to the next reset step.
* **Key Functions:**
    * `resetPassword()`: Initiates the password reset (e.g., sends an email).
    * `updatePassword()`: Sets the new password after verification.
    * `onEmailChange()`, `onNavigateHandled()`: Helper functions for state management.

### 4.8. `AppSettingsViewModel.kt` - App Preferences ⚙️

* **Purpose:** Manages application-wide settings and preferences.
* **Key States:**
    * `isDarkTheme`: Controls light/dark mode. 🌙
    * `notificationsEnabled`: For enabling/disabling notifications. 🔔
    * `prefersMetric`: For unit preferences (metric/imperial). 📏
    * `autoStartWorkout`: For auto-starting workouts. ▶️
* **Key Functions:**
    * `toggleDarkTheme()`, `toggleNotifications()`, `toggleMetric()`, `toggleAutoStart()`: Functions to change these settings.

### 4.9. `AdminViewModel.kt` - Admin Functions 👑

* **Purpose:** Manages administrative functionalities (though less code is provided for this).
* **Key States:**
    * `username`, `password`: Admin login credentials.
    * `authState`: Authentication state for admin login.
* **Key Functions:**
    * `authenticateAdmin()`: Logs in an admin.
    * `registerAdmin()`: Registers a new admin.
    * `uploadWorkoutsToFirebase()`: (Likely) uploads workout data as an admin.

---

## 5. 📦 Data Models & States

These are the blueprints for the data your app uses and the different states your UI can be in.

### 5.1. Data Models (`models` package) 📦

* **`User`**: Represents a user, holding details like `userID`, `firstName`, `lastName`, `age`, `gender`, `goals`, `yearsOfTraining`, `username`, `password`, `email`, `weight`, `height`, `profilePic` (URI as string), and `foodRecommendations`. 🧑‍💻
* **`Workout`**: Represents a single exercise, with properties like `index`, `name`, `muscleGroupWorked`, `sets`, `reps`, `restTime`, `imageResID`, and `equipmentUsed`. 🏋️‍♀️
* **`WorkoutDTO`**: A Data Transfer Object for `Workout`, likely used for fetching data from Firestore.
* **`SavedWorkout`**: Represents a workout plan saved by the user, including an `id`, `name`, `timestamp`, and a list of `Workout` objects. 💾
* **`BreathingExercise`**: Represents a single breathing exercise, with `inhaleTime`, `holdTime`, and `exhaleTime`. 🌬️
* **`FoodItem`**: Represents a food item with `name`, `calories`, and `protein` content. 🍎

### 5.2. UI States (`states` package) 🚦

These are `sealed classes` that define the different possible states for asynchronous operations (like authentication or password reset), making it easy for your UI to react accordingly.

* **`AuthState`**: Defines states for authentication (e.g., `Loading`, `Success`, `Error`). 🔄
* **`ResetState`**: Defines states for password reset (e.g., `Loading`, `Success`, `Error`). 🔑
* **`WorkoutState`**: Defines phases of a workout (e.g., `Exercise`, `Rest`, `Completed`). 🏃‍♂️
* **`BreathingPhase`**: Defines phases of a breathing exercise (e.g., `Inhale`, `Hold`, `Exhale`). 🧘‍♀️

---
