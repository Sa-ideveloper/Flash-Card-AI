package com.sai.flashcardai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sai.flashcardai.model.UserAnswer
import com.sai.flashcardai.ui.screens.*
import com.sai.flashcardai.ui.theme.*
import com.sai.flashcardai.viewmodel.FlashcardViewModel
import com.sai.flashcardai.viewmodel.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FlashcardAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlashcardApp()
                }
            }
        }
    }
}

@Composable
fun FlashcardApp(viewModel: FlashcardViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val topic by viewModel.topic.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val cardCount by viewModel.cardCount.collectAsState()

    AnimatedContent(
        targetState = uiState,
        transitionSpec = {
            fadeIn() + slideInHorizontally { it / 3 } togetherWith
                    fadeOut() + slideOutHorizontally { -it / 3 }
        },
        label = "screen"
    ) { state ->
        when (state) {
            is UiState.Input -> {
                InputScreen(
                    topic = topic,
                    notes = notes,
                    cardCount = cardCount,
                    onTopicChange = viewModel::updateTopic,
                    onNotesChange = viewModel::updateNotes,
                    onCardCountChange = viewModel::updateCardCount,
                    onGenerate = viewModel::generateCards
                )
            }

            is UiState.Loading -> {
                LoadingScreen()
            }

            is UiState.Quiz -> {
                QuizScreen(
                    cards = state.cards,
                    currentIndex = state.currentIndex,
                    isFlipped = state.isFlipped,
                    onFlip = viewModel::flipCard,
                    onMarkCorrect = { viewModel.markAnswer(UserAnswer.CORRECT) },
                    onMarkIncorrect = { viewModel.markAnswer(UserAnswer.INCORRECT) },
                    onPrevious = viewModel::previousCard,
                    onNext = viewModel::nextCard,
                    onFinish = viewModel::finishQuiz
                )
            }

            is UiState.Results -> {
                ResultsScreen(
                    result = state.result,
                    cards = state.cards,
                    onRetry = viewModel::restartQuiz,
                    onNewTopic = viewModel::startOver
                )
            }

            is UiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = viewModel::retryAfterError
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Warning,
            contentDescription = null,
            tint = Coral,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = SlateSubtle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber500,
                contentColor = Charcoal
            )
        ) {
            Text("Try Again", fontWeight = FontWeight.Bold)
        }
    }
}
