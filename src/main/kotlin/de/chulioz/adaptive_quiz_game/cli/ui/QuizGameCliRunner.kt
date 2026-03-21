package de.chulioz.adaptive_quiz_game.cli.ui

import de.chulioz.adaptive_quiz_game.cli.backend.QuizGameBackendFacade
import de.chulioz.adaptive_quiz_game.domain.Score
import de.chulioz.adaptive_quiz_game.service.TurnEvaluationResult
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.text.MessageFormat
import java.util.*

@Component
@ConditionalOnProperty(name = ["quiz.cli.mode"], havingValue = "interactive")
class QuizGameCliRunner(
    private val backend: QuizGameBackendFacade,
) : CommandLineRunner {
    private val locale: Locale = when (Locale.getDefault().language.lowercase(Locale.ROOT)) {
        Locale.GERMAN.language -> Locale.GERMAN
        Locale.ENGLISH.language -> Locale.ENGLISH
        else -> Locale.ENGLISH
    }
    private val messages: ResourceBundle = ResourceBundle.getBundle("i18n.cli", locale)

    override fun run(vararg args: String) {
        val reader = System.`in`.bufferedReader()
        println(message("app.title"))
        println(message("app.separator"))

        val playerCount = askInt(
            reader = reader,
            prompt = message("prompt.player_count"),
            validator = { it > 1 },
            validatorMessage = message("validation.player_count"),
        )

        backend.openRegistration(playerCount)

        repeat(playerCount) { index ->
            val playerIndex = index + 1
            val name = askNonBlank(reader, message("prompt.player_name", playerIndex))
            val age = askInt(
                reader = reader,
                prompt = message("prompt.player_age", playerIndex),
                validator = { it > 0 },
                validatorMessage = message("validation.player_age"),
            )
            backend.joinPlayer(name = name, age = age)
        }

        val rounds = askInt(
            reader = reader,
            prompt = message("prompt.rounds"),
            validator = { it > 0 },
            validatorMessage = message("validation.rounds"),
        )

        val session = backend.startSession(rounds)

        println()
        println(message("game.start", session.players.size, rounds))

        while (true) {
            val nextTurn = try {
                backend.nextTurn()
            } catch (e: IllegalStateException) {
                println()
                println(message("game.stopped", e.message.orEmpty()))
                break
            }

            if (nextTurn == null) {
                println()
                println(message("game.finished"))
                break
            }

            println()
            println(message("turn.for_player", nextTurn.player.name))
            println(nextTurn.question.question)
            nextTurn.question.answers.forEach { answer ->
                println("${answer.id}. ${answer.answer}")
            }

            val selectedAnswerId = askInt(
                reader = reader,
                prompt = message("prompt.answer_id"),
                validator = { input -> nextTurn.question.answers.any { it.id == input } },
                validatorMessage = message("validation.answer_id"),
            )

            when (backend.evaluateAnswer(nextTurn, selectedAnswerId)) {
                TurnEvaluationResult.CORRECT -> println(message("answer.correct"))
                TurnEvaluationResult.INCORRECT -> {
                    val correctAnswer = nextTurn.question.answers.firstOrNull { it.correct }
                    println(message("answer.incorrect", correctAnswer?.id, correctAnswer?.answer.orEmpty()))
                }
            }
            println()
            printScore(backend.currentScoresheet())
        }

        printScore(backend.currentScoresheet())
    }

    private fun printScore(scoresheet: List<Score>) {
        println()
        println(message("score.title"))
        scoresheet
            .sortedByDescending { it.score }
            .forEach { score ->
                println("- ${score.player.name} (${score.player.age}): ${score.score}")
            }
    }

    private fun askNonBlank(reader: java.io.BufferedReader, prompt: String): String {
        while (true) {
            print(prompt)
            val rawInput = reader.readLine()?.trim().orEmpty()
            if (rawInput.isNotBlank()) {
                return rawInput
            }
            println(message("validation.non_blank"))
        }
    }

    private fun askInt(
        reader: java.io.BufferedReader,
        prompt: String,
        validator: (Int) -> Boolean,
        validatorMessage: String,
    ): Int {
        while (true) {
            print(prompt)
            val value = reader.readLine()?.trim()?.toIntOrNull()
            if (value == null) {
                println(message("validation.integer"))
                continue
            }
            if (!validator(value)) {
                println(validatorMessage)
                continue
            }
            return value
        }
    }

    private fun message(key: String, vararg args: Any?): String {
        val pattern = messages.getString(key)
        return MessageFormat(pattern, locale).format(args)
    }
}
