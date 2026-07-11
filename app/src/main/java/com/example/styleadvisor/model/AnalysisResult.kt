package com.example.styleadvisor.model

import kotlinx.serialization.Serializable

@Serializable
data class Tip(
    val title: String,
    val description: String
)

@Serializable
data class AnalysisResult(
    val overallScore: Int, // 0-100
    val colorHarmonyScore: Int, // 0-100
    val fitScore: Int, // 0-100
    val styleScore: Int, // 0-100
    val shortTitle: String, // e.g. "Great Look! 🔥"
    val shortDescription: String,
    val styleTags: List<String>, // e.g. ["Smart Casual", "Minimal"]
    val bestForOccasions: List<String>, // e.g. ["Date Night", "Travel"]
    val whatLooksBest: String,
    val whatCouldImprove: String,
    val outfitElements: List<String> = emptyList(),
    val detectedColors: List<String> = emptyList(), // Hex strings
    val colorsDescription: String = "",
    val personalizedTips: List<Tip> = emptyList()
)
