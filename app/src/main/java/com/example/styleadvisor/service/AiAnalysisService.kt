package com.example.styleadvisor.service

import android.graphics.Bitmap
import android.util.Base64
import com.example.styleadvisor.BuildConfig
import com.example.styleadvisor.model.AnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonArray
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
            You are a strict and expert fashion stylist. Your FIRST and MOST IMPORTANT task is to verify if the image actually contains a HUMAN wearing clothing/outfit.
            If the image does NOT clearly contain a HUMAN wearing clothing (for example, if it is a random object, animal, scenery, car, cup, screenshot of text, blank image, or just clothing laid flat without a person wearing it), you MUST REJECT it.
            To reject it, return STRICTLY this exact JSON and NOTHING ELSE:
            {
                "error": "Please upload a photo of a person wearing an outfit."
            }
            
            ONLY if the image clearly contains a person wearing an outfit, proceed to analyze it and provide a realistic fashion analysis.
            CRITICAL INSTRUCTION: You MUST provide HONEST, STRICT, and REALISTIC feedback. DO NOT flatter the user. If the outfit is mismatched, poorly fitted, messy, or lacks style, you MUST give LOW scores (e.g., 30s-60s) and explicitly criticize it in `whatCouldImprove` and `shortTitle` (e.g., "Messy", "Mismatched", "Needs Work"). Only give high scores (80s-90s) if the outfit is genuinely excellent.
            
            1. `shortTitle`: Max 1 or 2 words based on the person/outfit (e.g., "Handsome", "Beautiful", "Dapper", "Stunning", "Messy", "Mismatched", "Needs Work").
            2. `shortDescription`: Max 2 lines of description based on the actual outfit and photo. Be honest.
            3. `primaryClothingItem`: The most prominent piece of clothing they are wearing (max 3 words, e.g., "Blue Denim Jacket").
            4. `styleTags`: Pick 2 to 4 styles from this list exactly: [Smart Casual, Casual, Business Casual, Formal, Streetwear, Vintage, Minimalist, Y2K, Bohemian, Grunge, Preppy, Athleisure, Cyberpunk, Goth, Chic, Classic, Retro, Hip Hop, Skater, Techwear, Dark Academia, Light Academia, Cottagecore, Punk, Rock, Old Money, Sporty, Safari, Nautical, Resort, Loungewear, Western, Military, Artsy, Monochrome, Pastel, Workwear, Avant-Garde].
            5. `bestForOccasions`: Pick 2 to 4 occasions from this list exactly: [Casual Outing, Date Night, Office/Work, Party, Formal Event, Workout/Gym, Travel, Weekend Brunch, Beach/Resort, Wedding Guest, Concert/Festival, Lounging at Home, Interview, Night Club, Outdoor Adventure, School/College].
            6. Scores (`overallScore`, `colorHarmonyScore`, `fitScore`, `styleScore`): Must be REALISTIC out of 100. DO NOT default to high scores. Use the full 0-100 range.
            7. `whatLooksBest`, `whatCouldImprove`, `outfitElements`, `detectedColors`: Analyze realistically based on the image. Give actionable, strict advice in `whatCouldImprove`.
            
            Return the output STRICTLY in the following JSON format without markdown blocks:
            {
                "overallScore": 86,
                "colorHarmonyScore": 88,
                "fitScore": 85,
                "styleScore": 87,
                "shortTitle": "Handsome",
                "shortDescription": "You've got a confident and stylish vibe. The layers work perfectly.",
                "primaryClothingItem": "Black Leather Jacket",
                "styleTags": ["Streetwear", "Minimalist"],
                "bestForOccasions": ["Casual Outing", "Date Night"],
                "whatLooksBest": "The earthy tones and clean layering...",
                "whatCouldImprove": "Try adding a statement accessory...",
                "outfitElements": ["Leather Jacket", "White T-Shirt", "Black Jeans"],
                "detectedColors": ["#000000", "#FFFFFF", "#8B4513"]
            }
        """.trimIndent()
        
        var responseBodyString = ""
        try {
            // Use the premium vision model supported by Mesh API
            responseBodyString = sendAnalysisRequest(base64Image, prompt, "openai/gpt-4o")
        } catch (e: Exception) {
            // Fallback to same model if it fails
            responseBodyString = sendAnalysisRequest(base64Image, prompt, "openai/gpt-4o")
        }
        
        if (responseBodyString.isBlank()) {
            throw Exception("Received empty response from API. The model might be incorrect or the API key might be invalid.")
        }
        
        // Parse the OpenAI-compatible response format
        val jsonElement = try {
            json.parseToJsonElement(responseBodyString).jsonObject
        } catch (e: Exception) {
            throw Exception("Failed to parse API response: $responseBodyString", e)
        }
        
        val choices = jsonElement["choices"]?.jsonArray
        val message = choices?.firstOrNull()?.jsonObject?.get("message")?.jsonObject
        val contentElement = message?.get("content")
        
        val content = when (contentElement) {
            is JsonArray -> contentElement.joinToString("") { 
                it.jsonObject["text"]?.jsonPrimitive?.content ?: "" 
            }
            else -> contentElement?.jsonPrimitive?.content ?: "{}"
        }
        
        // Strip markdown fences if the LLM wraps the JSON
        val cleanContent = content.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
        
        val contentJson = json.parseToJsonElement(cleanContent).jsonObject
        if (contentJson.containsKey("error")) {
            val errorMessage = contentJson["error"]?.jsonPrimitive?.content ?: "Invalid image."
            throw Exception(errorMessage)
        }
        
        return@withContext json.decodeFromString<AnalysisResult>(cleanContent)
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
