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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.example.styleadvisor.TipDetail
import com.example.styleadvisor.theme.*
import com.example.styleadvisor.R

data class TipCategory(val name: String, val icon: ImageVector)

val tipCategories = listOf(
    TipCategory("All", Icons.Default.AutoAwesome),
    TipCategory("Saved", Icons.Default.Bookmark),
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
    val image: Any
)

val featuredTips = listOf(
    TipData("Build Elegant Outfits Effortlessly", "A timeless wardrobe is built with versatile essentials that always look polished...", "Outfit", BadgePurpleBg, BadgePurpleText, R.drawable.tip_1),
    TipData("Build Elegant Outfits Effortlessly", "An elegant wardrobe is built around timeless pieces that can be mixed and matched...", "Outfit", BadgePurpleBg, BadgePurpleText, R.drawable.men_tip_1)
)

val popularTips = listOf(
    TipData("Colors That Brighten Your Look", "The right color combinations make your outfit look brighter, more luxurious...", "Colors", BadgeOrangeBg, BadgeOrangeText, R.drawable.tip_2),
    TipData("Dress for Your Body Shape", "The perfect fit enhances your natural shape while keeping your style comfortable...", "Fit", BadgeGreenBg, BadgeGreenText, R.drawable.tip_3),
    TipData("Beauty Habits That Elevate Your Style", "Beautiful style begins with healthy grooming habits that complement your clothing.", "Grooming", BadgeGreenBg, BadgeGreenText, R.drawable.tip_4),
    TipData("Accessories That Complete Every Look", "Accessories add personality and elegance without overwhelming your outfit.", "Accessories", BadgeYellowBg, BadgeYellowText, R.drawable.tip_5),
    TipData("Colors That Brighten Your Look", "The right colors can make your skin appear brighter and your outfit look more luxurious...", "Colors", BadgeOrangeBg, BadgeOrangeText, R.drawable.men_tip_2),
    TipData("Dress for Your Body Shape", "The best outfits highlight your natural proportions while remaining comfortable...", "Fit", BadgeGreenBg, BadgeGreenText, R.drawable.men_tip_3),
    TipData("Accessories That Complete Every Look", "Accessories are the finishing touch that transform a simple outfit into a premium look.", "Accessories", BadgeYellowBg, BadgeYellowText, R.drawable.men_tip_4),
    TipData("Beauty Habits That Elevate Your Style", "Beautiful style begins with healthy grooming habits that complement your clothing.", "Grooming", BadgeGreenBg, BadgeGreenText, R.drawable.men_tip_5)
)

@Composable
fun TipsContent(onItemClick: (NavKey) -> Unit = {}) {
    var selectedCategory by remember { mutableStateOf("All") }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val bookmarkedTips by BookmarksManager.bookmarkedTips.collectAsState()

    val filteredFeaturedTips = featuredTips.filter { tip ->
        val matchesSearch = searchQuery.isBlank() || tip.title.contains(searchQuery, ignoreCase = true) || tip.subtitle.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || (selectedCategory == "Saved" && bookmarkedTips.contains(tip.title)) || tip.category == selectedCategory
        matchesSearch && matchesCategory
    }

    val filteredPopularTips = popularTips.filter { tip ->
        val matchesSearch = searchQuery.isBlank() || tip.title.contains(searchQuery, ignoreCase = true) || tip.subtitle.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || (selectedCategory == "Saved" && bookmarkedTips.contains(tip.title)) || tip.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(4.dp))
        
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    placeholder = { Text("Search tips...") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                IconButton(onClick = { 
                    isSearchActive = false
                    searchQuery = "" 
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Search", tint = TextNavyBlue)
                }
            } else {
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
                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextNavyBlue
                        )
                    }
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
            if (filteredFeaturedTips.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Tips",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
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
                    filteredFeaturedTips.forEach { tip ->
                        FeaturedTipCard(tip = tip, onClick = { 
                            onItemClick(TipDetail(
                                title = tip.title, 
                                category = tip.category,
                                imageRes = tip.image as? Int,
                                imageUrl = tip.image as? String
                            )) 
                        })
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            if (filteredPopularTips.isNotEmpty()) {
                Text(
                    text = "Popular Tips",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavyBlue,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    filteredPopularTips.forEach { tip ->
                        TipCard(
                            tip = tip, 
                            isBookmarked = bookmarkedTips.contains(tip.title),
                            onBookmarkClick = { BookmarksManager.toggleBookmark(tip.title) },
                            onClick = { 
                                onItemClick(TipDetail(
                                    title = tip.title, 
                                    category = tip.category,
                                    imageRes = tip.image as? Int,
                                    imageUrl = tip.image as? String
                                )) 
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No tips found.", color = TextMuted)
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
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(4.dp)
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
            fontSize = 11.sp,
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
                model = tip.image,
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
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue,
                        lineHeight = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tip.subtitle,
                        fontSize = 11.sp,
                        color = TextMuted,
                        lineHeight = 16.sp
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
                            fontSize = 10.sp,
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
fun TipCard(tip: TipData, isBookmarked: Boolean, onBookmarkClick: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = tip.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(100.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f).height(120.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = tip.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = TextNavyBlue,
                        modifier = Modifier.size(24.dp).clickable { onBookmarkClick() }
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = tip.subtitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            }
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(tip.bgCol)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tip.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = tip.txtCol
                )
            }
        }
    }
}
