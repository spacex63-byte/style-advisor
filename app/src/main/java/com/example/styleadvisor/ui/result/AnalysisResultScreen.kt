package com.example.styleadvisor.ui.result

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*
import com.example.styleadvisor.model.AnalysisResult
import com.example.styleadvisor.ui.main.AnalysisState
import com.example.styleadvisor.ui.main.AnalysisViewModel
import com.example.styleadvisor.ui.main.PromoSection
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
                        if (imageUri != null) {
                            AsyncImage(
                                model = coil.request.ImageRequest.Builder(LocalContext.current)
                                    .data(imageUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Analyzing Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                alpha = 0.4f
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(16.dp))
                                .padding(24.dp)
                        ) {
                            CircularProgressIndicator(color = PrimaryBlue)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("AI is analyzing your photo...", color = TextNavyBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                is AnalysisState.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        OverviewContent(result = state.result, imageUri = imageUri)
                    }
                }
                is AnalysisState.Error -> {
                    LaunchedEffect(Unit) {
                        onBack()
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun AnimatedCard(delayMs: Int, content: @Composable () -> Unit) {
    var visible by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!visible) {
            kotlinx.coroutines.delay(delayMs.toLong())
            visible = true
        }
    }
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { 50 }, animationSpec = androidx.compose.animation.core.tween(500)) + androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(500))
    ) {
        content()
    }
}

@Composable
fun OverviewContent(result: AnalysisResult, imageUri: Uri?) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 0) { HeroScoreCard(result = result, imageUri = imageUri) }
        
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 150) { ScoreBreakdownCard(result = result) }
        
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 300) { StyleOverviewCard(result = result) }
        
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 450) { BestForCard(occasions = result.bestForOccasions) }
        
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 600) { OutfitElementsCard(elements = result.outfitElements) }
        
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 750) { DetectedColorsCard(colors = result.detectedColors, description = result.colorsDescription) }
        
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedCard(delayMs = 900) {
            FeedbackCard(
                isPositive = true,
                title = "What Looks Best",
                description = result.whatLooksBest
            )
        }
        
        if (result.overallScore < 90) {
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedCard(delayMs = 1050) {
                FeedbackCard(
                    isPositive = false,
                    title = "What Could Improve",
                    description = result.whatCouldImprove
                )
            }
        }
        
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
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationPlayed = true
    }
    
    val animatedScore by androidx.compose.animation.core.animateIntAsState(
        targetValue = if (animationPlayed) result.overallScore else 0,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "score"
    )
    val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (animationPlayed) result.overallScore / 100f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "progress"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(260.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Left Column: Score and text
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, topEnd = 0.dp, bottomEnd = 0.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular Score
            Box(
                modifier = Modifier.size(110.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
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
                        text = "$animatedScore",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue,
                        lineHeight = 32.sp
                    )
                    Text(
                        text = "/100",
                        fontSize = 14.sp,
                        color = TextNavyBlue,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                AutoResizeText(
                    text = result.shortTitle,
                    fontSize = 16.sp,
                    minFontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue,
                    minLines = 2,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = result.shortDescription,
                    fontSize = 12.sp,
                    color = TextNavyBlue,
                    lineHeight = 16.sp,
                    maxLines = 3,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
            }
        }
        
        // Right Column: Image
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 24.dp, bottomEnd = 24.dp))
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
                    painter = painterResource(id = R.drawable.sample_outfit_girl),
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
fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50) // Green
        score >= 50 -> Color(0xFFFFA000) // Orange
        else -> Color(0xFFE53935) // Red
    }
}

@Composable
fun NewAttributeCard(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, score: Int) {
    val scoreColor = getScoreColor(score)
    val bgColor = scoreColor.copy(alpha = 0.1f)
    
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
                modifier = Modifier.size(30.dp).clip(CircleShape).background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = scoreColor, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextNavyBlue, lineHeight = 14.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "$score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = scoreColor, modifier = Modifier.alignByBaseline())
            Text(text = "/100", fontSize = 12.sp, color = TextMuted, modifier = Modifier.alignByBaseline())
        }
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(2.5.dp)),
            color = scoreColor,
            trackColor = Color(0xFFF5F5F5),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
fun StyleOverviewCard(result: AnalysisResult) {
    var selectedTag by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    var triggerAnimations by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .animateContentSize()
            .onGloballyPositioned { coordinates ->
                if (!triggerAnimations) {
                    val yPos = coordinates.positionInWindow().y
                    if (yPos < screenHeightPx * 0.6f) { // 40% visible from bottom
                        triggerAnimations = true
                    }
                }
            }
            .padding(16.dp)
    ) {
        Text(text = "Style Overview", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(16.dp))
        @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            result.styleTags.forEachIndexed { index, tag ->
                var visible by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
                LaunchedEffect(triggerAnimations) {
                    if (!visible) {
                        kotlinx.coroutines.delay(300 + (index * 150).toLong())
                        visible = true
                    }
                }
                
                val scale by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = if (visible) 1f else 0f,
                    animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.6f, stiffness = 200f)
                )
                Box(modifier = Modifier.scale(scale)) {
                    PillTag(
                        text = tag, 
                        isPrimary = selectedTag == tag || (selectedTag == null && index == 0), 
                        onClick = { 
                            selectedTag = if (selectedTag == tag) null else tag 
                        }
                    )
                }
            }
        }
        
        androidx.compose.animation.AnimatedVisibility(
            visible = selectedTag != null,
            enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                val explanation = result.styleTagExplanations[selectedTag] ?: "A popular and stylish fashion choice."
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F8FA))
                        .padding(12.dp)
                ) {
                    Text(
                        text = explanation,
                        fontSize = 13.sp,
                        color = TextNavyBlue,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PillTag(text: String, isPrimary: Boolean, onClick: () -> Unit = {}) {
    val bgColor = if (isPrimary) TextNavyBlue else Color(0xFFF5F5F5)
    val textColor = if (isPrimary) Color.White else TextNavyBlue
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor)
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
            .padding(16.dp)
    ) {
        Text(text = "Best For", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(20.dp))
        @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val iconMap = mapOf(
                "Casual Outing" to Icons.Default.Coffee,
                "Date Night" to Icons.Default.FavoriteBorder,
                "Office/Work" to Icons.Default.Work,
                "Party" to Icons.Default.LocalBar,
                "Formal Event" to Icons.Default.Event,
                "Workout/Gym" to Icons.Default.FitnessCenter,
                "Travel" to Icons.Default.Flight,
                "Weekend Brunch" to Icons.Default.BrunchDining,
                "Beach/Resort" to Icons.Default.BeachAccess,
                "Wedding Guest" to Icons.Default.Celebration,
                "Concert/Festival" to Icons.Default.MusicNote,
                "Lounging at Home" to Icons.Default.Home,
                "Interview" to Icons.Default.CoPresent,
                "Night Club" to Icons.Default.Nightlife,
                "Outdoor Adventure" to Icons.Default.Terrain,
                "School/College" to Icons.Default.School,
                "Evening Dinner" to Icons.Default.Restaurant
            )
            
            occasions.take(4).forEachIndexed { index, occasion ->
                val icon = iconMap[occasion] ?: Icons.Default.Check
                BestForItem(icon = icon, label = occasion, isSelected = true)
            }
        }
    }
}

@Composable
fun BestForItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(16.dp)
                        .offset(x = (-2).dp, y = 2.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label.replace(" ", "\n"),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextNavyBlue,
            lineHeight = 14.sp,
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
            .padding(16.dp)
    ) {
        Text(text = "Score Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextNavyBlue)
        Spacer(modifier = Modifier.height(20.dp))
        
        ScoreBreakdownItem(icon = Icons.Default.Palette, label = "Color Match", score = result.colorHarmonyScore)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownItem(icon = Icons.Default.Person, label = "Clothing Fit", score = result.fitScore)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownItem(icon = Icons.Default.ThumbUp, label = "Fashion Style", score = result.styleScore)
        Spacer(modifier = Modifier.height(16.dp))
        ScoreBreakdownItem(icon = Icons.Default.AutoAwesome, label = "Overall Look", score = result.overallScore)
    }
}

@Composable
fun ScoreBreakdownItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, score: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationPlayed = true
    }
    
    val animatedScore by androidx.compose.animation.core.animateIntAsState(
        targetValue = if (animationPlayed) score else 0,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "score"
    )
    val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (animationPlayed) score / 100f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "progress"
    )

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
                Text(text = "$animatedScore", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue, modifier = Modifier.alignByBaseline())
                Text(text = "/100", fontSize = 12.sp, color = TextMuted, modifier = Modifier.alignByBaseline())
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = PrimaryBlue,
            trackColor = Color(0xFFF5F5F5),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
            drawStopIndicator = {}
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
            .padding(16.dp)
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
        
        Column(modifier = Modifier.fillMaxWidth()) {
            elements.forEach { element ->
                OutfitElementItem(element)
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
            .padding(16.dp)
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
        
        @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            colors.take(4).forEachIndexed { index, hex ->
                Box(
                    modifier = Modifier
                        .size(48.dp) // Made slightly larger to look better when spaced evenly
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

@Composable
fun AutoResizeText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    minFontSize: androidx.compose.ui.unit.TextUnit,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    var resizedFontSize by androidx.compose.runtime.remember(text) {
        androidx.compose.runtime.mutableStateOf(fontSize)
    }
    var shouldDraw by androidx.compose.runtime.remember(text) {
        androidx.compose.runtime.mutableStateOf(false)
    }
    val defaultFontSize = fontSize

    Text(
        text = text,
        color = color,
        fontWeight = fontWeight,
        fontSize = resizedFontSize,
        maxLines = maxLines,
        minLines = minLines,
        softWrap = true,
        lineHeight = resizedFontSize * 1.3f,
        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        onTextLayout = { result ->
            if (result.didOverflowHeight || result.didOverflowWidth) {
                if (resizedFontSize.value > minFontSize.value) {
                    resizedFontSize = (resizedFontSize.value * 0.95f).sp
                } else {
                    shouldDraw = true
                }
            } else {
                shouldDraw = true
            }
        }
    )
}
