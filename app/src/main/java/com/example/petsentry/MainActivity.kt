@file:Suppress("DEPRECATION")

package com.example.petsentry

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petsentry.ui.theme.PetSentryTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.IOException
import java.io.OutputStream
import java.util.UUID


class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }

    val uuid: UUID = UUID.randomUUID()

    var bluetoothAdapter: BluetoothAdapter? = null

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name

                    // Display the name of the discovered device as a toast message
                    Toast.makeText(context, "Discovered device: $deviceName", Toast.LENGTH_SHORT).show()

                    // Connect and send data
                    if (deviceName == "raspberrypi") {
                        Toast.makeText(context, "Trying to connect", Toast.LENGTH_SHORT).show()

                        // Cancel discovery
                        bluetoothAdapter?.cancelDiscovery()

                        // Get BluetoothSocket
                        var socket: BluetoothSocket? = null

                        try {
                            socket = device.createRfcommSocketToServiceRecord(uuid)
                        } catch (e: IOException) {
                            Toast.makeText(context, "BluetoothSocket error", Toast.LENGTH_SHORT).show()
                        }

                        // Connect
                        try {
                            socket?.connect()
                        } catch (connectException: IOException) {
                            Toast.makeText(context, "socket?.connect() error", Toast.LENGTH_SHORT).show()
                            try {
                                socket?.close()
                            } catch (closeException: IOException) {
                                Toast.makeText(context, "socket?.close() error", Toast.LENGTH_SHORT).show()
                                closeException.printStackTrace()
                            }
                        }

                        // Send data and close everything
                        val text = "SSID: hello, Password: world"
                        val outputStream: OutputStream? = socket?.outputStream
                        outputStream?.write(text.toByteArray())
                        outputStream?.close()
                        socket?.close()

//                        // Send file data after connection
//                        val file = File("test.txt") // replace with your file path
//                        val fileInputStream = FileInputStream(file)
//                        val buffer = ByteArray(file.length().toInt())
//                        fileInputStream.read(buffer)
//                        fileInputStream.close()
//
//                        val outputStream: OutputStream? = socket?.outputStream
//                        outputStream.write(buffer)
//                        outputStream.close()
//                        socket?.close()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
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
                        composable("welcome") { WelcomeScreen(navController, bluetoothAdapter) }
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

        // Bluetooth
        // Request permissions
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN), 1)
        // Get Bluetooth adapter
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "This device does not support Bluetooth", Toast.LENGTH_SHORT).show()
        } else {
            // Bluetooth is supported
            // Check and enable Bluetooth
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
            // Register for broadcasts when a device is discovered.
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }
}

// Screen composables
@SuppressLint("MissingPermission")
@Composable
fun WelcomeScreen(navController: NavHostController, bluetoothAdapter: BluetoothAdapter?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
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
                onClick = {
                    bluetoothAdapter?.startDiscovery()
                    Toast.makeText(context, "Enabled discovery", Toast.LENGTH_SHORT).show()
                    navController.navigate("register")
                          },
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
        Exoplayer()
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

// Livestream
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
