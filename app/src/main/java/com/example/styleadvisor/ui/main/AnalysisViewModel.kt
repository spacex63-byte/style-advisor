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
    
    fun analyzeImage(context: Context, uri: Uri) {
        _selectedImageUri.value = uri
        _uiState.value = AnalysisState.Analyzing
        viewModelScope.launch {
            try {
                val USE_REAL_API = false // Set to true when ready to use the actual API again
                
                // If it is the sample image or we are testing UI, return a mock response to save money
                if (!USE_REAL_API || uri.toString().contains("android.resource")) {
                    kotlinx.coroutines.delay(1000) // Simulate network delay
                    val mockResult = AnalysisResult(
                        overallScore = 88,
                        colorHarmonyScore = 92,
                        fitScore = 85,
                        styleScore = 89,
                        shortTitle = "Smart Casual / Urban Chic",
                        shortDescription = "Neutral tones with a sophisticated contrast",
                        styleTags = listOf("Smart Casual", "Monochrome", "Layered"),
                        bestForOccasions = listOf("Coffee dates", "Casual Fridays", "City strolling"),
                        whatLooksBest = "The layered jacket over the casual tee creates a relaxed but put-together silhouette.",
                        whatCouldImprove = "Add a subtle silver chain or watch, or try a slightly brighter inner shirt for pop."
                    )
                    _uiState.value = AnalysisState.Success(mockResult)
                    return@launch
                }
                
                val bitmap = loadBitmap(context, uri)
                if (bitmap == null) {
                    _uiState.value = AnalysisState.Error("Failed to load image")
                    return@launch
                }
                
                // Scale bitmap down to reduce token usage and speed up API
                val scaledBitmap = scaleBitmap(bitmap, 800)
                
                val result = aiService.analyzeOutfit(scaledBitmap)
                _uiState.value = AnalysisState.Success(result)
            } catch (e: Exception) {
                _uiState.value = AnalysisState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun reset() {
        _uiState.value = AnalysisState.Idle
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
