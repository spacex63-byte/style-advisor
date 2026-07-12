package com.example.styleadvisor.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.PrimaryBlue
import com.example.styleadvisor.theme.SurfaceVariant
import com.example.styleadvisor.theme.TextNavyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleEditSheet(
    editingField: String,
    viewModel: ProfileViewModel,
    onDismiss: () -> Unit
) {
    val styleData by viewModel.styleProfileState.collectAsState()
    val context = LocalContext.current
    
    // Copy all data to local state
    var currentData by remember { mutableStateOf(styleData) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Edit $editingField",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavyBlue,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            when (editingField) {
                "Preferred Style" -> {
                    ChipGroup(
                        options = listOf("Smart Casual", "Minimal", "Streetwear", "Old Money", "Business Casual", "Formal", "Sporty"),
                        selectedOptions = currentData.preferredStyles,
                        onSelectionChange = { toggleSelection(it, currentData.preferredStyles, true) { newSet -> currentData = currentData.copy(preferredStyles = newSet) } },
                        multiSelect = true
                    )
                }
                "Favorite Colors" -> {
                    ChipGroup(
                        options = listOf("White", "Black", "Beige", "Navy", "Brown", "Gray", "Olive"),
                        selectedOptions = currentData.favoriteColors,
                        onSelectionChange = { toggleSelection(it, currentData.favoriteColors, true) { newSet -> currentData = currentData.copy(favoriteColors = newSet) } },
                        multiSelect = true
                    )
                }
                "Fit Preference" -> {
                    ChipGroup(
                        options = listOf("Slim Fit", "Regular Fit", "Relaxed Fit", "Oversized"),
                        selectedOptions = currentData.fitPreferences,
                        onSelectionChange = { toggleSelection(it, currentData.fitPreferences, true) { newSet -> currentData = currentData.copy(fitPreferences = newSet) } },
                        multiSelect = true
                    )
                }
                "Budget Range" -> {
                    ChipGroup(
                        options = listOf("Budget", "Mid Range", "Premium", "Luxury"),
                        selectedOptions = currentData.budget,
                        onSelectionChange = { toggleSelection(it, currentData.budget, false) { newSet -> currentData = currentData.copy(budget = newSet) } },
                        multiSelect = false
                    )
                }
                "Height" -> {
                    OutlinedTextField(
                        value = currentData.height,
                        onValueChange = { currentData = currentData.copy(height = it) },
                        placeholder = { Text("Height (cm/ft)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TextNavyBlue,
                            unfocusedBorderColor = SurfaceVariant,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
                "Weight" -> {
                    OutlinedTextField(
                        value = currentData.weight,
                        onValueChange = { currentData = currentData.copy(weight = it) },
                        placeholder = { Text("Weight (kg/lbs)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TextNavyBlue,
                            unfocusedBorderColor = SurfaceVariant,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
                "Body Type" -> {
                    ChipGroup(
                        options = listOf("Slim", "Athletic", "Average", "Plus Size"),
                        selectedOptions = currentData.bodyType,
                        onSelectionChange = { toggleSelection(it, currentData.bodyType, false) { newSet -> currentData = currentData.copy(bodyType = newSet) } },
                        multiSelect = false
                    )
                }
                "Skin Tone" -> {
                    ChipGroup(
                        options = listOf("Fair", "Light", "Medium", "Olive", "Deep"),
                        selectedOptions = currentData.skinTone,
                        onSelectionChange = { toggleSelection(it, currentData.skinTone, false) { newSet -> currentData = currentData.copy(skinTone = newSet) } },
                        multiSelect = false
                    )
                }
                "Gender" -> {
                     ChipGroup(
                        options = listOf("Male", "Female"),
                        selectedOptions = currentData.gender,
                        onSelectionChange = { toggleSelection(it, currentData.gender, false) { newSet -> currentData = currentData.copy(gender = newSet) } },
                        multiSelect = false
                    )
                }
                "Age Group" -> {
                    ChipGroup(
                        options = listOf("Teen", "Young Adult", "Adult"),
                        selectedOptions = currentData.ageGroup,
                        onSelectionChange = { toggleSelection(it, currentData.ageGroup, false) { newSet -> currentData = currentData.copy(ageGroup = newSet) } },
                        multiSelect = false
                    )
                }
                "Occasions" -> {
                    ChipGroup(
                        options = listOf("Daily Wear", "Office", "College", "Party", "Wedding", "Travel", "Date Night"),
                        selectedOptions = currentData.occasions,
                        onSelectionChange = { toggleSelection(it, currentData.occasions, true) { newSet -> currentData = currentData.copy(occasions = newSet) } },
                        multiSelect = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    viewModel.saveStyleProfileData(context, currentData)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = TextNavyBlue)
            ) {
                Text("Save", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

private fun toggleSelection(
    option: String,
    currentSelections: Set<String>,
    multiSelect: Boolean,
    updateSelections: (Set<String>) -> Unit
) {
    if (multiSelect) {
        if (currentSelections.contains(option)) {
            updateSelections(currentSelections - option)
        } else {
            updateSelections(currentSelections + option)
        }
    } else {
        if (currentSelections.contains(option)) {
            updateSelections(emptySet())
        } else {
            updateSelections(setOf(option))
        }
    }
}
