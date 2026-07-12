package com.example.styleadvisor.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.DisposableEffect
import com.example.styleadvisor.data.NotificationRepository
import com.example.styleadvisor.data.NotificationModel
import java.util.concurrent.TimeUnit

fun getRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    if (diff < 0) return "just now"
    
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    
    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes mins ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "Yesterday"
        else -> "$days days ago"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(GlobalBackgroundGradient)) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back",
                        tint = TextNavyBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "Notifications",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavyBlue
                    )
                    Text(
                        text = "Stay updated with your style",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val notifications by NotificationRepository.notifications.collectAsState()
            
            DisposableEffect(Unit) {
                onDispose {
                    NotificationRepository.markAllAsRead()
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(notification)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationModel) {
    val icon: ImageVector
    val iconBgColor: Color
    val iconTintColor: Color

    when (notification.type) {
        "STYLE_CHECK" -> {
            icon = Icons.Filled.Checkroom
            iconBgColor = BadgePurpleBg
            iconTintColor = BadgePurpleText
        }
        "COLOR_TIP" -> {
            icon = Icons.Filled.Palette
            iconBgColor = BadgeOrangeBg
            iconTintColor = BadgeOrangeText
        }
        else -> {
            icon = Icons.Filled.Star
            iconBgColor = BadgeYellowBg
            iconTintColor = BadgeYellowText
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (notification.isUnread) Color.White else Color.White.copy(alpha = 0.6f),
        shadowElevation = if (notification.isUnread) 4.dp else 0.dp,
        border = if (!notification.isUnread) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = if (notification.isUnread) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextNavyBlue,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (notification.isUnread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5252))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = notification.message,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = getRelativeTime(notification.timestamp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted.copy(alpha = 0.7f)
                )
            }
        }
    }
}
