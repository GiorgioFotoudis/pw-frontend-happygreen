package com.example.frontend_happygreen.viewmodel

/*import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.QuizDto
import com.example.frontend_happygreen.data.model.QuizSubmissionDto
import com.example.frontend_happygreen.data.model.QuizAnswerDto
import com.example.frontend_happygreen.data.model.QuizResultDto
import com.example.frontend_happygreen.data.remote.ApiService
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private val apiService = ApiService()

    var quizList by mutableStateOf<List<QuizDto>>(emptyList())
        private set

    var currentQuiz by mutableStateOf<QuizDto?>(null)
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    var selectedAnswers by mutableStateOf<Map<Int, Int>>(emptyMap())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var showResult by mutableStateOf(false)
        private set

    var quizResult by mutableStateOf<QuizResultDto?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadQuizList() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                quizList = apiService.getAllQuiz()
            } catch (e: Exception) {
                errorMessage = "Errore nel caricamento dei quiz: ${e.message}"
                Log.e("QuizViewModel", "Error loading quiz list", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun startQuiz(quiz: QuizDto) {
        currentQuiz = quiz
        currentQuestionIndex = 0
        selectedAnswers = emptyMap()
        showResult = false
        quizResult = null
        errorMessage = null
    }

    fun selectAnswer(questionId: Int, answerId: Int) {
        selectedAnswers = selectedAnswers.toMutableMap().apply {
            put(questionId, answerId)
        }
    }

    fun nextQuestion() {
        currentQuiz?.let { quiz ->
            if (currentQuestionIndex < quiz.domande.size - 1) {
                currentQuestionIndex++
            }
        }
    }

    fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
        }
    }

    fun submitQuiz() {
        currentQuiz?.let { quiz ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null
                try {
                    val answers = selectedAnswers.map { (questionId, answerId) ->
                        QuizAnswerDto(domanda_id = questionId, risposta_id = answerId)
                    }

                    val submission = QuizSubmissionDto(risposte = answers)
                    val result = apiService.submitQuiz(quiz.id, submission)

                    quizResult = result
                    showResult = true
                } catch (e: Exception) {
                    errorMessage = "Errore nell'invio del quiz: ${e.message}"
                    Log.e("QuizViewModel", "Error submitting quiz", e)
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun resetQuiz() {
        currentQuiz = null
        currentQuestionIndex = 0
        selectedAnswers = emptyMap()
        showResult = false
        quizResult = null
        errorMessage = null
    }

    fun getCurrentQuestion() = currentQuiz?.domande?.getOrNull(currentQuestionIndex)

    fun isLastQuestion() = currentQuiz?.let { currentQuestionIndex >= it.domande.size - 1 } ?: false

    fun isFirstQuestion() = currentQuestionIndex == 0

    fun getProgress() = currentQuiz?.let { (currentQuestionIndex + 1) / it.domande.size.toFloat() } ?: 0f

    fun isQuestionAnswered(questionId: Int) = selectedAnswers.containsKey(questionId)

    fun areAllQuestionsAnswered() = currentQuiz?.let { quiz ->
        quiz.domande.all { question -> selectedAnswers.containsKey(question.id) }
    } ?: false
}*/