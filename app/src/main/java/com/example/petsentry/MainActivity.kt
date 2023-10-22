package com.example.petsentry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
                        composable("register") { RegisterScreen() }
                        composable("login") { LoginScreen() }
                    }
                }
            }
        }
    }
}

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
            fontSize = 48.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Text(
            text = "to",
            fontSize = 48.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.W400
                    )
                ) {
                    append("PetSentry")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 48.sp,
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
fun RegisterScreen(modifier: Modifier = Modifier) {
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
            fontSize = 48.sp,
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
                onClick = { /*TODO*/ },
                modifier = Modifier.scale(1.5F)
            ) {
                Text(text = "Register")
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
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
            fontSize = 48.sp,
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
                onClick = { /*TODO*/ },
                modifier = Modifier.scale(1.5F)
            ) {
                Text(text = "Login")
            }
        }
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenPreview() {
    PetSentryTheme {
        RegisterScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    PetSentryTheme {
        LoginScreen()
    }
}*/
