package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontend_happygreen.data.model.QuizDto
import com.example.frontend_happygreen.data.model.QuizResultDto
import com.example.frontend_happygreen.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    token: String
) {
    val quizViewModel: QuizViewModel = viewModel()
    val quizList by quizViewModel.quizList.collectAsState()
    val currentQuiz by quizViewModel.currentQuiz.collectAsState()
    val currentQuestionIndex by quizViewModel.currentQuestionIndex.collectAsState()
    val selectedAnswers by quizViewModel.selectedAnswers.collectAsState()
    val quizResult by quizViewModel.quizResult.collectAsState()
    val isLoading by quizViewModel.isLoading.collectAsState()
    val error by quizViewModel.error.collectAsState()

    // Carica i quiz all'avvio
    LaunchedEffect(token) {
        quizViewModel.loadAllQuiz(token)
    }

    // Gestione errori con Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Long
            )
            quizViewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Top App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (currentQuiz != null) {
                        quizViewModel.resetQuiz()
                    } else {
                        navController.navigateUp()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                }

                Text(
                    text = currentQuiz?.titolo ?: "🧠 Quiz Verde",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.width(48.dp)) // Per bilanciare l'icona a sinistra
            }

            Spacer(modifier = Modifier.height(16.dp))

            val currentQuizSnapshot = currentQuiz
            val quizResultSnapshot = quizResult

            when {
                isLoading -> LoadingContent()
                quizResultSnapshot != null -> QuizResultContent(
                    result = quizResultSnapshot,
                    onRetry = { quizViewModel.resetQuiz() },
                    onBackToList = { quizViewModel.resetQuiz() }
                )
                currentQuizSnapshot != null -> QuizQuestionContent(
                    quiz = currentQuizSnapshot,
                    currentQuestionIndex = currentQuestionIndex,
                    selectedAnswers = selectedAnswers,
                    onAnswerSelected = { questionId, answerId ->
                        quizViewModel.selectAnswer(questionId, answerId)
                    },
                    onNextQuestion = { quizViewModel.nextQuestion() },
                    onPreviousQuestion = { quizViewModel.previousQuestion() },
                    onSubmitQuiz = { token.let { quizViewModel.submitQuiz(it) } },
                    canProceedToNext = quizViewModel.canProceedToNext(),
                    isLastQuestion = quizViewModel.isLastQuestion(),
                    progressPercentage = quizViewModel.getProgressPercentage()
                )
                else -> QuizListContent(
                    quizList = quizList,
                    onQuizSelected = { quiz -> quizViewModel.startQuiz(quiz) }
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Caricamento quiz...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun QuizListContent(
    quizList: List<QuizDto>,
    onQuizSelected: (QuizDto) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🌱 Quiz Sostenibilità",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Testa le tue conoscenze sull'ambiente e la sostenibilità",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        if (quizList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Nessun quiz disponibile",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Torna più tardi per nuovi quiz!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        } else {
            items(quizList) { quiz ->
                ElevatedCard(
                    onClick = { onQuizSelected(quiz) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.size(48.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Quiz,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                quiz.titolo,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            if (quiz.descrizione.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    quiz.descrizione,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    maxLines = 2
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${quiz.domande.size} domande",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizQuestionContent(
    quiz: QuizDto,
    currentQuestionIndex: Int,
    selectedAnswers: Map<Int, Int>,
    onAnswerSelected: (Int, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onSubmitQuiz: () -> Unit,
    canProceedToNext: Boolean,
    isLastQuestion: Boolean,
    progressPercentage: Float
) {
    val currentQuestion = quiz.domande.getOrNull(currentQuestionIndex)

    if (currentQuestion == null) return

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Progress bar
        LinearProgressIndicator(
            progress = { progressPercentage },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF4CAF50),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question counter
        Text(
            "Domanda ${currentQuestionIndex + 1} di ${quiz.domande.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    currentQuestion.testo,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Answers
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            currentQuestion.risposte.forEach { risposta ->
                val isSelected = selectedAnswers[currentQuestion.id] == risposta.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = isSelected,
                            onClick = { onAnswerSelected(currentQuestion.id, risposta.id) }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelected) {
                        androidx.compose.foundation.BorderStroke(
                            2.dp,
                            MaterialTheme.colorScheme.primary
                        )
                    } else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onAnswerSelected(currentQuestion.id, risposta.id) }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            risposta.testo,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentQuestionIndex > 0) {
                OutlinedButton(
                    onClick = onPreviousQuestion,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Precedente")
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = if (isLastQuestion) onSubmitQuiz else onNextQuestion,
                enabled = canProceedToNext,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isLastQuestion) "Termina Quiz" else "Avanti")
                if (!isLastQuestion) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun QuizResultContent(
    result: QuizResultDto,
    onRetry: () -> Unit,
    onBackToList: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    result.punteggio >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    result.punteggio >= 60 -> Color(0xFFFF9800).copy(alpha = 0.1f)
                    else -> Color(0xFFF44336).copy(alpha = 0.1f)
                }
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    when {
                        result.punteggio >= 80 -> "🎉 Ottimo!"
                        result.punteggio >= 60 -> "👍 Bene!"
                        else -> "📚 Continua a studiare!"
                    },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "${result.punteggio.toInt()}%",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = when {
                            result.punteggio >= 80 -> Color(0xFF4CAF50)
                            result.punteggio >= 60 -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "${result.risposte_corrette} risposte corrette su ${result.total_domande}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Riprova")
                    }

                    Button(
                        onClick = onBackToList,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Altri Quiz")
                    }
                }
            }
        }
    }
}