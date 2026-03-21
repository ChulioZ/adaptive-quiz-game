package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.QuizGameSession
import de.chulioz.adaptive_quiz_game.domain.Score
import org.springframework.stereotype.Service

@Service
class QuizGameSessionService(
    private val participantCollectorService: PlayerCollectorService,
) {
    private var quizGameSession: QuizGameSession? = null

    fun startRoundWhenReady(): QuizGameSession {
        val participants = participantCollectorService.completedPlayersSnapshot()
            ?: error(
                "Cannot start quiz round yet. Missing participants: " +
                        participantCollectorService.missingPlayersCount(),
            )

        val newQuizGameSession = QuizGameSession(
            players = participants,
            scoresheet = participants.map { Score(player = it, score = 0) },
            turns = emptyList(),
        )

        quizGameSession = newQuizGameSession

        return newQuizGameSession
    }

    fun currentRound(): QuizGameSession? = quizGameSession
}
