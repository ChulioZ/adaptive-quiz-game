package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Answer
import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.Question
import de.chulioz.adaptive_quiz_game.domain.Turn
import org.springframework.stereotype.Service

@Service
class QuestionService {
    val questions: List<Question> =
        listOf(
            Question(
                question = "Was ist die Hauptstadt von Frankreich?",
                answers =
                    listOf(
                        Answer(answer = "Berlin", id = 1, correct = false),
                        Answer(answer = "Paris", id = 2, correct = true),
                        Answer(answer = "Madrid", id = 3, correct = false),
                        Answer(answer = "Rom", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welcher Planet ist als der Rote Planet bekannt?",
                answers =
                    listOf(
                        Answer(answer = "Mars", id = 1, correct = true),
                        Answer(answer = "Venus", id = 2, correct = false),
                        Answer(answer = "Jupiter", id = 3, correct = false),
                        Answer(answer = "Saturn", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welcher ist der größte Ozean der Erde?",
                answers =
                    listOf(
                        Answer(answer = "Atlantischer Ozean", id = 1, correct = false),
                        Answer(answer = "Indischer Ozean", id = 2, correct = false),
                        Answer(answer = "Pazifischer Ozean", id = 3, correct = true),
                        Answer(answer = "Arktischer Ozean", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Wer schrieb 'Romeo und Julia'?",
                answers =
                    listOf(
                        Answer(answer = "Charles Dickens", id = 1, correct = false),
                        Answer(answer = "Jane Austen", id = 2, correct = false),
                        Answer(answer = "Mark Twain", id = 3, correct = false),
                        Answer(answer = "William Shakespeare", id = 4, correct = true)
                    ),
            ),
            Question(
                question = "Was ist das chemische Symbol für Gold?",
                answers =
                    listOf(
                        Answer(answer = "Au", id = 1, correct = true),
                        Answer(answer = "Ag", id = 2, correct = false),
                        Answer(answer = "Gd", id = 3, correct = false),
                        Answer(answer = "Go", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Wie viele Kontinente gibt es?",
                answers =
                    listOf(
                        Answer(answer = "5", id = 1, correct = false),
                        Answer(answer = "7", id = 2, correct = true),
                        Answer(answer = "6", id = 3, correct = false),
                        Answer(answer = "8", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Wie hoch ist der Siedepunkt von Wasser auf Meereshöhe in Celsius?",
                answers =
                    listOf(
                        Answer(answer = "90", id = 1, correct = false),
                        Answer(answer = "80", id = 2, correct = false),
                        Answer(answer = "100", id = 3, correct = true),
                        Answer(answer = "120", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welches Land ist für das Ahornblatt-Symbol bekannt?",
                answers =
                    listOf(
                        Answer(answer = "Norwegen", id = 1, correct = false),
                        Answer(answer = "Schweden", id = 2, correct = false),
                        Answer(answer = "Kanada", id = 3, correct = true),
                        Answer(answer = "Finnland", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welches Gas nehmen Pflanzen aus der Atmosphäre auf?",
                answers =
                    listOf(
                        Answer(answer = "Kohlendioxid", id = 1, correct = true),
                        Answer(answer = "Sauerstoff", id = 2, correct = false),
                        Answer(answer = "Stickstoff", id = 3, correct = false),
                        Answer(answer = "Wasserstoff", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Was ist die Quadratwurzel von 81?",
                answers =
                    listOf(
                        Answer(answer = "8", id = 1, correct = false),
                        Answer(answer = "7", id = 2, correct = false),
                        Answer(answer = "6", id = 3, correct = false),
                        Answer(answer = "9", id = 4, correct = true)
                    ),
            ),
            Question(
                question = "Wofür steht CPU in der Informatik?",
                answers =
                    listOf(
                        Answer(answer = "Central Processing Unit", id = 1, correct = true),
                        Answer(answer = "Computer Power Unit", id = 2, correct = false),
                        Answer(answer = "Central Program Utility", id = 3, correct = false),
                        Answer(answer = "Control Processing User", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welcher Kontinent ist der größte nach Fläche?",
                answers =
                    listOf(
                        Answer(answer = "Afrika", id = 1, correct = false),
                        Answer(answer = "Europa", id = 2, correct = false),
                        Answer(answer = "Asien", id = 3, correct = true),
                        Answer(answer = "Nordamerika", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "In welchem Jahr fiel die Berliner Mauer?",
                answers =
                    listOf(
                        Answer(answer = "1987", id = 1, correct = false),
                        Answer(answer = "1989", id = 2, correct = true),
                        Answer(answer = "1991", id = 3, correct = false),
                        Answer(answer = "1993", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welches Element hat die Ordnungszahl 1?",
                answers =
                    listOf(
                        Answer(answer = "Helium", id = 1, correct = false),
                        Answer(answer = "Wasserstoff", id = 2, correct = true),
                        Answer(answer = "Sauerstoff", id = 3, correct = false),
                        Answer(answer = "Kohlenstoff", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welcher ist der längste Fluss der Welt?",
                answers =
                    listOf(
                        Answer(answer = "Amazonas", id = 1, correct = false),
                        Answer(answer = "Nil", id = 2, correct = true),
                        Answer(answer = "Mississippi", id = 3, correct = false),
                        Answer(answer = "Donau", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welche Programmiersprache läuft typischerweise in der JVM?",
                answers =
                    listOf(
                        Answer(answer = "Kotlin", id = 1, correct = true),
                        Answer(answer = "C", id = 2, correct = false),
                        Answer(answer = "Rust", id = 3, correct = false),
                        Answer(answer = "Go", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Wie viele Minuten hat eine Stunde?",
                answers =
                    listOf(
                        Answer(answer = "45", id = 1, correct = false),
                        Answer(answer = "50", id = 2, correct = false),
                        Answer(answer = "60", id = 3, correct = true),
                        Answer(answer = "90", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welches Organ pumpt Blut durch den menschlichen Körper?",
                answers =
                    listOf(
                        Answer(answer = "Lunge", id = 1, correct = false),
                        Answer(answer = "Leber", id = 2, correct = false),
                        Answer(answer = "Herz", id = 3, correct = true),
                        Answer(answer = "Niere", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welche Farbe entsteht aus Blau und Gelb?",
                answers =
                    listOf(
                        Answer(answer = "Lila", id = 1, correct = false),
                        Answer(answer = "Orange", id = 2, correct = false),
                        Answer(answer = "Grün", id = 3, correct = true),
                        Answer(answer = "Rot", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welches ist das kleinste Primzahl?",
                answers =
                    listOf(
                        Answer(answer = "1", id = 1, correct = false),
                        Answer(answer = "2", id = 2, correct = true),
                        Answer(answer = "3", id = 3, correct = false),
                        Answer(answer = "5", id = 4, correct = false),
                    ),
            ),
            Question(
                question = "Welche Sprache wird hauptsächlich in Brasilien gesprochen?",
                answers =
                    listOf(
                        Answer(answer = "Spanisch", id = 1, correct = false),
                        Answer(answer = "Portugiesisch", id = 2, correct = true),
                        Answer(answer = "Französisch", id = 3, correct = false),
                        Answer(answer = "Italienisch", id = 4, correct = false),
                    ),
            ),
        )

    fun nextQuestion(player: Player, previousTurns: List<Turn>): Question? {
        return questions.filter { it.question !in previousTurns.map { it.question.question } }.randomOrNull()
    }
}
