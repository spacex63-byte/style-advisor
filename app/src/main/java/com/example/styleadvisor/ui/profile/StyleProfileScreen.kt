package com.example.styleadvisor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel
) {
    var preferredColors by remember { mutableStateOf("Neutral, Earth Tones") }
    var bodyType by remember { mutableStateOf("Athletic") }
    var typicalOccasion by remember { mutableStateOf("Casual, Office") }

    Box(modifier = Modifier.fillMaxSize().background(GlobalBackgroundGradient)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Style Profile", fontWeight = FontWeight.Bold, color = TextNavyBlue) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextNavyBlue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = preferredColors,
                    onValueChange = { preferredColors = it },
                    label = { Text("Preferred Colors") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = bodyType,
                    onValueChange = { bodyType = it },
                    label = { Text("Body Type") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = typicalOccasion,
                    onValueChange = { typicalOccasion = it },
                    label = { Text("Typical Occasions") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val progress = if (preferredColors.isNotBlank() && bodyType.isNotBlank() && typicalOccasion.isNotBlank()) 1.0f else 0.5f
                        viewModel.updateStyleProfileProgress(progress)
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Save Profile", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}
