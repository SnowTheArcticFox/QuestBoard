package dev.snowfox.questboard.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.snowfox.questboard.data.repository.QuestRepository
import dev.snowfox.questboard.notifications.QuestNotificationManager
import kotlinx.coroutines.flow.first

// Se ejecuta en segundo plano a la hora de reset configurada. Hace el
// sorteo (si corresponde), actualiza la notificación del tablero, avisa
// con la notificación de renovación si de verdad hubo sorteo nuevo, y se
// vuelve a programar a sí mismo para la siguiente vez.
class RolloverWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = QuestRepository(applicationContext)

        val didRoll = repository.rollIfNeeded()

        val currentData = repository.appDataFlow.first()
        QuestNotificationManager.updateBoardNotification(applicationContext, currentData.activeQuests)

        if (didRoll) {
            QuestNotificationManager.showReloadNotification(applicationContext)
        }

        // Vuelve a programarse para el siguiente ciclo, respetando la
        // hora de reset actual (por si cambió mientras tanto).
        RolloverScheduler.scheduleNext(applicationContext, currentData.settings.resetHour)

        return Result.success()
    }
}