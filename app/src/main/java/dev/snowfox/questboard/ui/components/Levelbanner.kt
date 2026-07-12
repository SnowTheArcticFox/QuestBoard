package dev.snowfox.questboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snowfox.questboard.model.LevelInfo
import dev.snowfox.questboard.ui.theme.QuestColors

@Composable
fun LevelBanner(levelInfo: LevelInfo) {
    val progress = if (levelInfo.xpForNextLevel > 0) {
        (levelInfo.xpIntoLevel.toFloat() / levelInfo.xpForNextLevel.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(QuestColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "Nv. ${levelInfo.level}",
                color = QuestColors.Accent,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(QuestColors.Divider)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(QuestColors.Accent)
            )
        }

        Text(
            text = "${levelInfo.xpIntoLevel} / ${levelInfo.xpForNextLevel} XP",
            color = QuestColors.TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}