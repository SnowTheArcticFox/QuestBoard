package dev.snowfox.questboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dev.snowfox.questboard.ui.navigation.QuestBoardNavHost
import dev.snowfox.questboard.ui.theme.QuestBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuestBoardTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    QuestBoardNavHost()
                }
            }
        }
    }
}