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
            CRITICAL INSTRUCTION: You MUST provide HONEST, STRICT, and REALISTIC feedback. DO NOT flatter the user. If the user has worn bad clothes, is poorly dressed, mismatched, or the outfit lacks style, you MUST give VERY LOW scores (e.g., 10, 11, 12, 15, 20) and explicitly criticize it in `whatCouldImprove` and `shortTitle` (e.g., "Messy", "Mismatched", "Needs Work", "Disaster"). Only give high scores (80s-90s) if the outfit is genuinely excellent. If it's average, give 40-60. Do NOT default to high scores.
            
            CLOTHING CONDITION CHECK (VERY IMPORTANT):
            Before anything else, carefully assess the CONDITION of the clothing. Look for:
            - Torn, ripped, tattered, or shredded fabric
            - Stains, dirt, or visible damage
            - Extremely worn out, faded, or ragged clothing
            - Holes, fraying edges, or missing buttons
            If the clothing is in POOR CONDITION (damaged, tattered, extremely worn out, ragged):
            - Give overall scores between 5-20 MAXIMUM
            - Use `shortTitle` like "Disaster", "Worn Out", "Terrible", "Ragged", "Tattered"
            - `shortDescription` must explicitly mention the poor condition (e.g., "Extremely worn and damaged clothing that needs to be replaced immediately")
            - `styleTags` MUST include negative tags like "Worn Out", "Damaged", "Unkempt" from the list below
            - `bestForOccasions` should ONLY include "Lounging at Home" or be left minimal — damaged clothing is NOT suitable for any outing
            - `whatCouldImprove` must strongly recommend replacing the clothing entirely
            - `outfitElements` must describe items honestly with their condition (e.g., "Tattered T-Shirt", "Ragged Shorts", "Stained Jeans")
            
            1. `shortTitle`: Max 1 or 2 words based on the person/outfit (e.g., "Handsome", "Beautiful", "Dapper", "Stunning", "Messy", "Mismatched", "Needs Work", "Disaster", "Worn Out", "Terrible", "Ragged").
            2. `shortDescription`: Max 2 lines of description based on the actual outfit and photo. Be honest. If clothes are damaged, say so clearly.
            3. `primaryClothingItem`: The most prominent piece of clothing they are wearing (max 3 words, e.g., "Blue Denim Jacket"). Include condition if bad (e.g., "Torn White Shirt").
            4. `styleTags`: Pick 2 to 4 styles from this list exactly: [Casual, Formal, Party Wear, Street Style, Simple & Clean, Sporty, Traditional, Trendy, Everyday Wear, Office Wear, Relaxed, Elegant, Vintage, Bold, Minimalist, Worn Out, Damaged, Unkempt, Sloppy, Outdated].
            5. `bestForOccasions`: Pick 2 to 4 occasions from this list exactly: [Casual Outing, Date Night, Office/Work, Party, Formal Event, Workout/Gym, Travel, Weekend Brunch, Beach/Resort, Wedding Guest, Concert/Festival, Lounging at Home, Interview, Night Club, Outdoor Adventure, School/College, Evening Dinner]. For damaged/worn clothing, only pick "Lounging at Home" or at most 1-2 minimal options.
            6. Scores (`overallScore`, `colorHarmonyScore`, `fitScore`, `styleScore`): Must be REALISTIC out of 100. DO NOT default to high scores. Use the full 0-100 range. Damaged/tattered clothing should score 5-20.
            7. `whatLooksBest`, `whatCouldImprove`, `outfitElements`, `detectedColors`: Analyze realistically based on the image. Give actionable, strict advice in `whatCouldImprove`. For damaged clothing, recommend replacing items. For `colorsDescription`, describe the colors AND explicitly suggest which other colors would pair well with them.
            8. `styleTagExplanations`: For each tag you picked in `styleTags`, provide a 1-2 line explanation of what that style means. This should be a key-value map. For negative tags like "Worn Out" or "Damaged", explain what's wrong with the clothing condition.
            
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
                "detectedColors": ["#000000", "#FFFFFF", "#8B4513"],
                "colorsDescription": "Classic black and white base with earthy brown accents. Pairs beautifully with olive green, muted mustard, or silver accessories.",
                "styleTagExplanations": {
                    "Street Style": "A casual, comfortable fashion style that takes inspiration from everyday street culture.",
                    "Minimalist": "Focuses on simplicity, clean lines, and neutral colors without excessive accessories."
                }
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
