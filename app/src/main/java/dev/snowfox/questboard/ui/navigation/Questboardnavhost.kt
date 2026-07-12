package dev.snowfox.questboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.snowfox.questboard.ui.screens.QuestBoardScreen
import dev.snowfox.questboard.ui.screens.QuestFormScreen
import dev.snowfox.questboard.ui.screens.QuestPoolScreen
import dev.snowfox.questboard.ui.screens.SettingsScreen
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun QuestBoardNavHost(
    navController: NavHostController,
    viewModel: QuestBoardViewModel,
    onOpenDrawer: () -> Unit
) {
    NavHost(navController = navController, startDestination = QuestRoutes.BOARD) {
        composable(QuestRoutes.BOARD) {
            QuestBoardScreen(viewModel = viewModel, onOpenDrawer = onOpenDrawer)
        }

        composable(QuestRoutes.POOL) {
            QuestPoolScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer,
                onAddTemplate = { navController.navigate(QuestRoutes.FORM) },
                onEditTemplate = { id -> navController.navigate("${QuestRoutes.FORM}?${QuestRoutes.ARG_QUEST_ID}=$id") }
            )
        }

        composable(
            route = "${QuestRoutes.FORM}?${QuestRoutes.ARG_QUEST_ID}={${QuestRoutes.ARG_QUEST_ID}}",
            arguments = listOf(
                navArgument(QuestRoutes.ARG_QUEST_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(QuestRoutes.ARG_QUEST_ID) ?: -1
            QuestFormScreen(
                viewModel = viewModel,
                templateId = if (id == -1) null else id,
                onDone = { navController.popBackStack() }
            )
        }

        composable(QuestRoutes.SETTINGS) {
            SettingsScreen(viewModel = viewModel, onOpenDrawer = onOpenDrawer)
        }
    }
}