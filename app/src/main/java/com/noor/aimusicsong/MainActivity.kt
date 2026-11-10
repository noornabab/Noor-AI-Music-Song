
package com.noor.aimusicsong

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkBg = Color(0xFF10131B)
private val Panel = Color(0xFF1C2230)
private val Purple = Color(0xFF9870FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoorMusicApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoorMusicApp() {

    var tab by remember { mutableStateOf("Music") }
    var title by remember { mutableStateOf("") }
    var lyrics by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("Bangla Folk") }
    var vocal by remember { mutableStateOf("Male Vocal") }
    var duration by remember { mutableStateOf("180") }
    var audioUri by remember { mutableStateOf<Uri?>(null) }

    val audioPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        audioUri = uri
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            background = DarkBg,
            surface = Panel
        )
    ) {
        Scaffold(
            containerColor = DarkBg,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Noor AI Music Song",
                                color = Color.White
                            )
                            Text(
                                "Create Your Own Music",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkBg
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Panel
                ) {
                    listOf("Music", "Video", "Library").forEach { item ->
                        NavigationBarItem(
                            selected = tab == item,
                            onClick = { tab = item },
                            icon = {
                                Text(
                                    when (item) {
                                        "Music" -> "♫"
                                        "Video" -> "▶"
                                        else -> "▤"
                                    }
                                )
                            },
                            label = { Text(item) }
                        )
                    }
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                when (tab) {

                    "Music" -> {
                        Text(
                            "Create AI Music",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        InputBox(
                            title,
                            { title = it },
                            "Song Title"
                        )

                        InputBox(
                            lyrics,
                            { lyrics = it },
                            "Write Your Lyrics",
                            minLines = 5
                        )

                        InputBox(
                            prompt,
                            { prompt = it },
                            "Music Prompt",
                            minLines = 3
                        )

                        Text("Music Style", color = Color.White)

                        OptionSelector(
                            selected = style,
                            options = listOf(
                                "Bangla Folk",
                                "Baul-Fakir",
                                "Palligeeti",
                                "Sad Song",
                                "Pop",
                                "Rock",
                                "Islamic",
                                "Classical"
                            ),
                            onSelect = { style = it }
                        )

                        Text("Vocal Type", color = Color.White)

                        OptionSelector(
                            selected = vocal,
                            options = listOf(
                                "Male Vocal",
                                "Female Vocal",
                                "Instrumental"
                            ),
                            onSelect = { vocal = it }
                        )

                        InputBox(
                            duration,
                            { duration = it },
                            "Duration in Seconds"
                        )

                        OutlinedButton(
                            onClick = {
                                audioPicker.launch("audio/*")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (audioUri == null)
                                    "Upload Reference Audio"
                                else
                                    "Audio Selected ✓"
                            )
                        }

                        Button(
                            onClick = {
                                // AI generation will be connected
                                // after the backend is configured.
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Text("♫  CREATE MUSIC")
                        }

                        Text(
                            "AI service connection will be configured next.",
                            color = Color.Yellow,
                            fontSize = 12.sp
                        )
                    }

                    "Video" -> {
                        Text(
                            "Create Music Video",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Text(
                            "Create a video from your generated song.",
                            color = Color.LightGray
                        )

                        Text(
                            "Watermark: App Name",
                            color = Color.White
                        )

                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("CREATE VIDEO")
                        }

                        Text(
                            "Video generator will be connected next.",
                            color = Color.Yellow
                        )
                    }

                    "Library" -> {
                        Text(
                            "My Library",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Text(
                            "Your generated songs and videos will appear here.",
                            color = Color.LightGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Noor AI Music Song",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun InputBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun OptionSelector(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        options.chunked(2).forEach { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                group.forEach { option ->
                    FilterChip(
                        selected = selected == option,
                        onClick = { onSelect(option) },
                        label = { Text(option) }
                    )
                }
            }
        }
    }
}
