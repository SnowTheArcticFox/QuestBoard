package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.components.QuestSectionTitle
import dev.snowfox.questboard.ui.components.QuestTopBar
import dev.snowfox.questboard.ui.components.TemplateCard
import dev.snowfox.questboard.ui.theme.QuestColors
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun QuestPoolScreen(
    viewModel: QuestBoardViewModel,
    onOpenDrawer: () -> Unit,
    onAddTemplate: () -> Unit,
    onEditTemplate: (Int) -> Unit
) {
    val templates by viewModel.templates.collectAsStateWithLifecycle()

    val daily = templates.filter { it.type == QuestType.DAILY }
    val weekly = templates.filter { it.type == QuestType.WEEKLY }
    val monthly = templates.filter { it.type == QuestType.MONTHLY }

    Scaffold(
        containerColor = QuestColors.Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTemplate,
                containerColor = QuestColors.Accent,
                contentColor = QuestColors.Background
            ) {
                Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(QuestColors.Background),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = padding.calculateBottomPadding() + 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                QuestTopBar(title = "Pool de misiones", onOpenDrawer = onOpenDrawer)
                Spacer(Modifier.height(16.dp))
            }

            poolSection("Diarias", daily, onEditTemplate)
            poolSection("Semanales", weekly, onEditTemplate)
            poolSection("Mensuales", monthly, onEditTemplate)

            if (templates.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no hay misiones en el pool. Toca + para crear la primera.",
                        color = QuestColors.TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

private fun LazyListScope.poolSection(title: String, templates: List<QuestTemplate>, onEdit: (Int) -> Unit) {
    if (templates.isEmpty()) return
    item { QuestSectionTitle(title) }
    items(templates, key = { it.id }) { template ->
        TemplateCard(template = template, onClick = { onEdit(template.id) })
    }
    item { Spacer(Modifier.height(16.dp)) }
}