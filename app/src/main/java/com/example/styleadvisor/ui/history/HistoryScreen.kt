package com.example.styleadvisor.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.R
import com.example.styleadvisor.data.HistoryItem
import com.example.styleadvisor.theme.*
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onItemClick: (androidx.navigation3.runtime.NavKey) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: com.example.styleadvisor.ui.main.AnalysisViewModel? = null
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    val history by com.example.styleadvisor.data.AnalysisRepository.history.collectAsState()
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 100.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "History",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
                    )
                    Text(
                        text = "Track your past looks and see your style evolution.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Top Stats
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    StatCard(
                        icon = {
                            Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                        },
                        iconBg = Color(0xFFFFF0F0),
                        value = "12",
                        label = "Total Analyses"
                    )
                }
                item {
                    StatCard(
                        icon = {
                            Icon(Icons.Outlined.Insights, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(18.dp))
                        },
                        iconBg = Color(0xFFF0FFF0),
                        value = "88",
                        label = "Avg. Score"
                    )
                }
                item {
                    StatCard(
                        icon = {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(18.dp))
                        },
                        iconBg = Color(0xFFFAF0FF),
                        value = "5",
                        label = "Best Score"
                    )
                }
                item {
                    StatCard(
                        icon = {
                            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(18.dp))
                        },
                        iconBg = Color(0xFFFFF7ED),
                        value = "This Month",
                        label = "Most Active",
                        valueSize = 14
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Analyses",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                TextButton(
                    onClick = { isEditing = !isEditing },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (isEditing) "Done" else "Edit",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        items(
            items = history,
            key = { it.timestamp }
        ) { item ->
            HistoryListItem(
                item = item,
                isEditing = isEditing,
                onDelete = { com.example.styleadvisor.data.AnalysisRepository.deleteResult(item.timestamp) },
                onClick = { 
                    if (!isEditing) {
                        viewModel?.showHistoryItem(item)
                        onItemClick(com.example.styleadvisor.AnalysisResult) 
                    }
                },
                modifier = Modifier.animateItem()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = Color.White,
            dragHandle = null
        ) {
            FilterSheetContent(onDismiss = { showFilterSheet = false })
        }
    }
}

@Composable
fun StatCard(
    icon: @Composable () -> Unit,
    iconBg: Color,
    value: String,
    label: String,
    valueSize: Int = 20
) {
    Column(
        modifier = Modifier
            .width(96.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = value,
            fontSize = valueSize.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue,
            maxLines = 1
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextMuted,
            maxLines = 1
        )
    }
}

@Composable
fun HistoryListItem(
    item: HistoryItem, 
    isEditing: Boolean = false,
    onDelete: () -> Unit = {},
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val score = item.result.overallScore
    val (pillBg, pillText) = when(score) {
        in 90..100 -> Pair(BadgeGreenBg, BadgeGreenText)
        in 70..89 -> Pair(BadgeYellowBg, BadgeYellowText)
        else -> Pair(Color(0xFFFFF0F0), Color(0xFFFF5252)) // Red colors
    }
    
    val badgeLabel = when(score) {
        in 90..100 -> "Great Outfit"
        in 70..89 -> "Good Look"
        else -> "Needs Work"
    }
    
    val dateStr = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(item.timestamp))
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image
        AsyncImage(
            model = android.net.Uri.parse(item.imageUri),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.result.shortTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = dateStr,
                fontSize = 10.sp,
                color = TextMuted
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(pillBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = pillText
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Circular Score & Arrow
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) {
                CircularProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = pillText,
                    trackColor = Color.LightGray.copy(alpha = 0.3f),
                    strokeWidth = 3.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(y = 4.dp)
                ) {
                    Text(
                        text = score.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "/100",
                        fontSize = 9.sp,
                        color = TextMuted,
                        modifier = Modifier.offset(y = (-8).dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            AnimatedVisibility(visible = !isEditing) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Details",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            AnimatedVisibility(visible = isEditing) {
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
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
