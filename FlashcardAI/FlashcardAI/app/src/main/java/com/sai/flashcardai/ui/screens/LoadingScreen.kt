package com.sai.flashcardai.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sai.flashcardai.ui.theme.*

@Composable
fun LoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val dots = listOf(
        infiniteTransition.animateFloat(
            initialValue = 0.3f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(600), RepeatMode.Reverse,
                initialStartOffset = StartOffset(0)
            ), label = "dot1"
        ),
        infiniteTransition.animateFloat(
            initialValue = 0.3f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(600), RepeatMode.Reverse,
                initialStartOffset = StartOffset(200)
            ), label = "dot2"
        ),
        infiniteTransition.animateFloat(
            initialValue = 0.3f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(600), RepeatMode.Reverse,
                initialStartOffset = StartOffset(400)
            ), label = "dot3"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            dots.forEach { anim ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .scale(anim.value)
                        .alpha(anim.value)
                        .clip(CircleShape)
                        .background(Amber500)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Generating your flashcards...",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Claude is crafting questions\ntailored to your topic",
            style = MaterialTheme.typography.bodyMedium,
            color = SlateSubtle,
            textAlign = TextAlign.Center
        )
    }
}
