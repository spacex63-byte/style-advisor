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
                    // Pad to 5 items to prevent IndexOutOfBounds in the graph
                    val recent = if (rawRecent.size < 5) {
                        MutableList(5 - rawRecent.size) { 0f } + rawRecent
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
                        topStyle = top,
                        styleProfileProgress = (count / 10f).coerceAtMost(1.0f) // Arbitrary progress logic based on scans
                    )
                } else {
                    // Reset to zero if history is cleared
                    _uiState.value = _uiState.value.copy(
                        analysesCount = 0,
                        avgScore = 0,
                        outfitIdeas = 0,
                        recentScores = listOf(0f, 0f, 0f, 0f, 0f),
                        topStyle = "Unknown",
                        styleProfileProgress = 0.0f
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
    }

    fun updateStyleProfileProgress(progress: Float) {
        // e.g. update score and progress
        val newAvg = if (progress > 0.5f) 92 else 87
        val newRecent = if (progress > 0.5f) listOf(0.5f, 0.7f, 0.8f, 0.9f, 0.95f) else listOf(0.7f, 0.65f, 0.45f, 0.48f, 0.25f)
        _uiState.value = _uiState.value.copy(
            styleProfileProgress = progress,
            avgScore = newAvg,
            recentScores = newRecent
        )
    }
}
