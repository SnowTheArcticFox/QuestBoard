package dev.snowfox.questboard.work

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

private const val WORK_NAME = "quest_rollover"

object RolloverScheduler {

    // Programa el próximo sorteo para la hora de reset configurada.
    // Si esa hora ya pasó hoy, lo programa para mañana. Usa un nombre
    // único ("quest_rollover") con política REPLACE, así reprogramar
    // (por ejemplo al cambiar la hora en Ajustes) reemplaza lo anterior
    // en vez de duplicar tareas.
    fun scheduleNext(context: Context, resetHour: Int) {
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atTime(LocalTime.of(resetHour, 0))
        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }
        val delayMillis = Duration.between(now, next).toMillis()

        val request = OneTimeWorkRequestBuilder<RolloverWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}