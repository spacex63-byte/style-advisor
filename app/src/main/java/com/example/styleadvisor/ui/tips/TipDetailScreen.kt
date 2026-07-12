package com.example.styleadvisor.ui.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.styleadvisor.theme.*

@Composable
fun TipDetailScreen(
    title: String,
    category: String,
    imageRes: Int? = null,
    onBack: () -> Unit
) {
    // We'll use a placeholder image for the hero
    val heroImageUrl = "https://images.unsplash.com/photo-1516257984-b1b4d707412e?q=80&w=1200&auto=format&fit=crop"
    
    val categoryColors = when(category) {
        "Colors" -> Pair(BadgeOrangeBg, BadgeOrangeText)
        "Outfit" -> Pair(BadgePurpleBg, BadgePurpleText)
        "Fit" -> Pair(BadgeGreenBg, BadgeGreenText)
        "Grooming" -> Pair(BadgeGreenBg, BadgeGreenText)
        "Accessories" -> Pair(BadgeYellowBg, BadgeYellowText)
        else -> Pair(BadgeBlueBg, BadgeBlueText)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Hero Image
        AsyncImage(
            model = heroImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
        
        // Top Action Bar Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextNavyBlue
                )
            }
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = TextNavyBlue
                )
            }
        }
        
        // Content Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 350.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(categoryColors.first)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = category,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = categoryColors.second
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue,
                lineHeight = 34.sp
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Author info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Style Expert",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyBlue
                        )
                        Text(
                            text = "Fashion Advisor",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "May 12, 2025",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "5 min read",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Color combinations can make or break your outfit. Here are 5 timeless pairings that always create a stylish impression.",
                fontSize = 15.sp,
                color = TextNavyBlue.copy(alpha = 0.8f),
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Step 1
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "1. Navy Blue + White",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Classic, clean, and perfect for any occasion. It gives a fresh and confident look.",
                        fontSize = 14.sp,
                        color = TextMuted,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF0F2044)))
                        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFE8E9EB)))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1594938298596-eb5fd3f56ce2?q=80&w=200&auto=format&fit=crop",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Step 2
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "2. Black + Beige",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Effortless and sophisticated. This combination never goes out of style.",
                        fontSize = 14.sp,
                        color = TextMuted,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color.Black))
                        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFDCC2AC)))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1617137984095-74e4e5e3613f?q=80&w=200&auto=format&fit=crop",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                )
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
