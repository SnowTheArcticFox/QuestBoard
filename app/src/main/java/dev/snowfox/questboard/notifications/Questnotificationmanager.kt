package dev.snowfox.questboard.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dev.snowfox.questboard.MainActivity
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.model.QuestType

private const val CHANNEL_BOARD = "quest_board_channel"
private const val CHANNEL_RELOAD = "quest_reload_channel"
private const val NOTIF_ID_BOARD = 1001
private const val NOTIF_ID_RELOAD = 1002

// OJO: si tu MainActivity no está en el paquete raíz, ajusta el import de
// arriba a donde realmente esté.
object QuestNotificationManager {

    fun ensureChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return

        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_BOARD,
                "Misiones pendientes",
                NotificationManager.IMPORTANCE_LOW // sin sonido, discreta, tipo "estado"
            ).apply {
                description = "Muestra tus próximas misiones pendientes"
                setShowBadge(false)
            }
        )

        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_RELOAD,
                "Misiones renovadas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisa cuando se sortean misiones nuevas"
            }
        )
    }

    private fun hasPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openAppIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // Notificación "tablero": persistente (no se puede deslizar para
    // quitarla), muestra hasta 3 misiones pendientes priorizando
    // diarias -> semanales -> mensuales. Se llama cada vez que cambian
    // las misiones activas (marcar/desmarcar, sorteo nuevo, etc).
    fun updateBoardNotification(context: Context, quests: List<Quest>) {
        ensureChannels(context)
        if (!hasPermission(context)) return

        val pendingDaily = quests.filter { it.type == QuestType.DAILY && !it.completed }
        val pendingWeekly = quests.filter { it.type == QuestType.WEEKLY && !it.completed }
        val pendingMonthly = quests.filter { it.type == QuestType.MONTHLY && !it.completed }
        val preview = (pendingDaily + pendingWeekly + pendingMonthly).take(3)

        val builder = NotificationCompat.Builder(context, CHANNEL_BOARD)
            .setSmallIcon(context.applicationInfo.icon) // TODO: reemplazar por un ícono propio cuando tengas uno
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppIntent(context))

        if (preview.isEmpty()) {
            builder
                .setContentTitle("QuestBoard")
                .setContentText("No hay misiones pendientes 🎉")
        } else {
            val style = NotificationCompat.InboxStyle()
            preview.forEach { quest -> style.addLine("${quest.title}  (+${quest.xp} XP)") }

            builder
                .setContentTitle("${preview.size} misión(es) pendiente(s)")
                .setContentText(preview.first().title)
                .setStyle(style)
        }

        NotificationManagerCompat.from(context).notify(NOTIF_ID_BOARD, builder.build())
    }

    // Notificación normal (se puede descartar), solo para el momento en
    // que de verdad hubo un sorteo nuevo.
    fun showReloadNotification(context: Context) {
        ensureChannels(context)
        if (!hasPermission(context)) return

        val notification = NotificationCompat.Builder(context, CHANNEL_RELOAD)
            .setSmallIcon(context.applicationInfo.icon) // TODO: reemplazar por un ícono propio
            .setContentTitle("QuestBoard")
            .setContentText("¡Misiones renovadas! Toca para verlas.")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openAppIntent(context))
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_RELOAD, notification)
    }
}