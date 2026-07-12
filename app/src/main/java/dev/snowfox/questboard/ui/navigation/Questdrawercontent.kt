package dev.snowfox.questboard.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snowfox.questboard.ui.theme.QuestColors

@Composable
fun QuestDrawerContent(onNavigate: (String) -> Unit) {
    ModalDrawerSheet(drawerContainerColor = QuestColors.Surface) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "QuestBoard",
            color = QuestColors.Accent,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(24.dp))

        DrawerItem("Misiones de hoy", QuestRoutes.BOARD, onNavigate)
        DrawerItem("Pool de misiones", QuestRoutes.POOL, onNavigate)
        DrawerItem("Configuración", QuestRoutes.SETTINGS, onNavigate)
    }
}

@Composable
private fun DrawerItem(label: String, route: String, onNavigate: (String) -> Unit) {
    Text(
        text = label,
        color = QuestColors.TextPrimary,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(route) }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    )
}