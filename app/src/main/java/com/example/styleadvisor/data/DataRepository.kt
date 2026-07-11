package com.example.styleadvisor.data

import com.example.styleadvisor.model.AnalysisResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface DataRepository {
  val data: Flow<List<String>>
}

class DefaultDataRepository : DataRepository {
  override val data: Flow<List<String>> = flow { emit(listOf("Android")) }
}

data class HistoryItem(
    val result: AnalysisResult,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

object AnalysisRepository {
    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()
    
    fun addResult(result: AnalysisResult, imageUri: String? = null) {
        val currentList = _history.value.toMutableList()
        currentList.add(0, HistoryItem(result, imageUri)) // Add to the top
        _history.value = currentList
    }
    
    fun clearHistory() {
        _history.value = emptyList()
    }
}
