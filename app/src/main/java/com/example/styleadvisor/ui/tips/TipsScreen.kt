package com.example.styleadvisor.ui.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.example.styleadvisor.TipDetail
import com.example.styleadvisor.theme.*

data class TipCategory(val name: String, val icon: ImageVector)

val tipCategories = listOf(
    TipCategory("All", Icons.Default.AutoAwesome),
    TipCategory("Outfit", Icons.Default.Checkroom),
    TipCategory("Colors", Icons.Default.Palette),
    TipCategory("Fit", Icons.Default.Straighten),
    TipCategory("Grooming", Icons.Default.Face),
    TipCategory("Accessories", Icons.Default.Watch)
)

data class TipData(
    val title: String,
    val subtitle: String,
    val category: String,
    val bgCol: Color,
    val txtCol: Color,
    val imageUrl: String
)

val featuredTips = listOf(
    TipData("5 Color Combinations That Always Work", "Learn how to combine colors like a pro and elevate your look.", "Colors", BadgeOrangeBg, BadgeOrangeText, "https://images.unsplash.com/photo-1516257984-b1b4d707412e?q=80&w=600&auto=format&fit=crop"),
    TipData("Essential Wardrobe Pieces", "Build a versatile foundation for any season.", "Outfit", BadgePurpleBg, BadgePurpleText, "https://images.unsplash.com/photo-1489987707023-afc31613f8c8?q=80&w=600&auto=format&fit=crop")
)

val popularTips = listOf(
    TipData("How to Build a Capsule Wardrobe", "Essential pieces for effortless style every day.", "Outfit", BadgePurpleBg, BadgePurpleText, "https://images.unsplash.com/photo-1552374196-1ab2a1c593e8?q=80&w=400&auto=format&fit=crop"),
    TipData("Perfect Fit Guide for Every Body Type", "Look sharp by wearing the right fit.", "Fit", BadgeGreenBg, BadgeGreenText, "https://images.unsplash.com/photo-1507679799987-c73779587ccf?q=80&w=400&auto=format&fit=crop"),
    TipData("White Sneakers: Style Essential", "How to style white sneakers with anything.", "Outfit", BadgeBlueBg, BadgeBlueText, "https://images.unsplash.com/photo-1549298916-b41d501d3772?q=80&w=400&auto=format&fit=crop"),
    TipData("Grooming Habits That Change Your Look", "Small habits, big impact.", "Grooming", BadgeGreenBg, BadgeGreenText, "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?q=80&w=400&auto=format&fit=crop"),
    TipData("Accessories That Elevate Your Outfit", "The right accessories make all the difference.", "Accessories", BadgeYellowBg, BadgeYellowText, "https://images.unsplash.com/photo-1523170335258-f5ed11844a49?q=80&w=400&auto=format&fit=crop")
)

@Composable
fun TipsContent(onItemClick: (NavKey) -> Unit = {}) {
    var selectedCategory by remember { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "Style Tips",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "Expert tips to upgrade your style",
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
                IconButton(onClick = { /* Search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextNavyBlue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Category Chips (Icon + Text)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            tipCategories.forEach { category ->
                CategoryIconChip(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { selectedCategory = category.name }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Featured Tips Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Tips",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue
                )
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = TextMuted,
                    modifier = Modifier.clickable { }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                featuredTips.forEach { tip ->
                    FeaturedTipCard(tip = tip, onClick = { onItemClick(TipDetail(tip.title, tip.category)) })
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Popular Tips",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                popularTips.forEach { tip ->
                    TipCard(tip = tip, onClick = { onItemClick(TipDetail(tip.title, tip.category)) })
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun CategoryIconChip(category: TipCategory, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSelected) TextNavyBlue else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) Color.White else TextNavyBlue,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TextNavyBlue else TextMuted
        )
    }
}

@Composable
fun FeaturedTipCard(tip: TipData, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row {
            // Image
            AsyncImage(
                model = tip.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(
                modifier = Modifier.height(160.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = tip.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tip.subtitle,
                        fontSize = 13.sp,
                        color = TextMuted,
                        lineHeight = 18.sp
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(tip.bgCol)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tip.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = tip.txtCol
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(TextNavyBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Read",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TipCard(tip: TipData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = tip.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = tip.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue,
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = TextNavyBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = tip.subtitle,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(tip.bgCol)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tip.category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = tip.txtCol
                )
            }
        }
    }
}
