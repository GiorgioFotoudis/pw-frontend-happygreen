package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.*
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val api = ApiClient.service

    private val _quizList = MutableStateFlow<List<QuizDto>>(emptyList())
    val quizList: StateFlow<List<QuizDto>> = _quizList.asStateFlow()

    private val _currentQuiz = MutableStateFlow<QuizDto?>(null)
    val currentQuiz: StateFlow<QuizDto?> = _currentQuiz.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

    private val _quizResult = MutableStateFlow<QuizResultDto?>(null)
    val quizResult: StateFlow<QuizResultDto?> = _quizResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _userResults = MutableStateFlow<List<RisultatoQuizDto>>(emptyList())
    val userResults: StateFlow<List<RisultatoQuizDto>> = _userResults.asStateFlow()

    fun loadAllQuiz(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val quiz = api.getAllQuiz("Bearer $token")
                _quizList.value = quiz
            } catch (e: Exception) {
                _error.value = "Errore nel caricamento dei quiz: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startQuiz(quiz: QuizDto) {
        _currentQuiz.value = quiz
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = mutableMapOf()
        _quizResult.value = null
    }

    fun selectAnswer(questionId: Int, answerId: Int) {
        val currentAnswers = _selectedAnswers.value.toMutableMap()
        currentAnswers[questionId] = answerId
        _selectedAnswers.value = currentAnswers
    }

    fun nextQuestion() {
        val currentQuiz = _currentQuiz.value ?: return
        if (_currentQuestionIndex.value < currentQuiz.domande.size - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun submitQuiz(token: String) {
        val quiz = _currentQuiz.value ?: return
        val answers = _selectedAnswers.value

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val answerList = answers.map { (questionId, answerId) ->
                    QuizAnswerDto(domanda_id = questionId, risposta_id = answerId)
                }

                val submission = QuizSubmissionDto(risposte = answerList)
                val result = api.submitQuiz(quiz.id, "Bearer $token", submission)
                _quizResult.value = result
            } catch (e: Exception) {
                _error.value = "Errore nell'invio del quiz: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserResults(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val results = api.getUserQuizResults("Bearer $token")
                _userResults.value = results
            } catch (e: Exception) {
                _error.value = "Errore nel caricamento dei risultati: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetQuiz() {
        _currentQuiz.value = null
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = mutableMapOf()
        _quizResult.value = null
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }

    fun getCurrentQuestion(): DomandaDto? {
        val quiz = _currentQuiz.value ?: return null
        val index = _currentQuestionIndex.value
        return if (index < quiz.domande.size) quiz.domande[index] else null
    }

    fun isQuestionAnswered(questionId: Int): Boolean {
        return _selectedAnswers.value.containsKey(questionId)
    }

    fun canProceedToNext(): Boolean {
        val currentQuestion = getCurrentQuestion() ?: return false
        return isQuestionAnswered(currentQuestion.id)
    }

    fun isLastQuestion(): Boolean {
        val quiz = _currentQuiz.value ?: return true
        return _currentQuestionIndex.value >= quiz.domande.size - 1
    }

    fun getProgressPercentage(): Float {
        val quiz = _currentQuiz.value ?: return 0f
        return if (quiz.domande.isNotEmpty()) {
            (_currentQuestionIndex.value + 1).toFloat() / quiz.domande.size.toFloat()
        } else 0f
    }
}