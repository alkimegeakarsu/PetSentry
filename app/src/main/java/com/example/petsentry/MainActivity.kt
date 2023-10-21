package com.example.petsentry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petsentry.ui.theme.PetSentryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetSentryTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FirstGreetingText()
                }
            }
        }
    }
}

@Composable
fun FirstGreetingText(modifier: Modifier = Modifier) {
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
            fontSize = 80.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 28.sp,
            fontWeight = FontWeight.W100
        )
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "Welcome",
            fontSize = 64.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Text(
            text = "to",
            fontSize = 64.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.W400
                    )
                ) {
                    append("PetSentry")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.W400
                    )
                ) {
                    append(".")
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FirstGreetingTextPreview() {
    PetSentryTheme {
        FirstGreetingText()
    }
}