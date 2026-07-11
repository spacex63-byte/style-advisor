package com.example.styleadvisor.service

import android.graphics.Bitmap
import android.util.Base64
import com.example.styleadvisor.BuildConfig
import com.example.styleadvisor.model.AnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class AiAnalysisService {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
        
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun analyzeOutfit(image: Bitmap): AnalysisResult = withContext(Dispatchers.IO) {
        val base64Image = bitmapToBase64(image)
        
        val prompt = """
            You are an expert fashion stylist. Analyze the outfit in the provided image.
            Provide a realistic fashion analysis and score based on color harmony, fit, and overall style.
            
            Return the output STRICTLY in the following JSON format without markdown blocks:
            {
                "overallScore": 86,
                "colorHarmonyScore": 88,
                "fitScore": 85,
                "styleScore": 87,
                "shortTitle": "Great Look! 🔥",
                "shortDescription": "You've got a confident and stylish vibe.",
                "styleTags": ["Smart Casual", "Minimal", "Clean"],
                "bestForOccasions": ["Casual Outing", "Date Night"],
                "whatLooksBest": "The earthy tones and clean layering...",
                "whatCouldImprove": "Try adding a statement accessory..."
            }
        """.trimIndent()
        
        var responseBodyString = ""
        try {
            // Use the premium vision model supported by Mesh API
            responseBodyString = sendAnalysisRequest(base64Image, prompt, "openai/gpt-5.4-image")
        } catch (e: Exception) {
            // Fallback to same model (or another if available) if it fails
            responseBodyString = sendAnalysisRequest(base64Image, prompt, "openai/gpt-5.4-image")
        }
        
        // Parse the OpenAI-compatible response format
        val jsonElement = json.parseToJsonElement(responseBodyString).jsonObject
        val choices = jsonElement["choices"]?.jsonArray
        val message = choices?.firstOrNull()?.jsonObject?.get("message")?.jsonObject
        val content = message?.get("content")?.jsonPrimitive?.content ?: "{}"
        
        return@withContext json.decodeFromString<AnalysisResult>(content)
    }
    
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        // Compress as JPEG to reduce payload size
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    private fun sendAnalysisRequest(base64Image: String, prompt: String, model: String): String {
        val requestBodyJson = """
            {
              "model": "$model",
              "messages": [
                {
                  "role": "user",
                  "content": [
                    { "type": "text", "text": "${prompt.replace("\n", "\\n").replace("\"", "\\\"")}" },
                    { "type": "image_url", "image_url": { "url": "data:image/jpeg;base64,$base64Image" } }
                  ]
                }
              ],
              "response_format": { "type": "json_object" }
            }
        """.trimIndent()
        
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaType())
        
        val request = Request.Builder()
            .url("https://api.meshapi.ai/v1/chat/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${BuildConfig.MESH_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()
            
        val response = client.newCall(request).execute()
        
        if (!response.isSuccessful) {
            throw Exception("Failed to analyze image with $model: HTTP ${response.code} ${response.message}")
        }
        
        return response.body?.string() ?: "{}"
    }
}
