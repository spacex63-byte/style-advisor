package com.example.styleadvisor.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.styleadvisor.AnalysisResult
import com.example.styleadvisor.theme.*
import com.example.styleadvisor.ui.profile.ProfileContent

enum class BottomTab {
    HOME, PROFILE
}

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        bottomBar = { 
            CustomBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            ) 
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 70.dp
                )
        ) {
            when (selectedTab) {
                BottomTab.HOME -> HomeContent(onItemClick)
                BottomTab.PROFILE -> ProfileContent()
            }
        }
    }
}

@Composable
fun HomeContent(onItemClick: (NavKey) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TopAppBarSection()
        
        Spacer(modifier = Modifier.height(20.dp))
        HeroSection()
        
        Spacer(modifier = Modifier.height(20.dp))
        AnalyzeButtonSection()
        
        Spacer(modifier = Modifier.height(20.dp))
        RecentAnalysesSection(onItemClick = onItemClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        PromoSection()
        
    }
}

@Composable
fun TopAppBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Spacer to maintain center alignment for the title
        Spacer(modifier = Modifier.size(40.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Style Advisor",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDarkMaroon
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = TextDarkMaroon
            )
        }
    }
}

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Get AI-Powered Fashion Advice",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDarkMaroon,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Upload your outfit photo and get\npersonalized style insights instantly.",
            fontSize = 14.sp,
            color = TextMuted,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AnalyzeButtonSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFEBE3DE))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val bracketLength = 40.dp.toPx()
                val bracketStroke = 4.dp.toPx()
                val padding = 24.dp.toPx()
                val color = Color.White
                
                // Top Left
                drawLine(color, Offset(padding, padding), Offset(padding + bracketLength, padding), strokeWidth = bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(padding, padding), Offset(padding, padding + bracketLength), strokeWidth = bracketStroke, cap = StrokeCap.Round)
                
                // Bottom Left
                drawLine(color, Offset(padding, size.height - padding), Offset(padding + bracketLength, size.height - padding), strokeWidth = bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(padding, size.height - padding), Offset(padding, size.height - padding - bracketLength), strokeWidth = bracketStroke, cap = StrokeCap.Round)
                
                // Bottom Right
                drawLine(color, Offset(size.width - padding, size.height - padding), Offset(size.width - padding - bracketLength, size.height - padding), strokeWidth = bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width - padding, size.height - padding), Offset(size.width - padding, size.height - padding - bracketLength), strokeWidth = bracketStroke, cap = StrokeCap.Round)
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF4A28C)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FileUpload,
                    contentDescription = "Upload",
                    tint = Color.Black
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Upload Photo Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(TextDarkMaroon)
                .clickable { /* Action */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Upload Photo",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Upload Photo",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Or divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = TextDarkMaroon,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Try with Sample
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(30.dp))
                .clickable { /* Action */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Checkroom,
                contentDescription = "Sample",
                tint = TextDarkMaroon,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Try with a Sample",
                color = TextDarkMaroon,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RecentAnalysesSection(onItemClick: (NavKey) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Analyses",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDarkMaroon
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                color = TextMuted
            )
        }
        
        Spacer(modifier = Modifier.height(14.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnalysisCard("Smart Casual", "2 May 2025", 86, ScoreHigh, onClick = { onItemClick(AnalysisResult) })
            AnalysisCard("Casual Day Out", "30 Apr 2025", 92, ScoreHigh, onClick = { onItemClick(AnalysisResult) })
            AnalysisCard("Evening Look", "28 Apr 2025", 78, ScoreMedium, onClick = { onItemClick(AnalysisResult) })
        }
    }
}

@Composable
fun AnalysisCard(title: String, date: String, score: Int, scoreColor: Color, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center).size(48.dp),
                tint = TextMuted.copy(alpha = 0.3f)
            )
            
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkMaroon
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDarkMaroon
        )
        Text(
            text = date,
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.offset(y = (-4).dp)
        )
    }
}

@Composable
fun PromoSection() {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(PromoGradientStart, PromoGradientEnd)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = TextDarkMaroon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Unlock Your Best Style",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDarkMaroon
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AI tips that match your vibe,\npersonality & body type.",
                fontSize = 12.sp,
                color = TextDarkMaroon.copy(alpha = 0.8f),
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upgrade to Pro",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDarkMaroon
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextDarkMaroon
                    )
                }
            }
        }
    }
}

@Composable
fun CustomBottomBar(selectedTab: BottomTab, onTabSelected: (BottomTab) -> Unit) {
    val BlueAccent = Color(0xFF5A75FF)
    val LightBlueBg = Color(0xFFF5F6FE)
    val GrayIcon = Color(0xFFA0A0A0)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .navigationBarsPadding()
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Tab
                BottomNavItem(
                    label = "Home",
                    icon = Icons.Default.Home,
                    isSelected = selectedTab == BottomTab.HOME,
                    selectedColor = BlueAccent,
                    unselectedColor = GrayIcon,
                    selectedBgColor = LightBlueBg,
                    onClick = { onTabSelected(BottomTab.HOME) }
                )
                
                // Create Button
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .width(140.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF6B7BFF), Color(0xFF4A55FF))
                            )
                        )
                        .clickable { /* Create Action */ },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                
                // Profile Tab (Me)
                BottomNavItem(
                    label = "Me",
                    icon = Icons.Default.Person,
                    isSelected = selectedTab == BottomTab.PROFILE,
                    selectedColor = BlueAccent,
                    unselectedColor = GrayIcon,
                    selectedBgColor = LightBlueBg,
                    onClick = { onTabSelected(BottomTab.PROFILE) }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    label: String, 
    icon: ImageVector, 
    isSelected: Boolean, 
    selectedColor: Color,
    unselectedColor: Color,
    selectedBgColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) selectedBgColor else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) selectedColor else unselectedColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) selectedColor else unselectedColor,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
