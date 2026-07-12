package com.example.styleadvisor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(GlobalBackgroundGradient)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Privacy Policy", fontWeight = FontWeight.Bold, color = TextNavyBlue) },
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Text("Last Updated: July 2026", fontWeight = FontWeight.SemiBold, color = TextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Welcome to Style Advisor. We value your privacy and are committed to protecting your personal data.", fontSize = 15.sp, color = TextNavyBlue, lineHeight = 22.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    PolicySection("1. Information We Collect", "We collect information you provide directly, such as your style preferences, uploaded photos for analysis, and profile details.")
                    PolicySection("2. How We Use Your Information", "Your data is used to provide AI-driven fashion recommendations, improve our algorithms, and personalize your experience.")
                    PolicySection("3. Data Security", "We implement industry-standard security measures to ensure your uploaded images and personal details are safe and never shared with unauthorized third parties.")
                    PolicySection("4. Your Rights", "You can delete your account and data at any time from the app settings. You can also request a copy of the data we have stored about you.")
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("I Understand", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            color = TextMuted,
            lineHeight = 20.sp
        )
    }
}
