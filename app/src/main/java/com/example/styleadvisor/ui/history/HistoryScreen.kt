package com.example.styleadvisor.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HistoryContent() {
    var showFilterSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "History",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "Your past outfit analyses",
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }
            
            IconButton(onClick = { showFilterSheet = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = TextNavyBlue
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        HistoryCard(
            title = "Casual Streetwear",
            date = "May 12, 2025 • 10:30 AM",
            score = 87,
            scoreText = "Great Look!",
            scoreColor = ScoreHigh
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        HistoryCard(
            title = "Smart Casual",
            date = "May 10, 2025 • 6:45 PM",
            score = 78,
            scoreText = "Good",
            scoreColor = ScoreMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        HistoryCard(
            title = "Chic & Minimal",
            date = "May 09, 2025 • 2:20 PM",
            score = 92,
            scoreText = "Excellent",
            scoreColor = ScoreHigh
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        HistoryCard(
            title = "Winter Layered",
            date = "May 07, 2025 • 11:15 AM",
            score = 70,
            scoreText = "Average",
            scoreColor = ScoreMedium
        )
        
        Spacer(modifier = Modifier.height(100.dp))
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = Color.White,
            dragHandle = null // Removing the small grey drag handle as per design
        ) {
            FilterSheetContent(onDismiss = { showFilterSheet = false })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSheetContent(onDismiss: () -> Unit) {
    var selectedSort by remember { mutableStateOf("Recent") }
    var selectedStyle by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Filter History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceVariant, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(20.dp), tint = TextNavyBlue)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sort By
        Text(text = "Sort By", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sortOptions = listOf("Recent", "Oldest", "Highest Score", "Lowest Score")
            sortOptions.forEach { option ->
                FilterChip(
                    text = option,
                    isSelected = selectedSort == option,
                    onClick = { selectedSort = option }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Outfit Style
        Text(text = "Outfit Style", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val styleOptions = listOf("All", "Casual", "Streetwear", "Formal", "Smart Casual", "Party", "Summer", "Winter", "Minimal", "Other")
            styleOptions.forEach { option ->
                FilterChip(
                    text = option,
                    isSelected = selectedStyle == option,
                    onClick = { selectedStyle = option }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Date Range
        Text(text = "Date Range", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceVariant)
                .clickable { /* Select Date */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = TextNavyBlue)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Select Date Range", fontSize = 14.sp, color = TextNavyBlue, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Apply Button
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TextNavyBlue)
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Apply Filters", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) TextNavyBlue else SurfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = if (isSelected) Color.White else TextNavyBlue
        )
    }
}

@Composable
fun HistoryCard(title: String, date: String, score: Int, scoreText: String, scoreColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center).size(48.dp),
                tint = TextMuted.copy(alpha = 0.3f)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextNavyBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date,
                fontSize = 12.sp,
                color = TextMuted
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { score / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = scoreColor,
                        strokeWidth = 3.dp,
                        trackColor = SurfaceVariant
                    )
                    Text(
                        text = score.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = scoreText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scoreColor
                )
            }
        }
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextMuted
        )
    }
}
