package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.QuizGameSession
import de.chulioz.adaptive_quiz_game.domain.Score
import de.chulioz.adaptive_quiz_game.domain.Turn
import org.springframework.stereotype.Service

@Service
class QuizGameSessionService(
    private val playerCollectorService: PlayerCollectorService,
) {
    private var quizGameSession: QuizGameSession? = null

    fun startGameSessionWhenReady(desiredNumberOfFullRounds: Int): QuizGameSession {
        val players = playerCollectorService.completedPlayersSnapshot()
            ?: error(
                "Cannot start quiz round yet. Missing participants: " +
                        playerCollectorService.missingPlayersCount(),
            )

        val newQuizGameSession = QuizGameSession(
            players = players,
            scoresheet = players.map { Score(player = it, score = 0) },
            turns = emptyList(),
            desiredNumberOfTurns = desiredNumberOfFullRounds * players.size,
        )

        quizGameSession = newQuizGameSession

        return newQuizGameSession
    }

    fun currentGameSession(): QuizGameSession? = quizGameSession

    fun addTurn(turn: Turn) {
        checkNotNull(quizGameSession) { "No current game session found." }
        quizGameSession = quizGameSession!!.addTurn(turn)
    }

    fun addPoint(player: Player) {
        val currentSession = checkNotNull(quizGameSession) { "No current game session found." }
        check(currentSession.players.contains(player)) { "Player is not part of current game session." }
        quizGameSession = currentSession.addOnePoint(player)
    }
}
