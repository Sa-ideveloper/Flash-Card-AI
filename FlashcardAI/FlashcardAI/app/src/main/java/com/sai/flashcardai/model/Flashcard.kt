package com.sai.flashcardai.model

data class Flashcard(
    val id: Int,
    val question: String,
    val answer: String,
    val hint: String = "",
    var isFlipped: Boolean = false,
    var userAnswer: UserAnswer = UserAnswer.UNANSWERED
)

enum class UserAnswer {
    UNANSWERED, CORRECT, INCORRECT
}

data class QuizResult(
    val totalCards: Int,
    val correct: Int,
    val incorrect: Int,
    val unanswered: Int
) {
    val scorePercentage: Int
        get() = if (totalCards > 0) (correct * 100 / totalCards) else 0
}
