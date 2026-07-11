package dev.snowfox.questboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.snowfox.questboard.ui.theme.QuestBoardTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuestBoardTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    QuestBoardScreen()
                }
            }
        }
    }
}
@Composable
fun QuestBoardScreen(){

    val quests = listOf(
        Quest(
            title = "Brush Teeth",
            type = QuestType.DAILY
        ),
        Quest(
            title = "Open the curtains",
            type = QuestType.DAILY
        ),
        Quest(
            title = "Open the windows",
            type = QuestType.DAILY
        ),
        Quest(
            title = "Wash dishes",
            type = QuestType.WEEKLY
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){
        Text(
            text= "Questboard!"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        quests.forEach { quest ->
            Text(
                text = quest.title
            )
        }
    }
}