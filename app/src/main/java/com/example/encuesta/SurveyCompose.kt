package com.example.encuesta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SurveyApp() {
    val surveyState = remember { mutableStateOf(SurveyState()) }
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Encuesta de Aceptación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CheckboxWithLabel(
                label = "Acepta los términos",
                isChecked = surveyState.value.acceptedTerms,
                onCheckedChange = { isChecked ->
                    surveyState.value = surveyState.value.copy(
                        acceptedTerms = isChecked,
                        answers = if (!isChecked) List(5) { "" } else surveyState.value.answers
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(surveyState.value.questions.size) { index ->
            val question = surveyState.value.questions[index]
            Question(
                question = question,
                answer = surveyState.value.answers[index],
                enabled = surveyState.value.acceptedTerms,
                onAnswerChange = { answer ->
                    val newAnswers = surveyState.value.answers.toMutableList()
                    newAnswers[index] = answer
                    surveyState.value = surveyState.value.copy(answers = newAnswers)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Button(onClick = {
                if (surveyState.value.areAllQuestionsAnswered()) {
                    println("Enviando respuestas: ${surveyState.value.answers}")
                } else {
                    println("Por favor responda todas las preguntas.")
                }
            }) {
                Text("Enviar")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CheckboxWithLabel(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Red
            )
        )
        Text(text = label)
    }
}

@Composable
fun Question(
    question: String,
    answer: String,
    enabled: Boolean,
    onAnswerChange: (String) -> Unit
) {
    val options = listOf(
        "Opción 1: Muy en desacuerdo",
        "Opción 2: En desacuerdo",
        "Opción 3: Neutral",
        "Opción 4: De acuerdo",
        "Opción 5: Muy de acuerdo",
        "Otro"
    )

    var otherText by remember { mutableStateOf(if (answer.startsWith("Otro: ")) answer.removePrefix("Otro: ") else "") }
    val isOtherSelected = answer.startsWith("Otro")

    Column {
        Text(text = question)
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = answer == option || (isOtherSelected && option == "Otro"),
                    onClick = {
                        if (option == "Otro") {
                            onAnswerChange("Otro: $otherText")
                        } else {
                            onAnswerChange(option)
                        }
                    },
                    enabled = enabled
                )
                Text(text = option)
            }
        }
        if (isOtherSelected && enabled) {
            TextField(
                value = otherText,
                onValueChange = {
                    otherText = it
                    onAnswerChange("Otro: $it")
                },
                enabled = enabled,
                placeholder = { Text("Especifique aquí") }
            )
        }
    }
}
