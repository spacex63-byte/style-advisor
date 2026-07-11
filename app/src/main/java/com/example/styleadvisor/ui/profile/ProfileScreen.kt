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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@Composable
fun ProfileContent() {
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
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "Manage your style journey",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { /* Settings */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextNavyBlue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // User Profile Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar with Camera Badge
                Box {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center).size(48.dp),
                            tint = TextMuted.copy(alpha = 0.3f)
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(TextNavyBlue)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(20.dp))
                
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tavorian",
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
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = "Style Explorer",
                        fontSize = 14.sp,
                        color = TextMuted
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(TextNavyBlue)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Premium",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SurfaceVariant, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("23", "Analyses")
                VerticalDivider(modifier = Modifier.height(32.dp), color = SurfaceVariant, thickness = 1.dp)
                StatItem("87", "Avg. Score")
                VerticalDivider(modifier = Modifier.height(32.dp), color = SurfaceVariant, thickness = 1.dp)
                StatItem("12", "Outfit Ideas")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Style Profile Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, SurfaceVariant, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Checkroom, contentDescription = null, tint = TextNavyBlue)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Style Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "Complete your style profile",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
            
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 0.85f },
                    modifier = Modifier.fillMaxSize(),
                    color = ScoreHigh,
                    strokeWidth = 3.dp,
                    trackColor = SurfaceVariant
                )
                Text(
                    text = "85%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "My Overview",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Graph Card
        GraphCard()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Top Style Card
        TopStyleCard()
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextNavyBlue
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}

@Composable
fun GraphCard() {
    val graphBg = Brush.verticalGradient(
        colors = listOf(Color(0xFF351C1A), Color(0xFF1E100D))
    )
    val lineColor = Color(0xFFEBC77D)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp)
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
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "87",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = " /100",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropUp,
                                contentDescription = null,
                                tint = ScoreHigh,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "12%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ScoreHigh
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Canvas Graph
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    
                    val points = listOf(
                        0f to 0.8f,
                        0.15f to 0.7f,
                        0.3f to 0.75f,
                        0.45f to 0.5f,
                        0.6f to 0.45f,
                        0.75f to 0.5f,
                        0.9f to 0.3f,
                        1f to 0.2f
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
                    
                    // Draw fill gradient
                    val fillPath = Path().apply {
                        addPath(path)
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                    
                    // Draw line
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx())
                    )
                    
                    // Draw points
                    points.forEach { (xRatio, yRatio) ->
                        val x = xRatio * w
                        val y = yRatio * h
                        drawCircle(
                            color = Color.White,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = lineColor,
                            radius = 3.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // X-axis labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val labels = listOf("May 1", "May 8", "May 15", "May 22", "May 29")
                labels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun TopStyleCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp),
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
                text = "Smart Casual",
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
                imageVector = Icons.Default.Checkroom, // Suit jacket equivalent placeholder
                contentDescription = null,
                tint = TextNavyBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
