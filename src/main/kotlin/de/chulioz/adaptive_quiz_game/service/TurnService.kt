package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.Turn
import org.springframework.stereotype.Service

enum class TurnEvaluationResult {
    CORRECT,
    INCORRECT,
}

@Service
class TurnService(
    private val quizGameSessionService: QuizGameSessionService,
    private val questionService: QuestionService
) {
    fun nextTurn(): Turn? {
        val currentGameSession = quizGameSessionService.currentGameSession() ?: error("No current game session found.")

        if (currentGameSession.isFinished()) {
            return null
        }

        val nextPlayer = nextPlayer(currentGameSession.players, currentGameSession.turns)
        val nextQuestion = questionService.nextQuestion(nextPlayer, currentGameSession.turns)
            ?: error("No more questions available.")

        val nextTurn = Turn(nextPlayer, nextQuestion)

        quizGameSessionService.addTurn(nextTurn)

        return nextTurn
    }

    fun evaluateAnswer(turn: Turn, answerId: Int): TurnEvaluationResult {
        val currentGameSession = quizGameSessionService.currentGameSession() ?: error("No current game session found.")
        check(currentGameSession.turns.contains(turn)) { "Turn is not part of current game session." }

        val selectedAnswer = turn.question.answers.firstOrNull { it.id == answerId }
            ?: error("Selected answer id does not exist for this turn.")

        return if (selectedAnswer.correct) {
            quizGameSessionService.addPoint(turn.player)
            TurnEvaluationResult.CORRECT
        } else {
            TurnEvaluationResult.INCORRECT
        }
    }

    private fun nextPlayer(players: List<Player>, turns: List<Turn>): Player {
        if (turns.isEmpty()) {
            return players.first()
        }

        val lastTurn = turns.last()
        val lastPerson = lastTurn.player
        val indexOfLastPerson = players.indexOf(lastPerson)

        return if (indexOfLastPerson < players.size - 1) players[indexOfLastPerson + 1] else players.first()
    }
}
