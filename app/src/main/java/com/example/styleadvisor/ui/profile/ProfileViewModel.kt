package com.example.styleadvisor.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

data class ProfileState(
    val name: String = "Tavorian",
    val bio: String = "Style Explorer",
    val profileImageUri: Uri? = null,
    val analysesCount: Int = 0,
    val avgScore: Int = 0,
    val outfitIdeas: Int = 0,
    val styleProfileProgress: Float = 0.0f,
    val topStyle: String = "Unknown",
    val recentScores: List<Float> = listOf(0f, 0f, 0f, 0f, 0f)
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    private val _styleProfileState = MutableStateFlow(StyleProfileData())
    val styleProfileState: StateFlow<StyleProfileData> = _styleProfileState

    init {
        viewModelScope.launch {
            com.example.styleadvisor.data.AnalysisRepository.history.collect { historyList ->
                if (historyList.isNotEmpty()) {
                    val count = historyList.size
                    val avg = historyList.map { it.result.overallScore }.average().toInt()
                    val ideas = historyList.flatMap { it.result.styleTags }.distinct().size
                    
                    // Create a list of recent scores (max 5) scaled to 0.0f - 1.0f for the chart
                    // The chart expects values left-to-right (oldest to newest), but historyList is newest first.
                    // So we take up to 5, and reverse it.
                    val rawRecent = historyList.take(5).reversed().map { it.result.overallScore / 100f }
                    // Pad to 5 items to prevent IndexOutOfBounds in the graph.
                    // Pad with the oldest available score to prevent unrealistic dips to zero.
                    val paddingValue = rawRecent.firstOrNull() ?: 0.5f
                    val recent = if (rawRecent.size < 5) {
                        MutableList(5 - rawRecent.size) { paddingValue } + rawRecent
                    } else {
                        rawRecent
                    }
                    
                    // Top style (most frequent style tag)
                    val allTags = historyList.flatMap { it.result.styleTags }
                    val top = allTags.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: "Unknown"

                    _uiState.value = _uiState.value.copy(
                        analysesCount = count,
                        avgScore = avg,
                        outfitIdeas = ideas,
                        recentScores = recent,
                        topStyle = top
                    )
                } else {
                    // Reset to zero if history is cleared
                    _uiState.value = _uiState.value.copy(
                        analysesCount = 0,
                        avgScore = 0,
                        outfitIdeas = 0,
                        recentScores = listOf(0f, 0f, 0f, 0f, 0f),
                        topStyle = "Unknown"
                    )
                }
            }
        }
    }

    fun updateProfile(name: String, bio: String) {
        _uiState.value = _uiState.value.copy(name = name, bio = bio)
    }

    fun updateProfileImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "profile_pic_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                
                // Delete old profile pics to save space
                context.filesDir.listFiles { _, name -> name.startsWith("profile_pic_") }?.forEach { it.delete() }
                // Also delete the old default one if it exists
                File(context.filesDir, "profile_pic.jpg").delete()
                
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                _uiState.value = _uiState.value.copy(profileImageUri = Uri.fromFile(file))
                
                // Save the new file name in SharedPreferences
                val prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                prefs.edit().putString("profile_pic_name", fileName).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun loadProfileImage(context: Context) {
        val prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val fileName = prefs.getString("profile_pic_name", "profile_pic.jpg")
        val file = File(context.filesDir, fileName ?: "profile_pic.jpg")
        if (file.exists()) {
            _uiState.value = _uiState.value.copy(profileImageUri = Uri.fromFile(file))
        }
        
        // Also load style profile data here since this is called on screen launch
        loadStyleProfileData(context)
    }

    fun loadStyleProfileData(context: Context) {
        val prefs = context.getSharedPreferences("style_profile_prefs", Context.MODE_PRIVATE)
        val data = StyleProfileData(
            preferredStyles = prefs.getStringSet("preferredStyles", emptySet()) ?: emptySet(),
            favoriteColors = prefs.getStringSet("favoriteColors", emptySet()) ?: emptySet(),
            fitPreferences = prefs.getStringSet("fitPreferences", emptySet()) ?: emptySet(),
            bodyType = prefs.getStringSet("bodyType", emptySet()) ?: emptySet(),
            skinTone = prefs.getStringSet("skinTone", emptySet()) ?: emptySet(),
            gender = prefs.getStringSet("gender", emptySet()) ?: emptySet(),
            ageGroup = prefs.getStringSet("ageGroup", emptySet()) ?: emptySet(),
            budget = prefs.getStringSet("budget", emptySet()) ?: emptySet(),
            occasions = prefs.getStringSet("occasions", emptySet()) ?: emptySet(),
            preferredBrands = prefs.getString("preferredBrands", "") ?: "",
            height = prefs.getString("height", "") ?: "",
            weight = prefs.getString("weight", "") ?: ""
        )
        _styleProfileState.value = data
        _uiState.value = _uiState.value.copy(styleProfileProgress = prefs.getFloat("styleProfileProgress", 0f))
    }

    fun saveStyleProfileData(context: Context, data: StyleProfileData) {
        _styleProfileState.value = data
        
        var filled = 0
        if (data.preferredStyles.isNotEmpty()) filled++
        if (data.favoriteColors.isNotEmpty()) filled++
        if (data.fitPreferences.isNotEmpty()) filled++
        if (data.bodyType.isNotEmpty()) filled++
        if (data.skinTone.isNotEmpty()) filled++
        if (data.gender.isNotEmpty()) filled++
        if (data.ageGroup.isNotEmpty()) filled++
        if (data.budget.isNotEmpty()) filled++
        if (data.occasions.isNotEmpty()) filled++
        
        val progress = if (filled > 0) filled.toFloat() / 9 else 0f
        
        val prefs = context.getSharedPreferences("style_profile_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putStringSet("preferredStyles", data.preferredStyles)
            .putStringSet("favoriteColors", data.favoriteColors)
            .putStringSet("fitPreferences", data.fitPreferences)
            .putStringSet("bodyType", data.bodyType)
            .putStringSet("skinTone", data.skinTone)
            .putStringSet("gender", data.gender)
            .putStringSet("ageGroup", data.ageGroup)
            .putStringSet("budget", data.budget)
            .putStringSet("occasions", data.occasions)
            .putString("preferredBrands", data.preferredBrands)
            .putString("height", data.height)
            .putString("weight", data.weight)
            .putFloat("styleProfileProgress", progress)
            .apply()
            
        _uiState.value = _uiState.value.copy(styleProfileProgress = progress)
    }
}
