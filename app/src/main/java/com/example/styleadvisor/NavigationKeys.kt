package com.example.styleadvisor

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object AnalysisResult : NavKey
@Serializable data object HelpSupport : NavKey
@Serializable data object PrivacyPolicy : NavKey
@Serializable data object StyleProfile : NavKey
@Serializable data object Onboarding : NavKey
@Serializable data object Notifications : NavKey
@Serializable data class TipDetail(val title: String, val category: String, val imageRes: Int? = null, val imageUrl: String? = null) : NavKey
