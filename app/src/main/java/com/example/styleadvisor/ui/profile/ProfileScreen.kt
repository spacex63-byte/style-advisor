package com.example.styleadvisor.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextAlign
import android.content.Intent
import android.net.Uri
import androidx.navigation3.runtime.NavKey
import com.example.styleadvisor.HelpSupport
import com.example.styleadvisor.PrivacyPolicy
import com.example.styleadvisor.theme.*
import com.example.styleadvisor.R

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(onItemClick: (NavKey) -> Unit = {}, viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(context, it) }
    }
    
    LaunchedEffect(Unit) {
        viewModel.loadProfileImage(context)
    }

    if (showEditDialog) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var newName by remember { mutableStateOf(state.name) }
        var newBio by remember { mutableStateOf(state.bio) }
        
        ModalBottomSheet(
            onDismissRequest = { showEditDialog = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // Beautiful Profile Icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.profileImageUri != null) {
                        AsyncImage(
                            model = state.profileImageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = TextMuted.copy(alpha = 0.5f)
                        )
                    }
                    
                    // Camera overlay for editing
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to change photo",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = newBio,
                    onValueChange = { newBio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        viewModel.updateProfile(newName, newBio)
                        showEditDialog = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TextNavyBlue)
                ) {
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "Manage your style journey",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
            

        }
        
        Spacer(modifier = Modifier.height(18.dp))
        
        // User Profile Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(modifier = Modifier.clickable { showEditDialog = true }) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant)
                    ) {
                        if (state.profileImageUri != null) {
                            AsyncImage(
                                model = state.profileImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center).size(48.dp),
                                tint = TextMuted.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(20.dp))
                
                Column(modifier = Modifier.weight(1f).clickable { showEditDialog = true }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyBlue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Star, // Crown placeholder
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    
                    Text(
                        text = state.bio,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                        .clickable { showEditDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = TextNavyBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(19.dp))
            HorizontalDivider(color = SurfaceVariant, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(state.analysesCount.toString(), "Analyses")
                VerticalDivider(modifier = Modifier.height(32.dp), color = SurfaceVariant, thickness = 1.dp)
                StatItem(state.avgScore.toString(), "Avg. Score")
                VerticalDivider(modifier = Modifier.height(32.dp), color = SurfaceVariant, thickness = 1.dp)
                StatItem(state.outfitIdeas.toString(), "Outfit Ideas")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Style Profile Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF0F4FF))
                .clickable { onItemClick(com.example.styleadvisor.StyleProfile) }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, SurfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloth),
                    contentDescription = null,
                    tint = TextNavyBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy((-2).dp)
            ) {
                Text(
                    text = "Style Profile",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "Complete your style profile",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
            
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { state.styleProfileProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = ScoreHigh,
                    strokeWidth = 3.dp,
                    trackColor = SurfaceVariant,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Text(
                    text = "${(state.styleProfileProgress * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "My Overview",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Graph Card
        GraphCard(state)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Top Style Card
        TopStyleCard(state)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // More Section
        MoreSection(onItemClick = onItemClick)
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy((-4).dp)
    ) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}

@Composable
fun GraphCard(state: ProfileState) {
    val graphBg = Color(0xFFF0F4FA)
    val lineColor = Color(0xFF3C60F4)
    
    val animationProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(state.recentScores) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 1500, easing = androidx.compose.animation.core.FastOutSlowInEasing)
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(graphBg)
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Style Score",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7A99)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = state.avgScore.toString(),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B1527)
                        )
                        Text(
                            text = " /100",
                            fontSize = 16.sp,
                            color = Color(0xFF6B7A99),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropUp,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "12%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Canvas Graph
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    
                    val points = listOf(
                        0f to state.recentScores[0],
                        0.25f to state.recentScores[1],
                        0.5f to state.recentScores[2],
                        0.75f to state.recentScores[3],
                        1f to state.recentScores[4]
                    )
                    
                    val path = Path()
                    points.forEachIndexed { index, (xRatio, yRatio) ->
                        val x = xRatio * w
                        val y = yRatio * h
                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }
                    
                    val animatedPath = Path()
                    val pathMeasure = androidx.compose.ui.graphics.PathMeasure()
                    pathMeasure.setPath(path, false)
                    pathMeasure.getSegment(0f, pathMeasure.length * animationProgress.value, animatedPath, true)
                    
                    // Draw fill gradient
                    val fillPath = Path().apply {
                        addPath(animatedPath)
                        // Note: To properly close the fill, we must connect to the bottom axis
                        val currentLength = pathMeasure.length * animationProgress.value
                        val pos = pathMeasure.getPosition(currentLength)
                        if (pos != Offset.Unspecified) {
                            lineTo(pos.x, h)
                            lineTo(0f, h)
                            close()
                        }
                    }
                    
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(lineColor.copy(alpha = 0.2f), Color.Transparent)
                        )
                    )
                    
                    // Draw line
                    drawPath(
                        path = animatedPath,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx())
                    )
                    
                    // Draw points up to progress
                    points.forEachIndexed { index, (xRatio, yRatio) ->
                        if ((index / (points.size - 1).toFloat()) <= animationProgress.value) {
                            val x = xRatio * w
                            val y = yRatio * h
                            drawCircle(
                                color = lineColor,
                                radius = 5.dp.toPx(),
                                center = Offset(x, y)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 3.dp.toPx(),
                                center = Offset(x, y)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // X-axis labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val labels = listOf("May 1", "May 8", "May 15", "May 22", "May 29")
                labels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0B1527)
                    )
                }
            }
        }
    }
}

@Composable
fun TopStyleCard(state: ProfileState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Your Top Style",
                fontSize = 14.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = state.topStyle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "This is your most analyzed style",
                fontSize = 12.sp,
                color = TextMuted
            )
        }
        
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFFB9E5D9)), // Pastel Teal
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloth),
                contentDescription = null,
                tint = TextNavyBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun MoreSection(onItemClick: (NavKey) -> Unit = {}) {
    var showLogout by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "More",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            MoreItem(
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                title = "Help & Support",
                onClick = { onItemClick(HelpSupport) }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            MoreItem(
                icon = Icons.Default.StarBorder,
                title = "Rate Us",
                onClick = { /* TODO */ }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            MoreItem(
                icon = Icons.Default.Security,
                title = "Privacy Policy",
                onClick = { onItemClick(PrivacyPolicy) }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            MoreItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Logout",
                titleColor = Color(0xFFFF5722),
                iconColor = Color(0xFFFF5722),
                onClick = { showLogout = true }
            )
        }
    }

    if (showLogout) {
        Dialog(onDismissRequest = { showLogout = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF0ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Come back soon!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Are you sure you want to logout of your account?",
                        fontSize = 14.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { showLogout = false },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Cancel", color = TextNavyBlue, fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = { showLogout = false },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Logout", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoreItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    titleColor: Color = TextNavyBlue,
    iconColor: Color = TextNavyBlue,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = titleColor
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFA0A0A0),
            modifier = Modifier.size(20.dp)
        )
    }
}
