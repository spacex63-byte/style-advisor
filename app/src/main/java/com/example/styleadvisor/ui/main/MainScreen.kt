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
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.styleadvisor.AnalysisResult
import com.example.styleadvisor.R
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
                .background(ThemeLightBlue)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
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
            .fillMaxWidth()
            .background(ThemeLightBlue)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        TopAppBarSection()
        
        Spacer(modifier = Modifier.height(15.dp))
        HeroSection()
        
        Spacer(modifier = Modifier.height(20.dp))
        AnalyzeButtonSection()
        
        Spacer(modifier = Modifier.height(20.dp))
        RecentAnalysesSection(onItemClick = onItemClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        PromoSection()
        
        // Add bottom padding to ensure there's enough space to scroll past the bottom nav bar
        Spacer(modifier = Modifier.height(60.dp))
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
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = TextNavyBlue
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Style",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue
            )
            Text(
                text = "Advisor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = TextNavyBlue
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
            text = "Get AI-Powered\nFashion Advice",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextNavyBlue,
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
    var isImageUploaded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isImageUploaded) {
            // Image Container
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(4.dp, Color.White, RoundedCornerShape(32.dp))
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFFEBE3DE))
                    .clickable { isImageUploaded = false }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_fashion_man),
                    contentDescription = "Hero Fashion",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val bracketLength = 48.dp.toPx()
                    val bracketStroke = 4.dp.toPx()
                    val padding = 24.dp.toPx()
                    val color = Color.White
                    val cornerRadius = 24.dp.toPx()
                    
                    // Top Left
                    val pathTL = Path().apply {
                        moveTo(padding, padding + bracketLength)
                        lineTo(padding, padding + cornerRadius)
                        arcTo(Rect(padding, padding, padding + 2 * cornerRadius, padding + 2 * cornerRadius), 180f, 90f, false)
                        lineTo(padding + bracketLength, padding)
                    }
                    drawPath(pathTL, color, style = Stroke(width = bracketStroke, cap = StrokeCap.Round))
                    
                    // Top Right
                    val pathTR = Path().apply {
                        moveTo(size.width - padding - bracketLength, padding)
                        lineTo(size.width - padding - cornerRadius, padding)
                        arcTo(Rect(size.width - padding - 2 * cornerRadius, padding, size.width - padding, padding + 2 * cornerRadius), -90f, 90f, false)
                        lineTo(size.width - padding, padding + bracketLength)
                    }
                    drawPath(pathTR, color, style = Stroke(width = bracketStroke, cap = StrokeCap.Round))
                    
                    // Bottom Right
                    val pathBR = Path().apply {
                        moveTo(size.width - padding, size.height - padding - bracketLength)
                        lineTo(size.width - padding, size.height - padding - cornerRadius)
                        arcTo(Rect(size.width - padding - 2 * cornerRadius, size.height - padding - 2 * cornerRadius, size.width - padding, size.height - padding), 0f, 90f, false)
                        lineTo(size.width - padding - bracketLength, size.height - padding)
                    }
                    drawPath(pathBR, color, style = Stroke(width = bracketStroke, cap = StrokeCap.Round))
                    
                    // Bottom Left
                    val pathBL = Path().apply {
                        moveTo(padding + bracketLength, size.height - padding)
                        lineTo(padding + cornerRadius, size.height - padding)
                        arcTo(Rect(padding, size.height - padding - 2 * cornerRadius, padding + 2 * cornerRadius, size.height - padding), 90f, 90f, false)
                        lineTo(padding, size.height - padding - bracketLength)
                    }
                    drawPath(pathBL, color, style = Stroke(width = bracketStroke, cap = StrokeCap.Round))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
                    .clickable { isImageUploaded = true },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(ThemeLightBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FileUpload,
                            contentDescription = "Upload Icon",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Upload Your Outfit Photo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextNavyBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "JPG, PNG up to 10MB",
                        fontSize = 14.sp,
                        color = TextMuted
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Upload Photo Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(TextNavyBlue)
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
                color = TextNavyBlue,
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
                tint = TextNavyBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Try with a Sample",
                color = TextNavyBlue,
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
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextNavyBlue,
                letterSpacing = (-0.2).sp
            )
            Text(
                text = "See all",
                fontSize = 12.sp,
                color = TextMuted,
                letterSpacing = (-0.2).sp
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
            AnalysisCard("Office Wear", "22 Apr 2025", 88, ScoreHigh, onClick = { onItemClick(AnalysisResult) })
            AnalysisCard("Weekend Chill", "18 Apr 2025", 71, ScoreMedium, onClick = { onItemClick(AnalysisResult) })
            AnalysisCard("Party Fit", "10 Apr 2025", 65, ScoreMedium, onClick = { onItemClick(AnalysisResult) })
            AnalysisCard("Winter Coat", "5 Apr 2025", 45, ScoreLow, onClick = { onItemClick(AnalysisResult) })
        }
    }
}

@Composable
fun AnalysisCard(title: String, date: String, score: Int, scoreColor: Color, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceVariant)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_fashion_man),
                contentDescription = "Analysis Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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
                    color = TextNavyBlue
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextNavyBlue,
            letterSpacing = (-0.2).sp
        )
        Text(
            text = date,
            fontSize = 11.sp,
            color = TextMuted,
            letterSpacing = (-0.2).sp,
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
                    colors = listOf(PromoPopStart, PromoPopEnd)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Unlock Your Best Style",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AI tips that match your vibe,\npersonality & body type.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
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
                        color = TextNavyBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextNavyBlue
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
            .navigationBarsPadding()
    ) {
        Surface(
            shape = RectangleShape,
            color = Color.White,
            shadowElevation = 24.dp,
            modifier = Modifier.fillMaxWidth().height(70.dp).align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Tab
                BottomNavItem(
                    label = "Home",
                    icon = Icons.Rounded.Home,
                    isSelected = selectedTab == BottomTab.HOME,
                    selectedColor = BlueAccent,
                    unselectedColor = Color.Black,
                    selectedBgColor = LightBlueBg,
                    onClick = { onTabSelected(BottomTab.HOME) }
                )
                
                // Profile Tab (Me)
                BottomNavItem(
                    label = "Me",
                    icon = Icons.Rounded.Person,
                    isSelected = selectedTab == BottomTab.PROFILE,
                    selectedColor = BlueAccent,
                    unselectedColor = Color.Black,
                    selectedBgColor = LightBlueBg,
                    onClick = { onTabSelected(BottomTab.PROFILE) }
                )
            }
        }
        
        // Floating Camera Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp)
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 3.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFFFF7A59), // Coral
                            Color(0xFFFFD54F), // Yellow
                            Color(0xFF81C784), // Green
                            Color(0xFF6B7BFF), // Blue
                            Color(0xFFFF7A59)  // Coral
                        )
                    ),
                    shape = CircleShape
                )
                .clickable { /* Camera Action */ },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ButtonBlueStart, ButtonBlueEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
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
            .width(80.dp)
            .height(56.dp)
            .clip(CircleShape)
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
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) selectedColor else unselectedColor,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
