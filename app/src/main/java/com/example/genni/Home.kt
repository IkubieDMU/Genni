package com.example.genni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.viewmodels.HomeViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.WorkoutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nc: NavController, homeViewModel: HomeViewModel, authViewModel: AuthViewModel,workoutViewModel: WorkoutViewModel) {
    // Nav Drawer Variables
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentUser by authViewModel.currentUser.collectAsState()

    val context = LocalContext.current
    val workoutTitles = listOf("Muscle Groups", "Sets & Reps", "Equipment", "Duration")

    var selectedWorkout by remember { mutableStateOf<String?>(null) }
    // Bottom sheet variables
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSheet && selectedWorkout != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                selectedWorkout = null
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            when (selectedWorkout) {
                "Muscle Groups" -> MuscleGroupSelector { selected ->
                    homeViewModel.setMuscleGroups(selected)
                    showSheet = false
                }
                "Sets & Reps" -> SetsRepsInput { sets, reps ->
                    homeViewModel.setSetsAndReps(sets, reps)
                    showSheet = false
                }
                "Equipment" -> EquipmentSelector { selected ->
                    homeViewModel.setEquipment(selected)
                    showSheet = false
                }
                "Duration" -> DurationSelector { duration ->
                    homeViewModel.setDuration(duration)
                    showSheet = false
                }
            }
        }
    }

    val drawerItems = listOf(
        "Home" to Screens.HomeScreen.screen,
        "Health Calculations" to Screens.HealthCalculationsScreen.screen,
        "Breathing Exercises" to Screens.BreathingExercisesScreen.screen,
        "Profile" to Screens.ProfileScreen.screen,
        "Settings" to Screens.SettingsScreen.screen,
        "About" to Screens.AboutScreen.screen
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(drawerItems, nc, drawerState, scope,authViewModel) }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Genni", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = emeraldGreen)
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender))),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.genniappiconnb),
                        contentDescription = "Genni's Logo",
                        modifier = Modifier.size(70.dp)
                    )
                    Text("Welcome Back ${currentUser?.firstName ?: "User"}!", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Your AI-Powered Workout Partner", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(40.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        for (i in 0..1) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                for (j in 0..1) {
                                    val index = i * 2 + j
                                    WorkoutBox(workoutTitles[index]) {
                                        selectedWorkout = workoutTitles[index]
                                        showSheet = true
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(emeraldGreen.copy(alpha = 0.9f))
                            .clickable { homeViewModel.generateWorkout(nc, context, workoutViewModel) }
                            .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(50.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(drawerItems: List<Pair<String, String>>, nc: NavController, drawerState: DrawerState, scope: CoroutineScope, authViewModel: AuthViewModel) {
    // Smooth gradient background with a floating effect
    Box(
        modifier = Modifier.fillMaxHeight().width(250.dp)
            .background(Brush.verticalGradient(listOf(deepPurple, emeraldGreen)))
            .shadow(10.dp, RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val currentUser by authViewModel.currentUser.collectAsState()
            // Header Section with Profile Picture and Welcome Message
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // User Avatar (Placeholder)
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).border(2.dp, Color.White, CircleShape)) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(50.dp).align(Alignment.Center))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Welcome ${currentUser?.firstName ?: "User"}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Drawer Items
            drawerItems.forEachIndexed { index, (title, route) ->
                TextButton(
                    onClick = {
                        scope.launch { drawerState.close() }
                        nc.navigate(route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            if (index % 2 == 0) Color.White.copy(alpha = 0.1f)
                            else Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (title) {
                                "Home" -> Icons.Default.Home
                                "Health Calculations" -> Icons.Default.FitnessCenter
                                "Breathing Exercises" -> Icons.Default.Air
                                "Profile" -> Icons.Default.Person
                                "Settings" -> Icons.Default.Settings
                                "About" -> Icons.Default.Info
                                else -> Icons.Default.Menu
                            },
                            contentDescription = title,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(title, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))

            // Footer Section (Version Info)
            Text(
                text = "Version 8.8.0",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ModernBottomAppBar() {
    Box(
        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp).height(70.dp).clip(RoundedCornerShape(20.dp)).background(Brush.horizontalGradient(listOf(emeraldGreen.copy(alpha = 0.8f), deepPurple.copy(alpha = 0.8f))))
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            IconButton(onClick = { /* Home Click */ }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
            }
            IconButton(onClick = { /* Search Click */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { /* Profile Click */ }) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
            }
            IconButton(onClick = { /* Settings Click */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }
    }
}

// Supporting Composables
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

@Composable
fun MuscleGroupSelector(onConfirm: (List<String>) -> Unit) {
    val options = listOf("Chest", "Back", "Legs", "Arms", "Shoulders", "Core")
    val selected = remember { mutableStateListOf<String>() }

    Column(Modifier.padding(16.dp)) {
        Text("Select Muscle Groups", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        options.forEach { muscle ->
            Row(
                Modifier.fillMaxWidth().clickable {
                    if (selected.contains(muscle)) selected.remove(muscle)
                    else selected.add(muscle)
                }.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = muscle in selected, onCheckedChange = null)
                Text(muscle, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onConfirm(selected.toList()) }, modifier = Modifier.align(Alignment.End)) {
            Text("Confirm")
        }
    }
}

@Composable
fun SetsRepsInput(onConfirm: (Int, Int) -> Unit) {
    // State to track selected goal (Hypertrophy, Strength, Endurance)
    var selectedGoal by remember { mutableStateOf<String?>(null) }

    // States for min and max sets (used with training goals)
    var minSets by remember { mutableStateOf("") }
    var maxSets by remember { mutableStateOf("") }

    // States for manual sets and reps input
    var manualSets by remember { mutableStateOf("") }
    var manualReps by remember { mutableStateOf("") }

    // Map training goals to rep ranges
    val repRanges = mapOf(
        "Hypertrophy" to (8..12).toList(), // Hypertrophy (8-12 reps)
        "Strength" to (1..5).toList(),     // Strength (1-5 reps)
        "Endurance" to (15..30).filter { it % 2 == 0 } // Endurance (even reps only)
    )

    // Flags to check which input method is being used
    val usingManualInput = manualSets.isNotBlank() || manualReps.isNotBlank()
    val usingGoalSelection = selectedGoal != null

    Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Choose One Method", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // =============== GOAL SELECTION SECTION ===============
        Text("Option 1: Training Goal (Auto reps)", fontWeight = FontWeight.SemiBold)

        // Radio buttons for selecting training goal
        repRanges.keys.forEach { goal ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable(enabled = !usingManualInput) { selectedGoal = goal }.padding(vertical = 6.dp)
            ) {
                RadioButton(
                    selected = selectedGoal == goal,
                    onClick = { selectedGoal = goal },
                    enabled = !usingManualInput // disable if using manual input
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("$goal (${repRanges[goal]?.first()}-${repRanges[goal]?.last()} reps)")
            }
        }

        // Only show min/max sets if a goal is selected
        if (selectedGoal != null && !usingManualInput) {
            OutlinedTextField(
                value = minSets,
                onValueChange = { minSets = it.filter(Char::isDigit) },
                label = { Text("Min Sets") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = maxSets,
                onValueChange = { maxSets = it.filter(Char::isDigit) },
                label = { Text("Max Sets") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // =============== MANUAL INPUT SECTION ===============
        Text("Option 2: Manual Sets & Reps", fontWeight = FontWeight.SemiBold)

        // Manual Sets input
        OutlinedTextField(
            value = manualSets,
            onValueChange = {
                manualSets = it.filter(Char::isDigit)
                if (it.isNotBlank()) selectedGoal = null // disable goal selection
            },
            label = { Text("Sets") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            enabled = !usingGoalSelection // disable if goal selected
        )

        // Manual Reps input
        OutlinedTextField(
            value = manualReps,
            onValueChange = {
                manualReps = it.filter(Char::isDigit)
                if (it.isNotBlank()) selectedGoal = null // disable goal selection
            },
            label = { Text("Reps") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            enabled = !usingGoalSelection
        )

        Spacer(modifier = Modifier.height(20.dp))

        // =============== CONFIRM BUTTON ===============
        Button(
            onClick = {
                val sets: Int
                val reps: Int

                // If user selected a goal, randomly choose reps and sets within range
                if (usingGoalSelection && selectedGoal != null) {
                    val range = repRanges[selectedGoal]!!
                    reps = range.random()
                    sets = Random.nextInt(minSets.toIntOrNull() ?: 3, (maxSets.toIntOrNull() ?: 5) + 1)
                } else {
                    // If user manually entered sets and reps, use those
                    sets = manualSets.toIntOrNull() ?: 3
                    reps = manualReps.toIntOrNull() ?: 10
                }

                // Send sets and reps to the parent composable
                onConfirm(sets, reps)
            },
            modifier = Modifier.align(Alignment.End),
            enabled = (usingGoalSelection && minSets.isNotBlank() && maxSets.isNotBlank()) ||
                    (manualSets.isNotBlank() && manualReps.isNotBlank()) // Only allow confirm if valid input is provided
        ) {
            Text("Confirm")
        }
    }
}

@Composable
fun EquipmentSelector(onConfirm: (List<String>) -> Unit) {
    val equipmentOptions = listOf("Dumbbells", "Resistance Bands", "Bodyweight", "Kettlebells", "Barbell", "Pull-up Bar")
    val selected = remember { mutableStateListOf<String>() }

    Column(Modifier.padding(16.dp)) {
        Text("Select Equipment", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        equipmentOptions.forEach { item ->
            Row(
                Modifier.fillMaxWidth().clickable {
                    if (item in selected) selected.remove(item)
                    else selected.add(item)
                }.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = item in selected, onCheckedChange = null)
                Text(item, Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onConfirm(selected.toList()) }, modifier = Modifier.align(Alignment.End)) {
            Text("Confirm")
        }
    }
}

@Composable
fun DurationSelector(onConfirm: (Int) -> Unit) {
    var duration by remember { mutableStateOf(15f) } // default duration

    Column(Modifier.padding(16.dp)) {
        Text("Select Duration (Minutes)", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Text("${duration.toInt()} min", fontSize = 24.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(vertical = 8.dp))

        Slider(
            value = duration,
            onValueChange = { duration = it },
            valueRange = 5f..120f,
            steps = 23, // intervals of 5 mins
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onConfirm(duration.toInt()) }, modifier = Modifier.align(Alignment.End)) {
            Text("Confirm")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { HomeScreen(nc, HomeViewModel(), AuthViewModel(), WorkoutViewModel()) }
}