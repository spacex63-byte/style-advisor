package com.example.styleadvisor.ui.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.styleadvisor.theme.*

data class TipContentInfo(val description: String, val steps: List<String>, val proTip: String, val recommendedColors: String? = null)

val tipDatabase = mapOf(
    "Build Elegant Outfits Effortlessly" to TipContentInfo(
        description = "A timeless wardrobe is built with versatile essentials that always look polished and elegant.",
        steps = listOf(
            "Choose classic colors like white, black, beige, cream, navy, and soft gray.",
            "Pair a crisp white blouse with tailored black trousers for a sophisticated everyday outfit.",
            "High-waisted trousers create a longer, more balanced silhouette.",
            "Carry a structured black or beige handbag for a premium finish.",
            "Complete the outfit with pointed heels or elegant flats and delicate jewelry."
        ),
        recommendedColors = "White + Black • White + Beige • Cream + Black • White + Navy • Beige + Camel",
        proTip = "Classic neutral colors never go out of style and are easy to mix together."
    ),
    "Colors That Brighten Your Look" to TipContentInfo(
        description = "The right color combinations make your outfit look brighter, more luxurious, and naturally stylish.",
        steps = listOf(
            "Use black as the statement color and balance it with cream or beige trousers.",
            "Neutral bottoms make colorful tops easier to style.",
            "White sneakers create a clean casual finish.",
            "Keep accessories in silver, white, or beige for harmony.",
            "Avoid wearing more than three dominant colors together."
        ),
        recommendedColors = "Black + Cream • Black + Beige • White + Beige • Olive + Cream • Brown + White",
        proTip = "Neutral outfits always look more expensive than overly colorful combinations."
    ),
    "Dress for Your Body Shape" to TipContentInfo(
        description = "The perfect fit enhances your natural shape while keeping your style comfortable and elegant.",
        steps = listOf(
            "Choose high-waisted trousers to lengthen your legs.",
            "Tuck soft blouses into tailored pants for balanced proportions.",
            "Relaxed trousers pair beautifully with fitted or semi-fitted tops.",
            "Flowing fabrics create graceful movement.",
            "Minimal gold jewelry keeps the outfit feminine and refined."
        ),
        recommendedColors = "Cream + White • Beige + White • Camel + Ivory • White + Taupe • Sand + Cream",
        proTip = "Well-fitted clothing creates a stronger impression than expensive brands."
    ),
    "Accessories That Complete Every Look" to TipContentInfo(
        description = "Accessories add personality and elegance without overwhelming your outfit.",
        steps = listOf(
            "Carry a structured shoulder bag in black, beige, or brown.",
            "Wear delicate necklaces and small earrings for a refined appearance.",
            "Match your handbag with your shoes whenever possible.",
            "Choose classic heels or loafers instead of overly trendy footwear.",
            "Keep accessories simple and coordinated."
        ),
        recommendedColors = "Black + Black • Black + Beige • White + Black • Cream + Brown • Beige + Gold",
        proTip = "One quality handbag and a few timeless accessories are enough for countless outfits."
    ),
    "Beauty Habits That Elevate Your Style" to TipContentInfo(
        description = "Beautiful style begins with healthy grooming habits that complement your clothing.",
        steps = listOf(
            "Keep your hair healthy, shiny, and neatly styled.",
            "Choose fresh, natural makeup that enhances your features.",
            "Always wear wrinkle-free, well-fitted clothing.",
            "Maintain clean nails and subtle fragrances.",
            "A classic watch and elegant handbag complete a polished appearance."
        ),
        recommendedColors = "Black + White • Black + Cream • White + Beige • Black + Camel • White + Gray",
        proTip = "Confidence comes from looking clean, healthy, and comfortable in your outfit."
    ),
    "5 Color Combinations That Always Work" to TipContentInfo(
        description = "Color combinations can make or break your outfit. Here are timeless pairings that always create a stylish impression.",
        steps = listOf(
            "Navy Blue + White: Classic, clean, and perfect for any occasion. It gives a fresh and confident look.",
            "Black + Beige: Effortless and sophisticated. This combination never goes out of style."
        ),
        proTip = "When in doubt, stick to neutrals."
    )
)

@Composable
fun TipDetailScreen(
    title: String,
    category: String,
    imageRes: Int? = null,
    imageUrl: String? = null,
    onBack: () -> Unit
) {
    val fallbackImageUrl = "https://images.unsplash.com/photo-1516257984-b1b4d707412e?q=80&w=1200&auto=format&fit=crop"
    
    val bookmarkedTips by BookmarksManager.bookmarkedTips.collectAsState()
    val isBookmarked = bookmarkedTips.contains(title)
    
    val categoryColors = when(category) {
        "Colors" -> Pair(BadgeOrangeBg, BadgeOrangeText)
        "Outfit" -> Pair(BadgePurpleBg, BadgePurpleText)
        "Fit" -> Pair(BadgeGreenBg, BadgeGreenText)
        "Grooming" -> Pair(BadgeGreenBg, BadgeGreenText)
        "Accessories" -> Pair(BadgeYellowBg, BadgeYellowText)
        else -> Pair(BadgeBlueBg, BadgeBlueText)
    }

    val contentInfo = tipDatabase[title] ?: TipContentInfo(
        description = "Discover the secrets of great style.",
        steps = listOf("More details coming soon."),
        proTip = "Stay stylish and confident."
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        val scrollState = rememberScrollState()
        
        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .graphicsLayer {
                    translationY = -scrollState.value * 0.5f
                }
        ) {
            AsyncImage(
                model = imageRes ?: imageUrl ?: fallbackImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(350.dp))
            
            // Content Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
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
                        text = "1-2 min read",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = contentInfo.description,
                fontSize = 15.sp,
                color = TextNavyBlue.copy(alpha = 0.8f),
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            contentInfo.steps.forEachIndexed { index, step ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${index + 1}. ${step.substringBefore(':')}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyBlue
                        )
                        if (step.contains(':')) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = step.substringAfter(':').trim(),
                                fontSize = 14.sp,
                                color = TextMuted,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            contentInfo.recommendedColors?.let { colors ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFAFAFA))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Recommended Color Combinations",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyBlue
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        ColorPaletteColumn(colorsString = colors)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceVariant)
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Pro Tip",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pro Tip",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavyBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = contentInfo.proTip,
                        fontSize = 15.sp,
                        color = TextNavyBlue.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    
    // Fade gradient at the top that overlays the scrolling sheet
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .align(Alignment.TopCenter)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.White, Color.Transparent)
                )
            )
    )
    
    // Top Action Bar Overlay (Drawn last to stay on top)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = -scrollState.value * 0.5f
            }
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
                .background(Color.White)
                .clickable { BookmarksManager.toggleBookmark(title) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Bookmark",
                tint = TextNavyBlue
            )
        }
    }
}
}

@Composable
fun ColorPaletteColumn(colorsString: String) {
    val combinations = colorsString.split(" • ")
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        combinations.forEach { combo ->
            val colorNames = combo.split(" + ")
            if (colorNames.size == 2) {
                val c1 = getColorFromName(colorNames[0].trim())
                val c2 = getColorFromName(colorNames[1].trim())
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F9FA), RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Separated colored circles
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(c1)
                                .border(1.dp, Color.LightGray, CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(c2)
                                .border(1.dp, Color.LightGray, CircleShape)
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = combo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextNavyBlue
                    )
                }
            }
        }
    }
}

fun getColorFromName(name: String): Color {
    return when(name.lowercase()) {
        "white" -> Color.White
        "black" -> Color.Black
        "beige" -> Color(0xFFF5F5DC)
        "cream" -> Color(0xFFFFFDD0)
        "navy" -> Color(0xFF000080)
        "soft gray", "gray" -> Color.LightGray
        "olive" -> Color(0xFF808000)
        "brown" -> Color(0xFFA52A2A)
        "camel" -> Color(0xFFC19A6B)
        "ivory" -> Color(0xFFFFFFF0)
        "taupe" -> Color(0xFF483C32)
        "sand" -> Color(0xFFC2B280)
        "gold" -> Color(0xFFFFD700)
        else -> Color.Gray
    }
}
