package dev.snowfox.questboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.snowfox.questboard.ui.screens.QuestBoardScreen
import dev.snowfox.questboard.ui.screens.QuestFormScreen
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

private const val ROUTE_BOARD = "board"
private const val ROUTE_FORM = "form"
private const val ARG_QUEST_ID = "questId"

@Composable
fun QuestBoardNavHost() {
    val navController = rememberNavController()

    // Un solo ViewModel compartido por toda la app (se crea acá arriba,
    // en el NavHost, en vez de en cada pantalla): así el formulario ve
    // y modifica la misma lista que el tablero, sin duplicar estado.
    val viewModel: QuestBoardViewModel = viewModel()

    NavHost(navController = navController, startDestination = ROUTE_BOARD) {
        composable(ROUTE_BOARD) {
            QuestBoardScreen(
                viewModel = viewModel,
                onAddQuest = { navController.navigate(ROUTE_FORM) },
                onEditQuest = { id -> navController.navigate("$ROUTE_FORM?$ARG_QUEST_ID=$id") }
            )
        }
        composable(
            route = "$ROUTE_FORM?$ARG_QUEST_ID={$ARG_QUEST_ID}",
            arguments = listOf(
                navArgument(ARG_QUEST_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val questId = backStackEntry.arguments?.getInt(ARG_QUEST_ID) ?: -1
            QuestFormScreen(
                viewModel = viewModel,
                questId = if (questId == -1) null else questId,
                onDone = { navController.popBackStack() }
            )
        }
    }
}