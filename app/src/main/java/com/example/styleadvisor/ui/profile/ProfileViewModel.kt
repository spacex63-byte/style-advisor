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
    val analysesCount: Int = 23,
    val avgScore: Int = 87,
    val outfitIdeas: Int = 12,
    val styleProfileProgress: Float = 0.85f,
    val topStyle: String = "Smart Casual",
    val recentScores: List<Float> = listOf(0.7f, 0.65f, 0.45f, 0.48f, 0.25f)
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    fun updateProfile(name: String, bio: String) {
        _uiState.value = _uiState.value.copy(name = name, bio = bio)
    }

    fun updateProfileImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.filesDir, "profile_pic.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                _uiState.value = _uiState.value.copy(profileImageUri = Uri.fromFile(file))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun loadProfileImage(context: Context) {
        val file = File(context.filesDir, "profile_pic.jpg")
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
