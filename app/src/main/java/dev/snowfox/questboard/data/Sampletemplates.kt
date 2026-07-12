package dev.snowfox.questboard.data

import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.model.QuestType

// Pool de ejemplo, solo para la primera vez que se abre la app.
val sampleTemplates = listOf(
    QuestTemplate(1, "Lavarte los dientes", QuestType.DAILY, 10),
    QuestTemplate(2, "Tomar 2L de agua", QuestType.DAILY, 10),
    QuestTemplate(3, "Salir a caminar 15 min", QuestType.DAILY, 15),
    QuestTemplate(4, "Leer 10 páginas", QuestType.DAILY, 10),
    QuestTemplate(5, "Ordenar el escritorio", QuestType.DAILY, 10),
    QuestTemplate(6, "Hacer estiramientos", QuestType.DAILY, 10),
    QuestTemplate(7, "Llamar a un amigo", QuestType.WEEKLY, 30),
    QuestTemplate(8, "Limpiar a fondo tu pieza", QuestType.WEEKLY, 30),
    QuestTemplate(9, "Cocinar algo nuevo", QuestType.WEEKLY, 25),
    QuestTemplate(10, "Revisar presupuesto del mes", QuestType.MONTHLY, 60),
)