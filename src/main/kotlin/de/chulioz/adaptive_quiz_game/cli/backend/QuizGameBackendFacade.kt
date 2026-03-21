package de.chulioz.adaptive_quiz_game.cli.backend

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.QuizGameSession
import de.chulioz.adaptive_quiz_game.domain.Score
import de.chulioz.adaptive_quiz_game.domain.Turn
import de.chulioz.adaptive_quiz_game.service.PlayerCollectorService
import de.chulioz.adaptive_quiz_game.service.QuizGameSessionService
import de.chulioz.adaptive_quiz_game.service.TurnEvaluationResult
import de.chulioz.adaptive_quiz_game.service.TurnService
import org.springframework.stereotype.Component

@Component
class QuizGameBackendFacade(
    private val playerCollectorService: PlayerCollectorService,
    private val quizGameSessionService: QuizGameSessionService,
    private val turnService: TurnService,
) {
    fun openRegistration(expectedPlayerCount: Int) {
        playerCollectorService.openRegistration(expectedPlayerCount)
    }

    fun joinPlayer(name: String, age: Int) {
        playerCollectorService.join(Player(age = age, name = name))
    }

    fun startSession(desiredNumberOfFullRounds: Int): QuizGameSession {
        return quizGameSessionService.startGameSessionWhenReady(desiredNumberOfFullRounds)
    }

    fun nextTurn(): Turn? = turnService.nextTurn()

    fun evaluateAnswer(turn: Turn, answerId: Int): TurnEvaluationResult {
        return turnService.evaluateAnswer(turn, answerId)
    }

    fun currentScoresheet(): List<Score> {
        return quizGameSessionService.currentGameSession()?.scoresheet
            ?: error("No current game session found.")
    }
}
