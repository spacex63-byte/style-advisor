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

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Main)
  val analysisViewModel: AnalysisViewModel = viewModel()

  NavDisplay(
    backStack = backStack,
    transitionSpec = { slideInHorizontally { it } togetherWith slideOutHorizontally { -it } },
    popTransitionSpec = { slideInHorizontally { -it } togetherWith slideOutHorizontally { it } },
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Main> {
          MainScreen(onItemClick = { navKey -> backStack.add(navKey) }, modifier = Modifier, viewModel = analysisViewModel)
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
      },
  )
}
