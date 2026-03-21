package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.Turn
import org.springframework.stereotype.Service

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