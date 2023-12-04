package com.example.petsentry

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petsentry.ui.theme.PetSentryTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

var initialSelectedMode: String? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Firebase
        val database = FirebaseDatabase.getInstance("https://petsentry-633c1-default-rtdb.europe-west1.firebasedatabase.app/")
        val dbRef = database.reference

        dbRef.child("Operation Mode").child("op_mode").get().addOnSuccessListener {
            initialSelectedMode = it.value.toString()
        }.addOnFailureListener{
            // Failed to read value
        }


        super.onCreate(savedInstanceState)
        setContent {
            PetSentryTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "welcome"
                    ) {
                        composable("welcome") { WelcomeScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("menu") { MenuScreen(navController, dbRef) }
                        composable("sensor") { SensorScreen(dbRef) }
                        composable("livestream") { LivestreamScreen() }
                        composable("log") { EventLogScreen(dbRef) }
                    }
                }
            }
        }
    }
}


val LocalSelectedMode = compositionLocalOf { mutableStateOf(initialSelectedMode) }


// Screen composables
@Composable
fun WelcomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "Hello!",
            fontSize = 64.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 28.sp,
            fontWeight = FontWeight.W100
        )
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "Welcome",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Text(
            text = "to",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.W400
                    )
                ) {
                    append("PetSentry")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.W400
                    )
                ) {
                    append(".")
                }
            }
        )
        Column(
            modifier = Modifier
                           .fillMaxSize(),

            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.scale(1.5F)
            ) {
                Text(text = "Register")
            }
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.scale(1.5F)
            ) {
                Text(text = "Login")
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "Registration",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") }
                )
            }
            Button(
                onClick = { navController.navigate("menu") },
                modifier = Modifier.scale(1.5F)
            ) {
                Text(text = "Register")
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "Login",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") }
                )
            }
            Button(
                onClick = { navController.navigate("menu") },
                modifier = Modifier.scale(1.5F)
            ) {
                Text(text = "Login")
            }
        }
    }
}

@Composable
fun MenuScreen(navController: NavHostController, dbRef: DatabaseReference, modifier: Modifier = Modifier) {
    val selectedMode = LocalSelectedMode.current
    val modes = listOf("Automatic", "Manual")

    CompositionLocalProvider(LocalSelectedMode provides selectedMode) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(
                modifier = Modifier.height(40.dp)
            )
            Text(
                text = "PetSentry",
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W400
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Button(
                        onClick = { navController.navigate("livestream") },
                        modifier = Modifier.scale(1.5F)
                    ) {
                        Text(text = "Livestream")
                    }
                    Spacer(
                        modifier = Modifier.height(56.dp)
                    )
                    Button(
                        onClick = { navController.navigate("sensor") },
                        modifier = Modifier.scale(1.5F)
                    ) {
                        Text(text = "Sensors & Actions")
                    }
                    Spacer(
                        modifier = Modifier.height(56.dp)
                    )
                    Button(
                        onClick = { navController.navigate("log") },
                        modifier = Modifier.scale(1.5F)
                    ) {
                        Text(text = "Event Log")
                    }
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Operation mode:",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    modes.forEach { text ->
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedMode.value),
                                onClick = {
                                    selectedMode.value = text
                                    if (selectedMode.value == "Automatic") {
                                        dbRef.child("Operation Mode").child("op_mode").setValue("Automatic")
                                    } else if (selectedMode.value == "Manual") {
                                        dbRef.child("Operation Mode").child("op_mode").setValue("Manual")
                                    }
                                }
                            )
                            Text(
                                text = text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SensorScreen(dbRef: DatabaseReference, modifier: Modifier = Modifier) {
    val selectedMode = LocalSelectedMode.current
    var buttonsEnabled by remember { mutableStateOf(false) }
    var foodWeight by remember { mutableIntStateOf(0) }
    var waterLevel by remember { mutableStateOf("Low") }
    var reserveLevel by remember { mutableStateOf("Low") }
    var fillTo by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (selectedMode.value == "Automatic") {
        buttonsEnabled = false
    } else if (selectedMode.value == "Manual") {
        buttonsEnabled = true
    }

    LaunchedEffect(key1 = dbRef) {
        dbRef.child("Food").child("bowl_weight").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodWeight = dataSnapshot.getValue<Int>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
    LaunchedEffect(key1 = dbRef) {
        dbRef.child("Water").child("water_level").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                waterLevel = dataSnapshot.getValue<String>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
    LaunchedEffect(key1 = dbRef) {
        dbRef.child("Water").child("reserve_level").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reserveLevel = dataSnapshot.getValue<String>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sensors & Actions",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Food
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Food",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "In bowl: $foodWeight g",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, bottom = 8.dp)
                )
                Text(
                    text = "Fill to:",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = fillTo,
                    onValueChange = { fillTo = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    label = { Text("Weight in grams") },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Button(
                    enabled = buttonsEnabled,
                    onClick = {
                        if (fillTo.toIntOrNull() == null) {
                            Toast.makeText(context, "Invalid input! Try again.", Toast.LENGTH_SHORT).show()
                        } else if (fillTo.toIntOrNull()!! > 0) {
                            dbRef.child("Food").child("target_weight").setValue(fillTo.toInt())
                            dbRef.child("Food").child("fill_food_now").setValue(1)
                        }
                    },
                    modifier = Modifier.scale(1.3F)
                ) {
                    Text(text = "Dispense Food")
                }
            }
            Spacer(
                modifier = Modifier.height(40.dp)
            )
            // Water
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Water",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "Bowl level: $waterLevel",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, bottom = 8.dp)
                )
                Text(
                    text = "Reserve level: $reserveLevel",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, bottom = 16.dp)
                )
                Button(
                    enabled = buttonsEnabled,
                    onClick = {
                        dbRef.child("Water").child("fill_water_now").setValue(1)
                    },
                    modifier = Modifier.scale(1.3F)
                ) {
                    Text(text = "Dispense Water")
                }
            }
        }
    }
}

@Composable
fun LivestreamScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Livestream",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp)
        )
        Exoplayer()
    }
}

@Composable
fun EventLogScreen(dbRef: DatabaseReference, modifier: Modifier = Modifier) {

    //var logItems = remember { mutableStateListOf("") }
    val logItems = remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(key1 = dbRef) {
        dbRef.child("Event Log").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                logItems.value = dataSnapshot.getValue<SnapshotStateList<String>>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Event Log",
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp)
        )
        LazyColumn {
            items(logItems.value) { item ->
                ListItem(
                    headlineContent = { Text(item) }
                )
                Divider()
            }
        }
    }
}

// Livestream
@SuppressLint("OpaqueUnitKey")
@Composable
fun Exoplayer() {
    val context = LocalContext.current

    val mediaItem = MediaItem.Builder()
        .setUri("https://fcc3ddae59ed.us-west-2.playback.live-video.net/api/video/v1/us-west-2.893648527354.channel.DmumNckWFTqz.m3u8")
        .build()
    val exoPlayer = remember(context, mediaItem) {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = REPEAT_MODE_OFF
            }
    }

    DisposableEffect(
        AndroidView(factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
                useController = false
                hideController()
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
}