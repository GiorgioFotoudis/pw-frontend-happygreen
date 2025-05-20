package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.frontend_happygreen.data.model.QuizDto

class QuizViewModel : ViewModel() {
    var quizList: List<QuizDto> = emptyList()

    // getAllQuiz(), submitResult()... (da fare)
}
