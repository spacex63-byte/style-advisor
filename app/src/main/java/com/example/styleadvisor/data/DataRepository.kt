package com.example.styleadvisor.data

import android.content.Context
import android.content.SharedPreferences
import com.example.styleadvisor.model.AnalysisResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface DataRepository {
  val data: Flow<List<String>>
}

class DefaultDataRepository : DataRepository {
  override val data: Flow<List<String>> = flow { emit(listOf("Android")) }
}

@Serializable
data class HistoryItem(
    val result: AnalysisResult,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

object AnalysisRepository {
    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()
    
    private var sharedPreferences: SharedPreferences? = null
    private const val PREFS_NAME = "StyleAdvisorPrefs"
    private const val KEY_HISTORY = "history_items"
    
    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadHistory()
        }
    }
    
    private fun loadHistory() {
        val jsonString = sharedPreferences?.getString(KEY_HISTORY, null)
        if (jsonString != null) {
            try {
                val items = Json.decodeFromString<List<HistoryItem>>(jsonString)
                _history.value = items
            } catch (e: Exception) {
                e.printStackTrace()
                _history.value = emptyList()
            }
        }
    }
    
    private fun saveHistory() {
        try {
            val jsonString = Json.encodeToString(_history.value)
            sharedPreferences?.edit()?.putString(KEY_HISTORY, jsonString)?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun addResult(result: AnalysisResult, imageUri: String? = null) {
        val currentList = _history.value.toMutableList()
        currentList.add(0, HistoryItem(result, imageUri)) // Add to the top
        _history.value = currentList
        saveHistory()
    }
    
    fun clearHistory() {
        _history.value = emptyList()
        saveHistory()
    }
    
    fun deleteResult(timestamp: Long) {
        val currentList = _history.value.toMutableList()
        currentList.removeAll { it.timestamp == timestamp }
        _history.value = currentList
        saveHistory()
    }
}
