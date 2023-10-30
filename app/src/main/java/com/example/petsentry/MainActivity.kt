package com.example.petsentry

import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petsentry.ui.theme.PetSentryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
                        composable("menu") { MenuScreen(navController) }
                        composable("sensor") { SensorScreen() }
                        composable("livestream") { LivestreamScreen() }
                        composable("log") { EventLogScreen() }
                    }
                }
            }
        }
    }
}

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
fun MenuScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var selectedMode by remember { mutableStateOf("Automatic") }
    val modes = listOf("Automatic", "Manual")

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
            Column (
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
            Column (
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
                            selected = (text == selectedMode),
                            onClick = { selectedMode = text }
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

@Composable
fun SensorScreen(modifier: Modifier = Modifier) {
    var foodWeight = 0
    var waterLevel = "Low"
    var reserveLevel = "Low"
    var fillTo by remember { mutableStateOf("") }

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
                    label = { Text("Weight in grams") },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Button(
                    onClick = { /*TODO*/ },
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
                    onClick = { /*TODO*/ },
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
        // TODO: Livestream here
    }
}

@Composable
fun EventLogScreen(modifier: Modifier = Modifier) {
    var logItems = listOf("Event 1", "Event 2", "Event 3", "Event 4",
        "Event 5", "Event 6", "Event 7", "Event 8", "Event 9", "Event 10",
        "Event 11", "Event 12", "Event 13", "Event 14", "Event 15", "Event 16",
        "Event 17", "Event 18", "Event 19", "Event 20", "Event 21", "Event 22")

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
            items(logItems) { item ->
                ListItem(
                    headlineContent = { Text(item) }
                )
                Divider()
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ScreenPreview() {
    PetSentryTheme {
        EventLogScreen()
    }
}

/*
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WelcomeScreenPreview() {
    PetSentryTheme {
        WelcomeScreen()
    }
}
}*/
