### 1. `SavedWorkout.kt` 💾💪

This is the blueprint for a workout plan that a user has decided to keep for later! It's like a special diary entry for your favorite exercise routine.

* **What it is:** A `data class` that represents a saved workout. 📝
* **What it holds:**
    * `id`: A unique identifier for *this specific saved workout*. Imagine it as the serial number for your saved workout plan. 🏷️
    * `name`: The name the user gave to this saved workout (e.g., "Monday Morning Burn" 🔥).
    * `timestamp`: When this workout was saved. Super useful for sorting or seeing your history! ⏰
    * `workouts`: This is a `List` of actual `Workout` objects (we'll see that next!). So, one `SavedWorkout` can contain many individual exercises. It's like a whole playlist of moves! 🏋️‍♀️🤸‍♂️🧘‍♀️

---

### 2. `FoodItem.kt` 🍎🥕🍔

This is the blueprint for a single food item and its nutritional info. It's like a digital nutrition label for each meal or snack!

* **What it is:** A `data class` for representing a food item. 🍽️
* **What it holds:**
    * `name`: The name of the food (e.g., "Apple" 🍏, "Chicken Breast" 🍗).
    * `calories`: How many calories are in this food item. ⚡
    * `protein`: How many grams of protein are in this food item. 💪

---

### 3. `WorkoutDTO.kt` 📥🏋️‍♀️ (DTO = Data Transfer Object)

This blueprint is a bit special! "DTO" usually means "Data Transfer Object." It's a structure designed for sending or receiving workout information, especially when talking to a database like Firestore. It's like a standardized form for sharing workout details.

* **What it is:** A `data class` used to transfer workout information, likely between the app and a database. 🌐
* **What it holds:**
    * `name`: The name of the exercise (e.g., "Push-ups").
    * `muscleGroupWorked`: A list of muscles this exercise targets (e.g., `["Chest", "Triceps"]` 💪).
    * `sets`: How many sets for this exercise. 🔢
    * `reps`: How many repetitions per set. 🔄
    * `restTime`: How long to rest after a set (in seconds). 😴
    * `imageName`: The *name* of the image file for this exercise (e.g., "push_up_image.png"). This is clever because you store the *name*, not the actual image, making it easy to fetch from storage! 🖼️
    * `equipmentUsed`: A list of equipment needed (e.g., `["Dumbbells"]` 🛠️).

* **Why `DTO`?** Notice how all its properties have default values (like `""` or `0`). This is super common when working with databases like Firestore. It makes it easier for Firestore to convert data from its format into your `WorkoutDTO` object, even if some fields are missing! It's very flexible. 🧘‍♀️

---

### 4. `Workout.kt` 🏋️‍♀️📝

This is the core blueprint for a single exercise that's part of a workout plan. It's like a single instruction card in your workout deck.

* **What it is:** The main `data class` representing an individual workout exercise. 🏃‍♀️
* **What it holds:**
    * `index`: Its position in a list of exercises (e.g., "1st exercise," "2nd exercise"). 🔢
    * `name`: The name of the exercise (e.g., "Squats").
    * `muscleGroupWorked`: What muscles it targets. 💪
    * `sets`: Number of sets.
    * `reps`: Number of repetitions.
    * `restTime`: Rest time after a set. 😴
    * `imageResID`: This is the *actual resource ID* of the image for the exercise (like `R.drawable.squats`). This is used when the app needs to display the image directly on the screen. 📸
    * `progress`: How much of this specific exercise has been completed (e.g., for a progress bar during the workout). It starts at `0f` (0%). 📈
    * `equipmentUsed`: What equipment is needed for this exercise. 🛠️

* **Difference from `WorkoutDTO`:** `WorkoutDTO` is for *transferring* data (often from a database with just an `imageName`), while `Workout` is what your app *uses* internally, with a direct `imageResID` for displaying images. It's like `DTO` is the raw ingredient list, and `Workout` is the prepared meal! 🍳➡️🍲

---

### 5. `User.kt` 🧑‍🤝‍🧑👤

This is the main blueprint for all the important information about a user in your app. It's like a user's ID card and profile combined!

* **What it is:** A `data class` that stores all the details about a user. 🆔
* **What it holds:**
    * `userID`: A unique number for each user. 🔢
    * `firstName`, `middleName`, `lastName`: Their full name. 📇
    * `age`: How old they are. 🎂
    * `gender`: Their gender. 🚻
    * `goals`: A list of their fitness goals (e.g., `["Lose Weight", "Build Muscle"]` 🎯).
    * `yearsOfTraining`: How long they've been training. 🏋️‍♀️
    * `username`, `password`, `email`: Their login credentials. 🔑📧
    * `profilePic`: A link or name for their profile picture (optional). 📸
    * `foodRecommendations`: A list of food recommendations they might have. 🍎🥦
    * `weight`, `height`: Their current weight and height. ⚖️📏
* **`constructor()`:** You see a special `constructor()` with no arguments. This is a common trick in Kotlin for Firebase Firestore! Firebase sometimes needs an empty constructor to create `User` objects from the data it fetches from the database. It's like giving Firebase a blank form to fill in. 📄✨

---

### 6. `Admin.kt` 👑🔐

This is the blueprint for an administrator user, like someone who manages the app's content or users.

* **What it is:** A `data class` representing an administrator account. 🧑‍💻
* **What it holds:**
    * `adminID`: A unique number for each admin. 🔢
    * `username`: The admin's login username. 📧
    * `password`: The admin's login password. 🔑
* **`constructor()`:** Similar to `User.kt`, this empty constructor helps Firebase Firestore create `Admin` objects from database data. 🛠️

---

### 7. `BreathingExercise.kt` 🌬️🧘‍♀️

This is the blueprint for a single step within a breathing exercise routine.

* **What it is:** A `data class` defining a specific breathing pattern. 🧘‍♀️
* **What it holds:**
    * `inhaleTime`: How long to inhale (in seconds). 🌬️
    * `holdTime`: How long to hold your breath (in seconds). 🤫
    * `exhaleTime`: How long to exhale (in seconds). 💨

---

These data classes are the foundational building blocks of your app's information! They ensure that when you talk about a "user" or a "workout," everyone (both humans and the code! 🤖) knows exactly what pieces of data are involved. 🧠 organising information in a neat and tidy way! 🗂️✨
