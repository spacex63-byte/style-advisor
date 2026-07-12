package com.example.styleadvisor.ui.tips

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object BookmarksManager {
    private const val PREFS_NAME = "bookmarks_prefs"
    private const val KEY_BOOKMARKS = "bookmarked_tips"
    
    private var prefs: SharedPreferences? = null
    private val _bookmarkedTips = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedTips: StateFlow<Set<String>> = _bookmarkedTips.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedBookmarks = prefs?.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
        _bookmarkedTips.value = savedBookmarks
    }

    fun toggleBookmark(title: String) {
        _bookmarkedTips.update { current ->
            val newSet = if (current.contains(title)) {
                current - title
            } else {
                current + title
            }
            prefs?.edit()?.putStringSet(KEY_BOOKMARKS, newSet)?.apply()
            newSet
        }
    }

    fun isBookmarked(title: String): Boolean {
        return _bookmarkedTips.value.contains(title)
    }
}
