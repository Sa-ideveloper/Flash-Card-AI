package com.sai.flashcardai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.flashcardai.model.Flashcard
import com.sai.flashcardai.model.QuizResult
import com.sai.flashcardai.model.UserAnswer
import com.sai.flashcardai.network.AnthropicClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Input : UiState()
    object Loading : UiState()
    data class Quiz(
        val cards: List<Flashcard>,
        val currentIndex: Int = 0,
        val isFlipped: Boolean = false
    ) : UiState()
    data class Results(val result: QuizResult, val cards: List<Flashcard>) : UiState()
    data class Error(val message: String) : UiState()
}

class FlashcardViewModel : ViewModel() {

    private val client = AnthropicClient()

    private val _uiState = MutableStateFlow<UiState>(UiState.Input)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _topic = MutableStateFlow("")
    val topic: StateFlow<String> = _topic.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _cardCount = MutableStateFlow(10)
    val cardCount: StateFlow<Int> = _cardCount.asStateFlow()

    private var flashcards = mutableListOf<Flashcard>()

    fun updateTopic(value: String) { _topic.value = value }
    fun updateNotes(value: String) { _notes.value = value }
    fun updateCardCount(value: Int) { _cardCount.value = value.coerceIn(5, 20) }

    fun generateCards() {
        if (_topic.value.isBlank()) return

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val result = client.generateFlashcards(
                topic = _topic.value,
                notes = _notes.value,
                count = _cardCount.value
            )

            result.fold(
                onSuccess = { cards ->
                    flashcards = cards.toMutableList()
                    _uiState.value = UiState.Quiz(cards = flashcards, currentIndex = 0)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(
                        error.message ?: "Failed to generate flashcards"
                    )
                }
            )
        }
    }

    fun flipCard() {
        val current = _uiState.value
        if (current is UiState.Quiz) {
            _uiState.value = current.copy(isFlipped = !current.isFlipped)
        }
    }

    fun markAnswer(answer: UserAnswer) {
        val current = _uiState.value
        if (current is UiState.Quiz) {
            flashcards[current.currentIndex] = flashcards[current.currentIndex].copy(userAnswer = answer)

            val nextIndex = current.currentIndex + 1
            if (nextIndex < flashcards.size) {
                _uiState.value = UiState.Quiz(
                    cards = flashcards.toList(),
                    currentIndex = nextIndex,
                    isFlipped = false
                )
            } else {
                showResults()
            }
        }
    }

    fun goToCard(index: Int) {
        val current = _uiState.value
        if (current is UiState.Quiz && index in flashcards.indices) {
            _uiState.value = current.copy(currentIndex = index, isFlipped = false)
        }
    }

    fun previousCard() {
        val current = _uiState.value
        if (current is UiState.Quiz && current.currentIndex > 0) {
            _uiState.value = current.copy(currentIndex = current.currentIndex - 1, isFlipped = false)
        }
    }

    fun nextCard() {
        val current = _uiState.value
        if (current is UiState.Quiz && current.currentIndex < flashcards.size - 1) {
            _uiState.value = current.copy(currentIndex = current.currentIndex + 1, isFlipped = false)
        }
    }

    private fun showResults() {
        val result = QuizResult(
            totalCards = flashcards.size,
            correct = flashcards.count { it.userAnswer == UserAnswer.CORRECT },
            incorrect = flashcards.count { it.userAnswer == UserAnswer.INCORRECT },
            unanswered = flashcards.count { it.userAnswer == UserAnswer.UNANSWERED }
        )
        _uiState.value = UiState.Results(result = result, cards = flashcards.toList())
    }

    fun finishQuiz() {
        showResults()
    }

    fun restartQuiz() {
        flashcards = flashcards.map { it.copy(userAnswer = UserAnswer.UNANSWERED, isFlipped = false) }.toMutableList()
        _uiState.value = UiState.Quiz(cards = flashcards.toList(), currentIndex = 0)
    }

    fun startOver() {
        flashcards.clear()
        _topic.value = ""
        _notes.value = ""
        _cardCount.value = 10
        _uiState.value = UiState.Input
    }

    fun retryAfterError() {
        _uiState.value = UiState.Input
    }
}
