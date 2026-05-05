package com.sai.flashcardai.network

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.sai.flashcardai.BuildConfig
import com.sai.flashcardai.model.Flashcard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class AnthropicClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun generateFlashcards(topic: String, notes: String, count: Int = 10): Result<List<Flashcard>> {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = buildPrompt(topic, notes, count)
                val requestBody = buildRequestBody(prompt)

                val request = Request.Builder()
                    .url("https://api.anthropic.com/v1/messages")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", BuildConfig.ANTHROPIC_API_KEY)
                    .addHeader("anthropic-version", "2023-06-01")
                    .post(requestBody.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: throw Exception("Empty response")

                if (!response.isSuccessful) {
                    throw Exception("API error (${response.code}): $responseBody")
                }

                val cards = parseResponse(responseBody)
                Result.success(cards)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun buildPrompt(topic: String, notes: String, count: Int): String {
        val notesSection = if (notes.isNotBlank()) {
            "\n\nHere are the user's notes to base the flashcards on:\n$notes"
        } else ""

        return """Generate exactly $count flashcards about: $topic$notesSection

Return ONLY a JSON array with no other text. Each object must have these fields:
- "question": a clear, specific question
- "answer": a concise but complete answer (2-3 sentences max)
- "hint": a short hint to help recall (5-10 words)

Example format:
[{"question":"What is X?","answer":"X is...","hint":"Think about..."}]

Make questions progressively harder. Mix factual recall, conceptual understanding, and application questions."""
    }

    private fun buildRequestBody(prompt: String): String {
        val body = mapOf(
            "model" to "claude-sonnet-4-20250514",
            "max_tokens" to 4096,
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            )
        )
        return gson.toJson(body)
    }

    private fun parseResponse(responseBody: String): List<Flashcard> {
        val json = JsonParser.parseString(responseBody).asJsonObject
        val content = json.getAsJsonArray("content")
        val text = content[0].asJsonObject.get("text").asString

        // Extract JSON array from response (handle potential markdown wrapping)
        val jsonStr = text
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val array = JsonParser.parseString(jsonStr).asJsonArray

        return array.mapIndexed { index, element ->
            val obj = element.asJsonObject
            Flashcard(
                id = index,
                question = obj.get("question").asString,
                answer = obj.get("answer").asString,
                hint = obj.get("hint")?.asString ?: ""
            )
        }
    }
}
