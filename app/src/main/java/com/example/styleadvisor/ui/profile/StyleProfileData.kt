package com.example.styleadvisor.ui.profile

data class StyleProfileData(
    val preferredStyles: Set<String> = emptySet(),
    val favoriteColors: Set<String> = emptySet(),
    val fitPreferences: Set<String> = emptySet(),
    val bodyType: Set<String> = emptySet(),
    val skinTone: Set<String> = emptySet(),
    val gender: Set<String> = emptySet(),
    val ageGroup: Set<String> = emptySet(),
    val budget: Set<String> = emptySet(),
    val occasions: Set<String> = emptySet(),
    val preferredBrands: String = "",
    val height: String = "",
    val weight: String = ""
)
