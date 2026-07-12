package com.example.styleadvisor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@Composable
fun StyleProfileSummary(
    data: StyleProfileData,
    onEditItem: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Style Preferences Card
        SummaryCard(
            title = "Style Preferences"
        ) {
            SummaryItem(
                icon = Icons.Default.Checkroom,
                title = "Preferred Style",
                value = if (data.preferredStyles.isEmpty()) "Not set" else data.preferredStyles.joinToString(", "),
                onClick = { onEditItem("Preferred Style") }
            )
            HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(start = 56.dp))
            SummaryItem(
                icon = Icons.Default.Palette,
                title = "Favorite Colors",
                value = if (data.favoriteColors.isEmpty()) "Not set" else data.favoriteColors.joinToString(", "),
                onClick = { onEditItem("Favorite Colors") }
            )
            HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(start = 56.dp))
            SummaryItem(
                icon = Icons.Default.DryCleaning,
                title = "Fit Preference",
                value = if (data.fitPreferences.isEmpty()) "Not set" else data.fitPreferences.joinToString(", "),
                onClick = { onEditItem("Fit Preference") }
            )
            HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(start = 56.dp))
            SummaryItem(
                icon = Icons.Default.AccountBalanceWallet,
                title = "Budget Range",
                value = if (data.budget.isEmpty()) "Not set" else data.budget.joinToString(", "),
                onClick = { onEditItem("Budget Range") }
            )
        }

        // Body Info Card
        SummaryCard(
            title = "Body Info"
        ) {
            SummaryItem(
                icon = Icons.Default.Height,
                title = "Height",
                value = if (data.height.isEmpty()) "Not set" else "${data.height} cm",
                onClick = { onEditItem("Height") }
            )
            HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(start = 56.dp))
            SummaryItem(
                icon = Icons.Default.MonitorWeight,
                title = "Weight",
                value = if (data.weight.isEmpty()) "Not set" else "${data.weight} kg",
                onClick = { onEditItem("Weight") }
            )
            HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(start = 56.dp))
            SummaryItem(
                icon = Icons.Default.Accessibility,
                title = "Body Type",
                value = if (data.bodyType.isEmpty()) "Not set" else data.bodyType.joinToString(", "),
                onClick = { onEditItem("Body Type") }
            )
            HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(start = 56.dp))
            SummaryItem(
                icon = Icons.Default.Face,
                title = "Skin Tone",
                value = if (data.skinTone.isEmpty()) "Not set" else data.skinTone.joinToString(", "),
                onClick = { onEditItem("Skin Tone") }
            )
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue
            )
        }
        
        content()
    }
}

@Composable
fun SummaryItem(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextNavyBlue,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextNavyBlue
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = TextMuted
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
