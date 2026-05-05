package com.sai.flashcardai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sai.flashcardai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    topic: String,
    notes: String,
    cardCount: Int,
    onTopicChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onCardCountChange: (Int) -> Unit,
    onGenerate: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Text(
            text = "FlashcardAI",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Generate smart flashcards\nfrom any topic or notes",
            style = MaterialTheme.typography.bodyLarge,
            color = SlateSubtle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Topic input
        OutlinedTextField(
            value = topic,
            onValueChange = onTopicChange,
            label = { Text("Topic") },
            placeholder = { Text("e.g. Transformer Architecture, US History...") },
            leadingIcon = {
                Icon(Icons.Rounded.School, contentDescription = null, tint = Amber500)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Amber500,
                cursorColor = Amber500,
                focusedLabelColor = Amber500
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Notes input
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notes (optional)") },
            placeholder = { Text("Paste your study notes, lecture content, or key points here...") },
            leadingIcon = {
                Icon(Icons.Rounded.Notes, contentDescription = null, tint = Amber500)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Amber500,
                cursorColor = Amber500,
                focusedLabelColor = Amber500
            ),
            maxLines = 8
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card count selector
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Number of cards: $cardCount",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = cardCount.toFloat(),
                    onValueChange = { onCardCountChange(it.toInt()) },
                    valueRange = 5f..20f,
                    steps = 14,
                    colors = SliderDefaults.colors(
                        thumbColor = Amber500,
                        activeTrackColor = Amber500,
                        inactiveTrackColor = CreamDark
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("5", style = MaterialTheme.typography.bodySmall, color = SlateSubtle)
                    Text("20", style = MaterialTheme.typography.bodySmall, color = SlateSubtle)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Generate button
        Button(
            onClick = onGenerate,
            enabled = topic.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber500,
                contentColor = Charcoal,
                disabledContainerColor = CreamDark,
                disabledContentColor = SlateSubtle
            )
        ) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Generate Flashcards",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
