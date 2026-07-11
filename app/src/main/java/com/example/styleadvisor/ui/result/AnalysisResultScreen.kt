package com.example.styleadvisor.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@Composable
fun AnalysisResultScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = BackgroundWarmWhite,
        topBar = {
            ResultTopBar(
                title = "Analysis Result",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            OverviewContent()
        }
    }
}

@Composable
fun OverviewContent() {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        HeroScoreCard()
        
        Spacer(modifier = Modifier.height(24.dp))
        NewAttributeGrid()
        
        Spacer(modifier = Modifier.height(16.dp))
        StyleOverviewCard()
        
        Spacer(modifier = Modifier.height(16.dp))
        BestForCard()
        
        Spacer(modifier = Modifier.height(16.dp))
        FeedbackCard(
            isPositive = true,
            title = "What Looks Best",
            description = "The earthy tones, clean layering and fitted pants create a balanced and modern look."
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        FeedbackCard(
            isPositive = false,
            title = "What Could Improve",
            description = "Try adding a statement accessory and different shoes to elevate the look."
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ImproveContent() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Top Improvements",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDarkMaroon
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        ImprovementCard(
            icon = Icons.Default.Watch,
            title = "Add a Watch",
            description = "A sleek watch will elevate\nyour overall look."
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        ImprovementCard(
            icon = Icons.Default.Layers,
            title = "Try Layering",
            description = "Add a light jacket or overshirt\nfor more depth."
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        ImprovementCard(
            icon = Icons.Default.DirectionsWalk,
            title = "Better Footwear",
            description = "White sneakers are good,\nbut loafers will make it sharper."
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "AI Recommended Look",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDarkMaroon
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        AiRecommendedLook()
    }
}

@Composable
fun ImprovementCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(RedPillBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ScoreTextRed)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDarkMaroon)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 12.sp, color = TextMuted, lineHeight = 16.sp)
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center).size(24.dp),
                tint = TextMuted.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun AiRecommendedLook() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Your Look
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(24.dp)).background(SurfaceVariant)) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(80.dp),
                        tint = TextMuted.copy(alpha = 0.3f)
                    )
                }
                
                // Upgraded Look
                Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(24.dp)).background(SurfaceVariant)) {
                    Icon(
                        imageVector = Icons.Default.PersonOutline,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(80.dp),
                        tint = TextMuted.copy(alpha = 0.3f)
                    )
                }
            }
            
            // Floating Arrow Button
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, SurfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = ScoreTextRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Your Look", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDarkMaroon)
            Text(text = "Upgraded Look", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDarkMaroon)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ScoreTextRed.copy(alpha = 0.5f)))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SurfaceVariant))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SurfaceVariant))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SurfaceVariant))
        }
    }
}

@Composable
fun ResultTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextDarkMaroon
                )
            }
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDarkMaroon
            )
        }
        
        Box(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun HeroScoreCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(224.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(Color.White, Color(0xFFFDECE9))
            ))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Circular Progress
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { 0.86f },
                            modifier = Modifier.fillMaxSize(),
                            color = ScoreTextRed,
                            trackColor = Color(0xFFFDECE9),
                            strokeWidth = 10.dp,
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(y = 2.dp)
                        ) {
                            Text(
                                text = "86",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkMaroon,
                                lineHeight = 28.sp
                            )
                            Text(
                                text = "/100",
                                fontSize = 14.sp,
                                color = TextDarkMaroon,
                                modifier = Modifier.offset(y = (-4).dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(
                        text = "Great Look! \uD83D\uDD25",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDarkMaroon,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You've got a confident and stylish vibe.",
                        fontSize = 12.sp,
                        color = TextDarkMaroon,
                        lineHeight = 18.sp
                    )
                }
            }
            
            // Right Image Placeholder
            Box(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxHeight()
                    .background(SurfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center).size(120.dp),
                    tint = TextMuted.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun NewAttributeGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(androidx.compose.foundation.rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NewAttributeCard(modifier = Modifier.width(100.dp).height(120.dp), icon = Icons.Default.Palette, label = "Color\nHarmony", score = "88")
        NewAttributeCard(modifier = Modifier.width(100.dp).height(120.dp), icon = Icons.Default.Person, label = "Fit", score = "85") // Using Person/Checkroom placeholder
        NewAttributeCard(modifier = Modifier.width(100.dp).height(120.dp), icon = Icons.Default.ThumbUp, label = "Style", score = "87")
        NewAttributeCard(modifier = Modifier.width(100.dp).height(120.dp), icon = Icons.Default.Star, label = "Overall", score = "86", isHighlight = true)
    }
}

@Composable
fun NewAttributeCard(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, score: String, isHighlight: Boolean = false) {
    val bgColor = if (isHighlight) Color(0xFFFDECE9) else Color.White
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = TextDarkMaroon, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkMaroon, textAlign = TextAlign.Center, lineHeight = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = score, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDarkMaroon, modifier = Modifier.alignByBaseline())
            Text(text = "/100", fontSize = 12.sp, color = TextMuted, modifier = Modifier.alignByBaseline())
        }
    }
}

@Composable
fun StyleOverviewCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(text = "Style Overview", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDarkMaroon)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PillTag(text = "Smart Casual", isPrimary = true)
            PillTag(text = "Minimal", isPrimary = false)
            PillTag(text = "Clean", isPrimary = false)
            PillTag(text = "Modern", isPrimary = false)
        }
    }
}

@Composable
fun PillTag(text: String, isPrimary: Boolean) {
    val bgColor = if (isPrimary) TextDarkMaroon else Color(0xFFF5F5F5)
    val textColor = if (isPrimary) Color.White else TextDarkMaroon
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(text = text, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

@Composable
fun BestForCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(text = "Best For", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDarkMaroon)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BestForItem(icon = Icons.Default.ShoppingBag, label = "Casual\nOuting")
            BestForItem(icon = Icons.Default.FavoriteBorder, label = "Date\nNight")
            BestForItem(icon = Icons.Default.Flight, label = "Travel")
            BestForItem(icon = Icons.Default.School, label = "College")
        }
    }
}

@Composable
fun BestForItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = TextDarkMaroon, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = TextDarkMaroon, lineHeight = 14.sp)
    }
}

@Composable
fun FeedbackCard(isPositive: Boolean, title: String, description: String) {
    val bgColor = if (isPositive) Color(0xFFEDF6E5) else Color(0xFFFDECE9)
    val iconColor = if (isPositive) Color(0xFF4CAF50) else Color(0xFFFF7043)
    val iconVector = Icons.Default.CheckCircle
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDarkMaroon)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, fontSize = 13.sp, color = TextDarkMaroon, lineHeight = 18.sp)
        }
    }
}


