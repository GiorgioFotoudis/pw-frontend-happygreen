package com.example.frontend_happygreen.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontend_happygreen.data.model.QuizDto
import com.example.frontend_happygreen.data.model.QuizResultDto
import com.example.frontend_happygreen.viewmodel.QuizViewModel
import com.example.frontend_happygreen.ui.theme.*

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EcoGreen50,
                        Color.White,
                        EcoGreen50.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top App Bar personalizzata
            CustomTopAppBar(
                title = currentQuiz?.titolo ?: "🧠 Quiz Verde",
                onBackClick = {
                    if (currentQuiz != null) {
                        quizViewModel.resetQuiz()
                    } else {
                        navController.navigateUp()
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

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

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CustomTopAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(EcoGreen100, CircleShape)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Indietro",
                    tint = EcoGreen700
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = EcoGreen800
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(40.dp)) // Per bilanciare
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(40.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = EcoGreen500,
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Caricamento quiz...",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = EcoGreen700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "🌱 Preparando le domande sulla sostenibilità",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = EcoGreen500
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun QuizListContent(
    quizList: List<QuizDto>,
    onQuizSelected: (QuizDto) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    EcoGreen500,
                                    EcoGreen600,
                                    LeafGreen
                                )
                            )
                        )
                        .padding(28.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "🧠",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Quiz Sostenibilità",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Testa le tue conoscenze sull'ambiente e scopri quanto sei eco-friendly!",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (quizList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(EcoGreen100, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "🌱",
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Nessun quiz disponibile",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EcoGreen800
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Torna più tardi per nuovi quiz sulla sostenibilità!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = EcoGreen600
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(quizList) { quiz ->
                QuizCard(
                    quiz = quiz,
                    onClick = { onQuizSelected(quiz) }
                )
            }
        }
    }
}

@Composable
private fun QuizCard(
    quiz: QuizDto,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    ElevatedCard(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icona del quiz
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(EcoGreen400, EcoGreen600)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Quiz,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Contenuto del quiz
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    quiz.titolo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen800
                    )
                )
                if (quiz.descrizione.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        quiz.descrizione,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = EcoGreen600
                        ),
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Help,
                        contentDescription = null,
                        tint = EcoGreen500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${quiz.domande.size} domande",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = EcoGreen500,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Freccia
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(EcoGreen100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = EcoGreen600,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
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
    val currentQuestion = quiz.domande.getOrNull(currentQuestionIndex) ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Progress Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Domanda ${currentQuestionIndex + 1}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EcoGreen800
                        )
                    )
                    Text(
                        "${(progressPercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EcoGreen600
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress bar personalizzata
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(EcoGreen100, RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progressPercentage)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(EcoGreen500, LeafGreen)
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "di ${quiz.domande.size} domande totali",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = EcoGreen600
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                EcoGreen500,
                                EcoGreen600
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "❓",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Domanda",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        currentQuestion.testo,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = MaterialTheme.typography.headlineSmall.lineHeight * 1.2
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Answers Section
        Text(
            "Seleziona la risposta corretta:",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EcoGreen800
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Answer Options
        currentQuestion.risposte.forEachIndexed { index, risposta ->
            val isSelected = selectedAnswers[currentQuestion.id] == risposta.id
            val letterLabel = ('A' + index).toString()

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300, delayMillis = index * 100)
                ) + fadeIn(animationSpec = tween(300, delayMillis = index * 100))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .selectable(
                            selected = isSelected,
                            onClick = { onAnswerSelected(currentQuestion.id, risposta.id) }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            EcoGreen500.copy(alpha = 0.1f)
                        } else {
                            Color.White
                        }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = if (isSelected) {
                        androidx.compose.foundation.BorderStroke(2.dp, EcoGreen500)
                    } else {
                        androidx.compose.foundation.BorderStroke(1.dp, EcoGreen200)
                    },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 8.dp else 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Letter Label
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (isSelected) EcoGreen500 else EcoGreen100,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                letterLabel,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else EcoGreen700
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Answer Text
                        Text(
                            risposta.testo,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (isSelected) EcoGreen800 else EcoGreen700,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Selection Indicator
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EcoGreen500,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            if (index < currentQuestion.risposte.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (currentQuestionIndex > 0) {
                OutlinedButton(
                    onClick = onPreviousQuestion,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = EcoGreen600
                    ),
                    border = androidx.compose.foundation.BorderStroke(2.dp, EcoGreen600),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Precedente",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Button(
                onClick = if (isLastQuestion) onSubmitQuiz else onNextQuestion,
                enabled = canProceedToNext,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EcoGreen500,
                    disabledContainerColor = EcoGreen300
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    if (isLastQuestion) "Termina Quiz" else "Avanti",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                if (!isLastQuestion) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun QuizResultContent(
    result: QuizResultDto,
    onRetry: () -> Unit,
    onBackToList: () -> Unit
) {
    val resultColor = when {
        result.punteggio >= 80 -> SuccessGreen
        result.punteggio >= 60 -> SunYellow
        else -> ErrorRed
    }

    val resultEmoji = when {
        result.punteggio >= 80 -> "🎉"
        result.punteggio >= 60 -> "👍"
        else -> "📚"
    }

    val resultTitle = when {
        result.punteggio >= 80 -> "Fantastico!"
        result.punteggio >= 60 -> "Bene!"
        else -> "Continua a studiare!"
    }

    val resultMessage = when {
        result.punteggio >= 80 -> "Sei un vero esperto di sostenibilità! 🌱"
        result.punteggio >= 60 -> "Buona conoscenza, ma puoi migliorare ancora! 🌿"
        else -> "Non mollare, continua a imparare sulla sostenibilità! 📖"
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Result Icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            resultColor.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        resultEmoji,
                        style = MaterialTheme.typography.displayMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    resultTitle,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen800
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    resultMessage,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = EcoGreen600
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Score Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = resultColor.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "${result.punteggio.toInt()}%",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = resultColor
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "${result.risposte_corrette} risposte corrette su ${result.total_domande}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = EcoGreen700
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackToList,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = EcoGreen600
                        ),
                        border = BorderStroke(2.dp, EcoGreen600),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.List, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Tutti i quiz",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Button(
                        onClick = onRetry,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen500
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Riprova",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}