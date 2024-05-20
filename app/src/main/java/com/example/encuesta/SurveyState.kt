package com.example.encuesta

data class SurveyState(
    val acceptedTerms: Boolean = false,
    val answers: List<String> = List(5) { "" }
) {
    val questions = listOf(
        "¿Cree que su universidad es la mejor de Managua?",
        "¿Cree que la calidad de su universidad es la mejor?",
        "¿Cree que las clases que se imparten le ayudarán a su vida como profesional?",
        "¿Cree que la clase de Programación Orientada a Objetos le contribuirá a su futuro profesional?",
        "¿Considera que hacer la tarea con carácter individual y honesto, le ayudarán en el futuro profesional?"
    )

    fun areAllQuestionsAnswered(): Boolean {
        return acceptedTerms && answers.none { it.isBlank() }
    }
}

