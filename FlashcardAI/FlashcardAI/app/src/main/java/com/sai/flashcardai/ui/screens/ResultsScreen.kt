package com.sai.flashcardai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sai.flashcardai.model.Flashcard
import com.sai.flashcardai.model.QuizResult
import com.sai.flashcardai.model.UserAnswer
import com.sai.flashcardai.ui.theme.*

@Composable
fun ResultsScreen(
    result: QuizResult,
    cards: List<Flashcard>,
    onRetry: () -> Unit,
    onNewTopic: () -> Unit
) {
    val scrollState = rememberScrollState()

    val emoji = when {
        result.scorePercentage >= 90 -> "🎉"
        result.scorePercentage >= 70 -> "👏"
        result.scorePercentage >= 50 -> "💪"
        else -> "📚"
    }

    val message = when {
        result.scorePercentage >= 90 -> "Outstanding!"
        result.scorePercentage >= 70 -> "Great job!"
        result.scorePercentage >= 50 -> "Keep going!"
        else -> "Keep studying!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(text = emoji, fontSize = 64.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You scored ${result.scorePercentage}%",
            style = MaterialTheme.typography.headlineMedium,
            color = Amber500,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(
                label = "Correct",
                count = result.correct,
                color = Sage,
                icon = Icons.Rounded.Check
            )
            StatCard(
                label = "Incorrect",
                count = result.incorrect,
                color = Coral,
                icon = Icons.Rounded.Close
            )
            StatCard(
                label = "Skipped",
                count = result.unanswered,
                color = SlateSubtle,
                icon = Icons.Rounded.Remove
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Missed cards review
        val missedCards = cards.filter { it.userAnswer == UserAnswer.INCORRECT }
        if (missedCards.isNotEmpty()) {
            Text(
                text = "Review missed cards",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            missedCards.forEach { card ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = card.question,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = SageLight.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = card.answer,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Sage,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber500,
                contentColor = Charcoal
            )
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry Same Cards", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNewTopic,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber600)
        ) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("New Topic", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun StatCard(
    label: String,
    count: Int,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                fontWeight = FontWeight.Black
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}
