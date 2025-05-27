---

### 1. `WorkoutViewModel.kt` 💪📊

This ViewModel is like your personal fitness trainer and record keeper! It manages everything related to workouts, from fetching exercises to tracking your progress and saving your achievements.

* **What it holds:**
    * `_allWorkouts` and `allWorkouts`: A list of *all* the exercises available in your app, fetched from Firestore. Think of this as your gym's complete exercise catalog! 📚
    * `_workouts` and `workouts`: The specific list of exercises for the *current* workout session. This is your personalized workout plan for the day! 🏋️‍♀️
    * `currentGeneratedWorkout`: Stores the workout plan that was just created for you. Ready to go! 🏃‍♀️
    * `_savedWorkouts` and `savedWorkouts`: A treasure chest of all the workouts you've saved. You can always go back and do your favorite routines! 💾❤️
    * `progress`: Shows how far along you are in your current workout, like a little progress bar! 🟩
    * `currentExerciseIndex`, `currentSet`, `currentState`, `timeLeft`: These keep track of exactly where you are in your workout, what exercise you're on, what set, if you're exercising or resting, and how much time is left. Super precise! ⏱️
    * `_isPaused` and `isPaused`: Tells you if your workout timer is currently paused. Need a water break? ⏸️💧
    * `isWorkoutRunning`: A simple switch to know if a workout is currently active. 🚦

* **What it does (its superpowers!):**
    * `WorkspaceWorkoutsFromFirestore()`: Goes to your online database (Firestore) and grabs all the possible exercises. It then turns them into `Workout` objects for your app to use, and even finds the right image for each exercise! 🖼️☁️
    * `getImageResId(imageName: String)`: This clever function takes an image name (like "plank") and finds the correct image file in your app's resources. No more guessing game for images! 🔍🖼️
    * `filterExercisesBy(muscles: List<String>, equipment: List<String>)`: Helps you find exercises that target specific muscles (like "biceps" 💪) or use certain equipment (like "dumbbells" 🏋️). If it can't find anything, it just gives you a random mix! 🎯
    * `generateWorkouts(muscles, sets, reps, equipment, duration)`: This is where the magic happens! ✨ You tell it what muscles you want to work, how many sets and reps, what equipment you have, and how long you want to exercise. It then creates a custom workout just for you! 🧑‍💻➡️📝
    * `startWorkout(onFinished: () -> Unit)`: Kicks off your workout timer! It handles counting down exercise time, rest time, and moving through sets and exercises. It even pauses when you need it to! 🚀
    * `updateProgress()`: Keeps your progress bar updated so you know how well you're doing. 📈
    * `skipSet()` and `skipExercise()`: Feeling too tired or just want to move on? These functions let you skip to the next set or even the next entire exercise. ⏩
    * `pauseWorkout()` and `unpauseWorkout()`: Easy controls to pause and resume your workout. ⏯️
    * `saveCurrentWorkout(...)`: Lets you save the awesome workout you just generated so you can do it again later! It sends the workout details to Firestore. ☁️✍️
    * `toMap()`: A helper function to turn a `Workout` object into a `Map` so it can be easily saved in Firestore. 🗺️🔄
    * `toWorkout()`: The opposite of `toMap()`, this converts data from Firestore back into a `Workout` object. 🔄🏋️‍♀️
    * `loadSavedWorkouts(...)`: Fetches all your previously saved workouts from Firestore. So convenient! 📥
    * `setCurrentWorkout(newWorkout: List<Workout>)` and `loadWorkout(savedWorkout: SavedWorkout)`: These help you put a saved workout into the current workout plan. 🔄📋
    * `deleteSavedWorkout(...)`: If you no longer need a saved workout, you can delete it. 👋🗑️

---

### 2. `HomeViewModel.kt` 🏠⚙️

This ViewModel is in charge of all the choices you make on the app's home screen when you're setting up your workout.

* **What it holds:**
    * `_selectedWorkout` and `selectedWorkout`: If you select a specific type of workout (though this seems less used currently based on the code). 🎯
    * `_selectedMuscleGroups` and `selectedMuscleGroups`: A list of the muscle groups you've chosen to target. 💪🧐
    * `_sets`, `_reps`, `_restTimePerExercise`, `_restTimePerSet`: The numbers you pick for how many sets, reps, and rest times you want. 🔢
    * `_selectedEquipment` and `selectedEquipment`: What exercise equipment you have available. 🛠️
    * `_duration` and `duration`: How long you want your workout to be in minutes. ⏰

* **What it does (its superpowers!):**
    * `selectWorkout(workout: String, context: Context)`: If you click on a pre-defined workout type, this stores it and shows a little message. (Currently, the main focus seems to be on custom generation). 👍
    * `setMuscleGroups(muscleGroups: List<String>)`: Updates the muscle groups you've selected. 📝
    * `setSetsAndReps(...)`: Stores the sets, reps, and rest times you've chosen. 📝
    * `setEquipment(equipment: List<String>)`: Records the equipment you'll be using. 📝
    * `setDuration(minutes: Int)`: Saves how long you want your workout to be. 📝
    * `generateWorkout(nc: NavController, context: Context, workoutViewModel: WorkoutViewModel)`: This is the big one! It takes all your preferences (muscles, sets, reps, equipment, duration) and tells the `WorkoutViewModel` to create a new workout plan. Then, it navigates you to the screen where you can see your newly generated workout! 🎉➡️📺

---

### 3. `HCViewModel.kt` 📏🍎

"HC" probably stands for "Health Calculator"! This ViewModel is your personal health analyst, taking your measurements and calculating important health metrics.

* **What it holds:**
    * `_weightInput`, `_heightInput`, `_muscleMassPercentageInput`, `_ageInput`, `_genderInput`, `_activityLevelInput`: These are the raw numbers you type into the calculator for your weight, height, muscle mass, age, gender, and how active you are. 📥
    * `_bmi`, `_ffmi`, `_muscleMass`, `_bmr`, `_bfp`, `_tdee`, `_proteinIntake`, `_waterIntake`: These are the *results* of the calculations – your Body Mass Index, Fat-Free Mass Index, muscle mass, Basal Metabolic Rate, Body Fat Percentage, Total Daily Energy Expenditure, recommended protein intake, and daily water intake. So much info! 📈💧
    * `_calculated`: A flag to know if the calculations have already been performed. ✅

* **What it does (its superpowers!):**
    * `onWeightChange(...)`, `onHeightChange(...)`, etc.: These are simple functions that update the input fields as you type your information. ✍️
    * `calculate()`: This is the brainy part! 🧠 It takes all your input values and performs a series of mathematical formulas to figure out your BMI, BMR, BFP, TDEE, muscle mass, protein intake range, and water intake. It's like having a nutritionist and fitness expert in your pocket! 📐🔢🍎

---

### 4. `BEViewModel.kt` 🌬️🧘‍♀️

"BE" stands for "Breathing Exercises"! This ViewModel guides you through calming and beneficial breathing exercises.

* **What it holds:**
    * `_breathingExercises` and `breathingExercises`: A list of `BreathingExercise` objects, each defining how long to inhale, hold, and exhale. Like a custom breathing routine! 🌬️
    * `_currentExerciseIndex` and `currentExerciseIndex`: Which breathing exercise you're currently on in the sequence. 👆
    * `_timeLeft` and `timeLeft`: The countdown for the current breathing phase (inhale, hold, or exhale). ⏳
    * `_currentPhase` and `currentPhase`: Tells you if you should be inhaling, holding your breath, or exhaling. Very helpful! 🌬️↔️💨
    * `_isPaused` and `isPaused`: Lets you know if the breathing session is paused. Take a break if you need! ⏸️
    * `isRunning`: A simple flag to know if a breathing session is active. 🚦

* **What it does (its superpowers!):**
    * `generateBreathingExercises(durationMinutes: Int = 5)`: Creates a sequence of breathing exercises for you. You tell it how long you want to breathe for, and it generates a routine with random inhale, hold, and exhale times that fit within that duration. 🧘‍♀️✨
    * `startBreathingSession(onFinished: () -> Unit)`: Begins the breathing session! It will guide you through each phase (inhale, hold, exhale) and countdown the time for each. When it's all done, it lets the app know. 🚀🎶
    * `runCurrentExercise(onFinished: () -> Unit)`: This is the internal engine that powers the breathing session, handling the timing for each phase. ⚙️
    * `pause()` and `resume()`: Allows you to pause and resume your breathing exercise session. Very calming! ⏯️😌

---

### 5. `ForgetPasswordViewModel.kt` 🔑📧

This ViewModel helps users who have forgotten their password. It's focused on guiding them through the password reset process.

* **What it holds:**
    * `email`: The email address the user enters to reset their password. 📧
    * `_uiState` and `uiState`: This is super important! It tells the screen what's happening: `Idle` (doing nothing), `Loading` (waiting for a response), or `Error` (something went wrong). It's like a status report! 🚦
    * `_navigateToResetPage` and `MapsToResetPage`: A signal to tell the app to move the user to the actual password reset page once their email is confirmed. ➡️📄

* **What it does (its superpowers!):**
    * `onEmailChange(newEmail: String)`: Updates the email field as the user types. ✍️
    * `resetPassword()`: This is the main action. It checks if the email is in your "Users" database. If it is, it signals to navigate to the reset page. If not, or if there's an error, it updates the `uiState` so the user knows. It uses `viewModelScope.launch` because it's talking to the database over the internet, which takes a moment. 🌐⏳
    * `updatePassword(newPassword: String, onResult: (Boolean) -> Unit)`: Once on the reset page, this function actually changes the user's password in the database. It lets the screen know if it was successful or not. 🔐✅❌
    * `onNavigateHandled()`: Resets the navigation flag once the navigation has happened. Important for preventing accidental multiple navigations. 🔄

---

### 6. `UserViewModel.kt` 🧑‍🤝‍🧑🔐

This ViewModel is all about user management: registering new users and authenticating existing ones.

* **What it holds:**
    * It doesn't hold much state itself, but rather interacts with the Firebase Firestore database to manage user data. ☁️

* **What it does (its superpowers!):**
    * `registerUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit)`: This is how new users sign up! It automatically generates a unique `userID` for the new user by looking at the last existing user's ID and adding one. Then, it saves the new user's details (username, password, etc.) to the "Users" collection in Firestore. It tells the app if the registration was a success or if there was an error. 🆕🆔✍️
    * `authenticateUser(username: String, password: String, callBack: (User?) -> Unit)`: This is for logging in! It checks if the provided username and password match a user in your Firestore "Users" collection. If a match is found, it returns the `User` object; otherwise, it returns `null`. 🕵️‍♀️✅❌

---

### 7. `AdminViewModel.kt` 👑🔐

Similar to the `UserViewModel` but specifically for managing *admin* users.

* **What it holds:**
    * `username` and `password`: The input fields for admin login. ✍️
    * `_currentAdmin` and `admin`: Stores the information of the admin who is currently logged in. 👑👤
    * `_authState` and `authState`: Just like in the `ForgetPasswordViewModel`, this tells the screen the current state of the login process (Idle, Loading, Error, Success). 🚦

* **What it does (its superpowers!):**
    * `onAdminUsernameChange(newUsername: String)` and `onAdminPasswordChange(newPassword: String)`: Updates the input fields as the admin types their credentials. ✍️
    * `registerAdmin(admin: Admin, onSuccess: () -> Unit, onFailure: (String) -> Unit)`: Allows new admin accounts to be created. It generates a unique `adminID` and saves the admin details to the "Admins" collection in Firestore. 🆕🆔✍️
    * `checkAdminInDB(username: String, password: String, callBack: (Admin?) -> Unit)`: This is the core logic for verifying admin credentials against the Firestore database. 🕵️‍♀️
    * `authenticateAdmin(username: String, password: String, onLoginSuccess: () -> Unit)`: This function performs the actual login attempt for admins. It first checks if the input fields are empty. Then, it uses `checkAdminInDB` to verify the credentials. If successful, it stores the logged-in admin and updates the `authState` to `Success`, then calls `onLoginSuccess`. If not, it sets the `authState` to `Error`. 🚀✅❌

---

### 8. `AuthViewModel.kt` 🤝🔐

This ViewModel handles the general authentication process for regular users (login and logout). It works closely with `UserViewModel`.

* **What it holds:**
    * `username` and `password`: The text entered by the user for login. ✍️
    * `_authState` and `authState`: Shows the current status of the authentication process (Idle, Error, Success). 🚦
    * `_currentUser` and `currentUser`: Stores the information of the user who is currently logged in. 👤

* **What it does (its superpowers!):**
    * `onUsernameChange(newUsername: String)` and `onPasswordChange(newPassword: String)`: Updates the input fields as the user types. ✍️
    * `authenticateUser(userViewModel: UserViewModel, onLoginSuccess: () -> Unit)`: This is the main login function. It checks if the fields are empty. Then, it calls `authenticateUser` from the `UserViewModel` (which talks to the database). If the login is successful, it saves the `currentUser` and updates `authState` to `Success`. If not, it sets `authState` to `Error`. 🚀✅❌
    * `logout(onLogout: () -> Unit)`: Helps a user log out! It clears the current user's information, resets the input fields, and sets the `authState` back to `Idle`. 👋🚪

---

### 9. `AppSettingsViewModel.kt` ⚙️🌓🔔

This ViewModel manages various settings for the application, allowing users to customize their experience.

* **What it holds:**
    * `isDarkTheme`: A boolean (true/false) indicating if dark theme is enabled. 🌙
    * `notificationsEnabled`: A boolean indicating if notifications are turned on. 🔔
    * `prefersMetric`: A boolean indicating if the user prefers metric units (vs. imperial). 📏
    * `autoStartWorkout`: A boolean indicating if workouts should start automatically. 🚀

* **What it does (its superpowers!):**
    * `toggleDarkTheme()`: Flips the switch for dark mode! If it's on, it turns off, and vice-versa. 🌓🔄
    * `toggleNotifications()`: Turns notifications on or off. 🔔🔄
    * (Currently, there are no explicit toggle functions for `prefersMetric` or `autoStartWorkout`, but these would likely follow a similar pattern if they were to be user-controllable from settings.)

---

These ViewModels work together to make the Genii app functional and user-friendly, handling all the complex logic behind the scenes so the UI can remain simple and responsive! 🚀✨
