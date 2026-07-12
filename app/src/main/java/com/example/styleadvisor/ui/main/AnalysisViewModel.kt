package com.example.styleadvisor.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.styleadvisor.model.AnalysisResult
import com.example.styleadvisor.service.AiAnalysisService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Analyzing : AnalysisState()
    data class Success(val result: AnalysisResult) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}

class AnalysisViewModel : ViewModel() {
    private val aiService = AiAnalysisService()
    
    private val _uiState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val uiState: StateFlow<AnalysisState> = _uiState
    
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri
    
    fun analyzeImage(context: Context, uri: Uri, isSample: Boolean = false) {
        _selectedImageUri.value = uri
        
        if (isSample) {
            val mockResult = com.example.styleadvisor.model.AnalysisResult(
                overallScore = 92,
                colorHarmonyScore = 95,
                fitScore = 88,
                styleScore = 94,
                shortTitle = "Stunning All-Black Outfit",
                shortDescription = "A sleek black wrap top paired with a flowy midi skirt creates a classy and elegant look.",
                primaryClothingItem = "Black V-Neck Wrap Top",
                styleTags = listOf("Elegant", "Chic", "Minimal"),
                bestForOccasions = listOf("Evening Dinner", "Date Night", "Formal Event"),
                whatLooksBest = "The all-black color keeps it simple and classy. The wrap top adds a nice shape and the flared skirt balances it perfectly.",
                whatCouldImprove = "Adding a statement necklace or earrings would elevate this look even more.",
                outfitElements = listOf("Black V-Neck Wrap Top", "Black Flared Midi Skirt"),
                detectedColors = listOf("#1A1A1A", "#2C2C2C", "#3D3D3D"),
                colorsDescription = "All-black tones with subtle depth and texture."
            )
            com.example.styleadvisor.data.AnalysisRepository.addResult(mockResult, uri.toString())
            _uiState.value = AnalysisState.Success(mockResult)
            return
        }
        
        _uiState.value = AnalysisState.Analyzing
        viewModelScope.launch {
            try {
                val bitmap = loadBitmap(context, uri)
                if (bitmap == null) {
                    _uiState.value = AnalysisState.Error("Failed to load image")
                    return@launch
                }
                
                // Scale bitmap down to reduce token usage and speed up API
                val scaledBitmap = scaleBitmap(bitmap, 800)
                
                val result = aiService.analyzeOutfit(scaledBitmap)
                com.example.styleadvisor.data.AnalysisRepository.addResult(result, uri.toString())
                _uiState.value = AnalysisState.Success(result)
            } catch (e: Exception) {
                _uiState.value = AnalysisState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun reset() {
        _uiState.value = AnalysisState.Idle
        _selectedImageUri.value = null
    }
    
    fun showHistoryItem(item: com.example.styleadvisor.data.HistoryItem) {
        _selectedImageUri.value = item.imageUri?.let { Uri.parse(it) }
        _uiState.value = AnalysisState.Success(item.result)
    }
    
    private fun loadBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    decoder.isMutableRequired = true
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxDimension && height <= maxDimension) return bitmap
        
        val ratio = width.toFloat() / height.toFloat()
        val newWidth = if (ratio > 1) maxDimension else (maxDimension * ratio).toInt()
        val newHeight = if (ratio > 1) (maxDimension / ratio).toInt() else maxDimension
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
