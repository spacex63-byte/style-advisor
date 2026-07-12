package com.example.styleadvisor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StyleProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel
) {
    val styleData by viewModel.styleProfileState.collectAsState()
    val context = LocalContext.current

    // State for multiple selections
    var preferredStyles by remember { mutableStateOf(styleData.preferredStyles) }
    var favoriteColors by remember { mutableStateOf(styleData.favoriteColors) }
    var fitPreferences by remember { mutableStateOf(styleData.fitPreferences) }
    var occasions by remember { mutableStateOf(styleData.occasions) }

    // State for single selections
    var bodyType by remember { mutableStateOf(styleData.bodyType) }
    var skinTone by remember { mutableStateOf(styleData.skinTone) }
    var gender by remember { mutableStateOf(styleData.gender) }
    var ageGroup by remember { mutableStateOf(styleData.ageGroup) }
    var budget by remember { mutableStateOf(styleData.budget) }

    // Text fields
    var preferredBrands by remember { mutableStateOf(styleData.preferredBrands) }
    var height by remember { mutableStateOf(styleData.height) }
    var weight by remember { mutableStateOf(styleData.weight) }

    var isEditing by remember { mutableStateOf(false) }

    // Update local state when ViewModel state changes
    LaunchedEffect(styleData) {
        preferredStyles = styleData.preferredStyles
        favoriteColors = styleData.favoriteColors
        fitPreferences = styleData.fitPreferences
        occasions = styleData.occasions
        bodyType = styleData.bodyType
        skinTone = styleData.skinTone
        gender = styleData.gender
        ageGroup = styleData.ageGroup
        budget = styleData.budget
        preferredBrands = styleData.preferredBrands
        height = styleData.height
        weight = styleData.weight
    }

    Box(modifier = Modifier.fillMaxSize().background(GlobalBackgroundGradient)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Style Profile", fontWeight = FontWeight.Bold, color = TextNavyBlue) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextNavyBlue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            if (isEditing) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Complete your style profile",
                        fontSize = 14.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // 1. Preferred Style (Multiple)
                    SectionTitle("Preferred Style")
                    ChipGroup(
                        options = listOf("Smart Casual", "Minimal", "Streetwear", "Old Money", "Business Casual", "Formal", "Sporty"),
                        selectedOptions = preferredStyles,
                        onSelectionChange = { toggleSelection(it, preferredStyles, true) { newSet -> preferredStyles = newSet } },
                        multiSelect = true
                    )

                    // 2. Favorite Colors (Multiple)
                    SectionTitle("Favorite Colors")
                    ChipGroup(
                        options = listOf("White", "Black", "Beige", "Navy", "Brown", "Gray", "Olive"),
                        selectedOptions = favoriteColors,
                        onSelectionChange = { toggleSelection(it, favoriteColors, true) { newSet -> favoriteColors = newSet } },
                        multiSelect = true
                    )

                    // 3. Fit Preference (Multiple)
                    SectionTitle("Fit Preference")
                    ChipGroup(
                        options = listOf("Slim Fit", "Regular Fit", "Relaxed Fit", "Oversized"),
                        selectedOptions = fitPreferences,
                        onSelectionChange = { toggleSelection(it, fitPreferences, true) { newSet -> fitPreferences = newSet } },
                        multiSelect = true
                    )

                    // 4. Body Type (Single)
                    SectionTitle("Body Type")
                    ChipGroup(
                        options = listOf("Slim", "Athletic", "Average", "Plus Size"),
                        selectedOptions = bodyType,
                        onSelectionChange = { toggleSelection(it, bodyType, false) { newSet -> bodyType = newSet } },
                        multiSelect = false
                    )

                    // 5. Skin Tone (Single)
                    SectionTitle("Skin Tone")
                    ChipGroup(
                        options = listOf("Fair", "Light", "Medium", "Olive", "Deep"),
                        selectedOptions = skinTone,
                        onSelectionChange = { toggleSelection(it, skinTone, false) { newSet -> skinTone = newSet } },
                        multiSelect = false
                    )

                    // 6. Gender (Single)
                    SectionTitle("Gender")
                    ChipGroup(
                        options = listOf("Male", "Female"),
                        selectedOptions = gender,
                        onSelectionChange = { toggleSelection(it, gender, false) { newSet -> gender = newSet } },
                        multiSelect = false
                    )

                    // 7. Age Group (Single)
                    SectionTitle("Age Group")
                    ChipGroup(
                        options = listOf("Teen", "Young Adult", "Adult"),
                        selectedOptions = ageGroup,
                        onSelectionChange = { toggleSelection(it, ageGroup, false) { newSet -> ageGroup = newSet } },
                        multiSelect = false
                    )

                    // 8. Budget (Single)
                    SectionTitle("Budget")
                    ChipGroup(
                        options = listOf("Budget", "Mid Range", "Premium", "Luxury"),
                        selectedOptions = budget,
                        onSelectionChange = { toggleSelection(it, budget, false) { newSet -> budget = newSet } },
                        multiSelect = false
                    )

                    // 9. Occasions (Multiple)
                    SectionTitle("Occasions")
                    ChipGroup(
                        options = listOf("Daily Wear", "Office", "College", "Party", "Wedding", "Travel", "Date Night"),
                        selectedOptions = occasions,
                        onSelectionChange = { toggleSelection(it, occasions, true) { newSet -> occasions = newSet } },
                        multiSelect = true
                    )

                    // 10. Preferred Brands
                    SectionTitle("Preferred Brands (optional)")
                    OutlinedTextField(
                        value = preferredBrands,
                        onValueChange = { preferredBrands = it },
                        placeholder = { Text("e.g. Zara, H&M, Nike") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TextNavyBlue,
                            unfocusedBorderColor = SurfaceVariant,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    // 11. Measurements
                    SectionTitle("Measurements (optional)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it },
                            placeholder = { Text("Height (cm/ft)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TextNavyBlue,
                                unfocusedBorderColor = SurfaceVariant,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            placeholder = { Text("Weight (kg/lbs)") },
                            modifier = Modifier.weight(1f),
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

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            val data = StyleProfileData(
                                preferredStyles = preferredStyles,
                                favoriteColors = favoriteColors,
                                fitPreferences = fitPreferences,
                                bodyType = bodyType,
                                skinTone = skinTone,
                                gender = gender,
                                ageGroup = ageGroup,
                                budget = budget,
                                occasions = occasions,
                                preferredBrands = preferredBrands,
                                height = height,
                                weight = weight
                            )
                            viewModel.saveStyleProfileData(context, data)
                            isEditing = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = CircleShape, // Fully rounded
                        colors = ButtonDefaults.buttonColors(containerColor = TextNavyBlue)
                    ) {
                        Text("Save Profile", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    StyleProfileSummary(
                        data = styleData,
                        onEditItem = { isEditing = true }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = TextNavyBlue,
        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipGroup(
    options: List<String>,
    selectedOptions: Set<String>,
    onSelectionChange: (String) -> Unit,
    multiSelect: Boolean = true
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEach { option ->
            val isSelected = selectedOptions.contains(option)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) TextNavyBlue else Color.White)
                    .clickable {
                        onSelectionChange(option)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color.White else TextNavyBlue,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                )
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
