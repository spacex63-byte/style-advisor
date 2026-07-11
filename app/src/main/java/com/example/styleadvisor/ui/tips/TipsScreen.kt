package com.example.styleadvisor.ui.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@Composable
fun TipsContent() {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Outfit", "Colors", "Fit", "Grooming", "Accessories")

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        
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
                    text = "Style Tips",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkMaroon
                )
                Text(
                    text = "Expert tips to upgrade your style",
                    fontSize = 14.sp,
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
                IconButton(onClick = { /* Search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextDarkMaroon
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Category Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categories.forEach { category ->
                CategoryChip(
                    text = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "All Tips",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkMaroon,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TipCard(
                title = "Layering Tips for Every Season",
                subtitle = "Layer smart, stay stylish.",
                category = "Outfit",
                bgCol = BadgePurpleBg,
                txtCol = BadgePurpleText
            )
            
            TipCard(
                title = "Neutral Tones Never Fail",
                subtitle = "Why neutrals are the foundation of great style.",
                category = "Colors",
                bgCol = BadgeOrangeBg,
                txtCol = BadgeOrangeText
            )
            
            TipCard(
                title = "White Sneakers:\nStyle Essential",
                subtitle = "How to style white sneakers\nwith anything.",
                category = "Footwear",
                bgCol = BadgeBlueBg,
                txtCol = BadgeBlueText
            )
            
            TipCard(
                title = "Grooming Habits That\nChange Your Look",
                subtitle = "Small habits, big impact.",
                category = "Grooming",
                bgCol = BadgeGreenBg,
                txtCol = BadgeGreenText
            )
            
            TipCard(
                title = "Accessories That\nElevate Your Outfit",
                subtitle = "The right accessories make\nall the difference.",
                category = "Accessories",
                bgCol = BadgeYellowBg,
                txtCol = BadgeYellowText
            )
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) TextDarkMaroon else SurfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.SemiBold,
            color = if (isSelected) Color.White else TextMuted
        )
    }
}

@Composable
fun TipCard(title: String, subtitle: String, category: String, bgCol: Color, txtCol: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center).size(48.dp),
                tint = TextMuted.copy(alpha = 0.3f)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkMaroon,
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = TextDarkMaroon,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgCol)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = txtCol
                )
            }
        }
    }
}
