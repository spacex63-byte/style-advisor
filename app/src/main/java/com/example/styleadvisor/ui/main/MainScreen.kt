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
import androidx.compose.ui.draw.shadow
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
import androidx.compose.animation.*
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.styleadvisor.ui.profile.ProfileContent

enum class BottomTab {
    HOME, PROFILE
}

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalysisViewModel? = null
) {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && tempImageUri != null) {
                selectedImageUri = tempImageUri
                isAnalyzing = true
            }
        }
    )
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val file = context.createImageFile()
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                tempImageUri = uri
                cameraLauncher.launch(uri)
            }
        }
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        bottomBar = { 
            CustomBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onCameraClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) }
            ) 
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GlobalBackgroundGradient)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                    }
                },
                label = "tab_transition"
            ) { targetTab ->
                when (targetTab) {
                    BottomTab.HOME -> HomeContent(
                        onItemClick = onItemClick,
                        selectedImageUri = selectedImageUri,
                        onImageSelected = { selectedImageUri = it },
                        isAnalyzing = isAnalyzing,
                        onAnalyzeStarted = { 
                            isAnalyzing = true
                            selectedImageUri?.let { uri ->
                                viewModel?.analyzeImage(context, uri)
                            }
                        },
                        onAnalyzeComplete = {
                            isAnalyzing = false
                            onItemClick(AnalysisResult)
                        }
                    )
                    BottomTab.PROFILE -> ProfileContent()
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    onItemClick: (NavKey) -> Unit,
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    isAnalyzing: Boolean,
    onAnalyzeStarted: () -> Unit,
    onAnalyzeComplete: () -> Unit
) {
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
        AnalyzeButtonSection(
            selectedImageUri = selectedImageUri,
            onImageSelected = onImageSelected,
            isAnalyzing = isAnalyzing,
            onAnalyzeStarted = onAnalyzeStarted,
            onAnalyzeComplete = onAnalyzeComplete
        )
        
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
fun AnalyzeButtonSection(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    isAnalyzing: Boolean,
    onAnalyzeStarted: () -> Unit,
    onAnalyzeComplete: () -> Unit
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            if (uri != null) {
                onImageSelected(uri)
                onAnalyzeStarted()
            }
        }
    )
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && tempImageUri != null) {
                onImageSelected(tempImageUri)
                onAnalyzeStarted()
            }
        }
    )
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val file = context.createImageFile()
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                tempImageUri = uri
                cameraLauncher.launch(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedImageUri != null) {
            // Image Container
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(4.dp, Color.White, RoundedCornerShape(32.dp))
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFFEBE3DE))
                    .clickable { onImageSelected(null) }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Uploaded Fashion",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Scanning Line Animation
                val infiniteTransition = rememberInfiniteTransition(label = "scan")
                val scanLineY by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scan_line_y"
                )
                
                var isGoingDown by remember { mutableStateOf(true) }
                var previousScanY by remember { mutableStateOf(0f) }
                LaunchedEffect(scanLineY) {
                    if (scanLineY != previousScanY) {
                        isGoingDown = scanLineY > previousScanY
                        previousScanY = scanLineY
                    }
                }
                
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Light Black Overlay
                    drawRect(color = Color.Black.copy(alpha = 0.3f))
                    
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
                    
                    // Deep Ambient Glow
                    val currentY = scanLineY * size.height
                    val scanColor = Color(0xFF5A75FF)
                    
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(scanColor.copy(alpha = 0.01f), Color.Transparent),
                            center = Offset(size.width / 2, currentY),
                            radius = 500f
                        ),
                        topLeft = Offset(0f, currentY - 500f),
                        size = androidx.compose.ui.geometry.Size(size.width, 1000f)
                    )
                    
                    // Draw Scan Line (core)
                    drawLine(
                        color = scanColor,
                        start = Offset(0f, currentY),
                        end = Offset(size.width, currentY),
                        strokeWidth = 4.dp.toPx()
                    )
                    
                    // Draw Trailing Bleed
                    if (isGoingDown) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, scanColor.copy(alpha = 0.6f)),
                                startY = currentY - 150f,
                                endY = currentY
                            ),
                            topLeft = Offset(0f, currentY - 150f),
                            size = androidx.compose.ui.geometry.Size(size.width, 150f)
                        )
                    } else {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(scanColor.copy(alpha = 0.6f), Color.Transparent),
                                startY = currentY,
                                endY = currentY + 150f
                            ),
                            topLeft = Offset(0f, currentY),
                            size = androidx.compose.ui.geometry.Size(size.width, 150f)
                        )
                    }
                }
                
                // AI Analyzing Container Overlaid at the bottom of the image
                Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isAnalyzing,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut()
                    ) {
                        AIAnalyzingContainer(
                            onComplete = {
                                onAnalyzeComplete()
                            }
                        )
                    }
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
                    .clickable { 
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
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
                .clickable { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Take Photo",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Take Photo",
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
                .clickable {
                    val uri = android.net.Uri.parse("android.resource://${context.packageName}/${com.example.styleadvisor.R.drawable.sample_outfit}")
                    onImageSelected(uri)
                    onAnalyzeStarted()
                },
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
fun CustomBottomBar(
    selectedTab: BottomTab, 
    onTabSelected: (BottomTab) -> Unit,
    onCameraClick: () -> Unit
) {
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
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    ambientColor = PrimaryBlue,
                    spotColor = PrimaryBlue
                )
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onCameraClick() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
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

@Composable
fun AIAnalyzingContainer(onComplete: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
        ) { value, _ ->
            progress = value
        }
        kotlinx.coroutines.delay(500)
        onComplete()
    }

}

fun android.content.Context.createImageFile(): java.io.File {
    val timeStamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = java.io.File(cacheDir, "images")
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }
    return java.io.File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}
