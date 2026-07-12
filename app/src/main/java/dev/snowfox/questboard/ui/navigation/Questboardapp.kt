package dev.snowfox.questboard.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel
import kotlinx.coroutines.launch

// Composable raíz: llama a esta función desde tu MainActivity.
@Composable
fun QuestBoardApp() {
    val navController = rememberNavController()
    val viewModel: QuestBoardViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // En Android 15+ el sistema fuerza edge-to-edge y ya no se puede pintar
    // la barra de estado vía window.statusBarColor (quedó deprecated y no
    // hace nada). En su lugar, pintamos nosotros mismos un rectángulo negro
    // del alto exacto de la barra de estado, y el contenido real de la app
    // va debajo, completamente fuera de esa zona.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Espaciador negro (hereda el background del Column de arriba)
        // del alto exacto de la barra de estado / notch de cámara.
        Spacer(
            modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars)
        )

        ModalNavigationDrawer(
            modifier = Modifier.weight(1f),
            drawerState = drawerState,
            drawerContent = {
                QuestDrawerContent(onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                })
            }
        ) {
            QuestBoardNavHost(
                navController = navController,
                viewModel = viewModel,
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }
    }
}