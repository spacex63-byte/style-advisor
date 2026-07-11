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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.styleadvisor.R
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
import com.example.styleadvisor.model.AnalysisResult
import com.example.styleadvisor.ui.main.AnalysisState
import com.example.styleadvisor.ui.main.AnalysisViewModel
import coil.compose.AsyncImage
import android.net.Uri

@Composable
fun AnalysisResultScreen(
    onBack: () -> Unit,
    viewModel: AnalysisViewModel? = null
) {
    val uiState by viewModel?.uiState?.collectAsState(initial = AnalysisState.Idle) ?: remember { mutableStateOf(AnalysisState.Idle) }
    val imageUri by viewModel?.selectedImageUri?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize().background(GlobalBackgroundGradient)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ResultTopBar(
                        title = "Analysis Result",
                        onBack = onBack
                    )
                }
            }
        ) { innerPadding ->
            when (val state = uiState) {
                is AnalysisState.Analyzing -> {
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = PrimaryBlue)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("AI is analyzing your style...", color = TextNavyBlue, fontSize = 16.sp)
                        }
                    }
                }
                is AnalysisState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        OverviewContent(result = state.result, imageUri = imageUri)
                    }
                }
                is AnalysisState.Error -> {
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${state.message}", color = Color.Red)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun OverviewContent(result: AnalysisResult, imageUri: Uri?) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        HeroScoreCard(result = result, imageUri = imageUri)
        
        Spacer(modifier = Modifier.height(24.dp))
        NewAttributeGrid(result = result)
        
        Spacer(modifier = Modifier.height(16.dp))
        StyleOverviewCard(tags = result.styleTags)
        
        Spacer(modifier = Modifier.height(16.dp))
        BestForCard(occasions = result.bestForOccasions)
        
        Spacer(modifier = Modifier.height(16.dp))
        FeedbackCard(
            isPositive = true,
            title = "What Looks Best",
            description = result.whatLooksBest
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        FeedbackCard(
            isPositive = false,
            title = "What Could Improve",
            description = result.whatCouldImprove
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownCard(result = result)
        
        Spacer(modifier = Modifier.height(16.dp))
        OutfitElementsCard(elements = result.outfitElements)
        
        Spacer(modifier = Modifier.height(16.dp))
        DetectedColorsCard(colors = result.detectedColors, description = result.colorsDescription)
        
        Spacer(modifier = Modifier.height(16.dp))
        PersonalizedTipsCard(tips = result.personalizedTips)
        
        Spacer(modifier = Modifier.height(16.dp))
        UpgradeProCard()
        
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
            color = TextNavyBlue
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
            color = TextNavyBlue
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
                .background(BluePillBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = ScoreTextBlue)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextNavyBlue)
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
                    tint = ScoreTextBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Your Look", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextNavyBlue)
            Text(text = "Upgraded Look", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextNavyBlue)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ScoreTextBlue.copy(alpha = 0.5f)))
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
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextNavyBlue
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextNavyBlue
            )
        }
        
        Box(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun HeroScoreCard(result: AnalysisResult, imageUri: Uri?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Left Column: Score and text
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Circular Score
            Box(
                modifier = Modifier.size(90.dp).align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { result.overallScore / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = PrimaryBlue,
                    trackColor = Color(0xFFF5F5F5),
                    strokeWidth = 8.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(y = 2.dp)
                ) {
                    Text(
                        text = "${result.overallScore}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue,
                        lineHeight = 28.sp
                    )
                    Text(
                        text = "/100",
                        fontSize = 12.sp,
                        color = TextNavyBlue,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = result.shortTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Great Look!", fontSize = 11.sp, color = PrimaryBlue, fontWeight = FontWeight.Medium)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = result.shortDescription,
                fontSize = 12.sp,
                color = TextNavyBlue,
                lineHeight = 16.sp
            )
        }
        
        // Right Column: Image
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceVariant)
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Analyzed image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.sample_outfit),
                    contentDescription = "Analyzed image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun NewAttributeGrid(result: AnalysisResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NewAttributeCard(modifier = Modifier.weight(1f).height(120.dp), icon = Icons.Default.Palette, label = "Color\nHarmony", score = result.colorHarmonyScore)
        NewAttributeCard(modifier = Modifier.weight(1f).height(120.dp), icon = Icons.Default.Person, label = "Fit", score = result.fitScore)
        NewAttributeCard(modifier = Modifier.weight(1f).height(120.dp), icon = Icons.Default.ThumbUp, label = "Style", score = result.styleScore)
    }
}

@Composable
fun NewAttributeCard(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, score: Int) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(28.dp).clip(CircleShape).background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = TextNavyBlue, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextNavyBlue, lineHeight = 14.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "$score", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue, modifier = Modifier.alignByBaseline())
            Text(text = "/100", fontSize = 12.sp, color = TextMuted, modifier = Modifier.alignByBaseline())
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = PrimaryBlue,
            trackColor = Color(0xFFF5F5F5),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
fun StyleOverviewCard(tags: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(text = "Style Overview", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEachIndexed { index, tag ->
                PillTag(text = tag, isPrimary = index == 0)
            }
        }
    }
}

@Composable
fun PillTag(text: String, isPrimary: Boolean) {
    val bgColor = if (isPrimary) TextNavyBlue else Color(0xFFF5F5F5)
    val textColor = if (isPrimary) Color.White else TextNavyBlue
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
fun BestForCard(occasions: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(text = "Best For", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val iconMap = mapOf(
                "Casual Outing" to Icons.Default.Coffee,
                "Date Night" to Icons.Default.FavoriteBorder,
                "Travel" to Icons.Default.Flight,
                "College" to Icons.Default.School,
                "Work" to Icons.Default.Work,
                "Party" to Icons.Default.LocalBar
            )
            occasions.take(3).forEach { occasion ->
                BestForItem(icon = iconMap[occasion] ?: Icons.Default.Check, label = occasion)
            }
        }
    }
}

@Composable
fun BestForItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label.replace(" ", "\n"),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextNavyBlue,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FeedbackCard(isPositive: Boolean, title: String, description: String) {
    val bgColor = if (isPositive) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
    val iconColor = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)
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
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 13.sp, color = TextNavyBlue, lineHeight = 18.sp)
        }
    }
}

@Composable
fun ScoreBreakdownCard(result: AnalysisResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(text = "Score Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(20.dp))
        
        ScoreBreakdownItem(icon = Icons.Default.Palette, label = "Color Harmony", score = result.colorHarmonyScore)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownItem(icon = Icons.Default.Person, label = "Fit", score = result.fitScore)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownItem(icon = Icons.Default.ThumbUp, label = "Style", score = result.styleScore)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownItem(icon = Icons.Default.AutoAwesome, label = "Overall Impression", score = result.overallScore)
    }
}

@Composable
fun ScoreBreakdownItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, score: Int) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = TextNavyBlue, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextNavyBlue)
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "$score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue, modifier = Modifier.alignByBaseline())
                Text(text = "/100", fontSize = 12.sp, color = TextMuted, modifier = Modifier.alignByBaseline())
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = PrimaryBlue,
            trackColor = Color(0xFFF5F5F5),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
fun OutfitElementsCard(elements: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Checkroom, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Outfit Elements", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        // 2 columns layout
        val chunked = elements.chunked(maxOf(1, (elements.size + 1) / 2))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                chunked.getOrNull(0)?.forEach { element ->
                    OutfitElementItem(element)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                chunked.getOrNull(1)?.forEach { element ->
                    OutfitElementItem(element)
                }
            }
        }
    }
}

@Composable
fun OutfitElementItem(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(PrimaryBlue))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontSize = 13.sp, color = TextNavyBlue)
    }
}

@Composable
fun DetectedColorsCard(colors: List<String>, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Detected Colors", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            colors.forEach { hex ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(if (hex.startsWith("#")) hex else "#$hex")))
                        .border(1.dp, Color(0xFFF0F0F0), CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = description, fontSize = 13.sp, color = TextNavyBlue)
    }
}

@Composable
fun PersonalizedTipsCard(tips: List<com.example.styleadvisor.model.Tip>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Personalized Tips", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        val icons = listOf(Icons.Default.Watch, Icons.Default.Layers, Icons.Default.DirectionsWalk)
        tips.forEachIndexed { index, tip ->
            PersonalizedTipItem(
                icon = icons.getOrElse(index) { Icons.Default.Check },
                title = tip.title,
                description = tip.description
            )
            if (index < tips.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PersonalizedTipItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF5F8FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextNavyBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 12.sp, color = TextMuted, lineHeight = 16.sp)
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextNavyBlue)
    }
}

@Composable
fun UpgradeProCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF5F8FF))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Want More Personalized Tips?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Unlock advanced insights tailored to\nyour style & body type.", fontSize = 11.sp, color = TextMuted, lineHeight = 14.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(TextNavyBlue)
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Upgrade to Pro", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
    }
}
