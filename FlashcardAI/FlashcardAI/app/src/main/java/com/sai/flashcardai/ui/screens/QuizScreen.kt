package com.sai.flashcardai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sai.flashcardai.model.Flashcard
import com.sai.flashcardai.model.UserAnswer
import com.sai.flashcardai.ui.components.FlashcardView
import com.sai.flashcardai.ui.theme.*

@Composable
fun QuizScreen(
    cards: List<Flashcard>,
    currentIndex: Int,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onMarkCorrect: () -> Unit,
    onMarkIncorrect: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit
) {
    val card = cards[currentIndex]
    val progress = (currentIndex + 1).toFloat() / cards.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar + counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${currentIndex + 1} / ${cards.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row {
                val answered = cards.count { it.userAnswer != UserAnswer.UNANSWERED }
                val correct = cards.count { it.userAnswer == UserAnswer.CORRECT }
                Text(
                    text = "$correct✓  ${answered - correct}✗",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateSubtle
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Amber500,
            trackColor = CreamDark,
        )

        // Card status dots
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            cards.forEachIndexed { index, c ->
                val dotColor = when {
                    index == currentIndex -> Amber500
                    c.userAnswer == UserAnswer.CORRECT -> Sage
                    c.userAnswer == UserAnswer.INCORRECT -> Coral
                    else -> CreamDark
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(if (index == currentIndex) 10.dp else 7.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Flashcard
        FlashcardView(
            card = card,
            isFlipped = isFlipped,
            onFlip = onFlip,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Answer buttons (visible when flipped)
        AnimatedVisibility(
            visible = isFlipped,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Did you know the answer?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateSubtle
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Incorrect button
                    OutlinedButton(
                        onClick = onMarkIncorrect,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Coral
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Coral.copy(alpha = 0.5f))
                        )
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Didn't Know", fontWeight = FontWeight.SemiBold)
                    }

                    // Correct button
                    Button(
                        onClick = onMarkCorrect,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Sage,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        )
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Got It!", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevious,
                enabled = currentIndex > 0
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Previous",
                    tint = if (currentIndex > 0) MaterialTheme.colorScheme.onBackground else SlateSubtle
                )
            }

            TextButton(onClick = onFinish) {
                Text("Finish Quiz", color = Amber600, fontWeight = FontWeight.SemiBold)
            }

            IconButton(
                onClick = onNext,
                enabled = currentIndex < cards.size - 1
            ) {
                Icon(
                    Icons.Rounded.ArrowForward,
                    contentDescription = "Next",
                    tint = if (currentIndex < cards.size - 1) MaterialTheme.colorScheme.onBackground else SlateSubtle
                )
            }
        }
    }
}
