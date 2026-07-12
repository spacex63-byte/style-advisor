package com.example.styleadvisor

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.styleadvisor.ui.main.MainScreen

import androidx.compose.animation.*

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.styleadvisor.ui.main.AnalysisViewModel
import com.example.styleadvisor.ui.profile.HelpSupportScreen
import com.example.styleadvisor.ui.profile.PrivacyPolicyScreen
import com.example.styleadvisor.ui.profile.StyleProfileScreen
import com.example.styleadvisor.ui.profile.ProfileViewModel

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Onboarding)
  val analysisViewModel: AnalysisViewModel = viewModel()
  val profileViewModel: ProfileViewModel = viewModel()

  NavDisplay(
    backStack = backStack,
    transitionSpec = { slideInHorizontally { it } togetherWith slideOutHorizontally { -it } },
    popTransitionSpec = { slideInHorizontally { -it } togetherWith slideOutHorizontally { it } },
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Onboarding> {
          com.example.styleadvisor.ui.onboarding.OnboardingScreen(
            onGetStarted = { 
                backStack.removeLastOrNull()
                backStack.add(Main) 
            }
          )
        }
        entry<Main> {
          MainScreen(onItemClick = { navKey -> backStack.add(navKey) }, modifier = Modifier, viewModel = analysisViewModel, profileViewModel = profileViewModel)
        }
        entry<AnalysisResult> {
          com.example.styleadvisor.ui.result.AnalysisResultScreen(onBack = { backStack.removeLastOrNull() }, viewModel = analysisViewModel)
        }
        entry<HelpSupport> {
          HelpSupportScreen(onBack = { backStack.removeLastOrNull() })
        }
        entry<PrivacyPolicy> {
          PrivacyPolicyScreen(onBack = { backStack.removeLastOrNull() })
        }
        entry<StyleProfile> {
          StyleProfileScreen(onBack = { backStack.removeLastOrNull() }, viewModel = profileViewModel)
        }
        entry<Notifications> {
          com.example.styleadvisor.ui.notifications.NotificationScreen(onBack = { backStack.removeLastOrNull() })
        }
        entry<TipDetail> {
          com.example.styleadvisor.ui.tips.TipDetailScreen(
            title = it.title,
            category = it.category,
            imageRes = it.imageRes,
            imageUrl = it.imageUrl,
            onBack = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}
