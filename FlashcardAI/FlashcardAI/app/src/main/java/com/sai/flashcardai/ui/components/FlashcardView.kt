package com.sai.flashcardai.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sai.flashcardai.model.Flashcard
import com.sai.flashcardai.ui.theme.*

@Composable
fun FlashcardView(
    card: Flashcard,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "flip"
    )

    val showHint = remember { mutableStateOf(false) }

    // Reset hint when card changes
    LaunchedEffect(card.id) {
        showHint.value = false
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Amber500.copy(alpha = 0.15f),
                spotColor = Amber500.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(24.dp))
            .clickable { onFlip() }
    ) {
        if (rotation <= 90f) {
            // Front — Question
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.QuestionMark,
                    contentDescription = null,
                    tint = Amber500,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = card.question,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                if (card.hint.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))

                    if (showHint.value) {
                        Surface(
                            color = CreamDark,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.Lightbulb,
                                    contentDescription = null,
                                    tint = Amber600,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = card.hint,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SlateSubtle
                                )
                            }
                        }
                    } else {
                        TextButton(onClick = { showHint.value = true }) {
                            Icon(
                                Icons.Rounded.Lightbulb,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Show hint")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Rounded.TouchApp,
                        contentDescription = null,
                        tint = SlateSubtle,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tap to reveal answer",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateSubtle
                    )
                }
            }
        } else {
            // Back — Answer
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Amber500)
                    .padding(28.dp)
                    .graphicsLayer { rotationY = 180f },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ANSWER",
                    style = MaterialTheme.typography.labelLarge,
                    color = Charcoal.copy(alpha = 0.5f),
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = card.answer,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Charcoal,
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
            }
        }
    }
}

private val sp = androidx.compose.ui.unit.sp
