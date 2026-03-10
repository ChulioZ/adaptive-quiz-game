package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.QuizRound
import de.chulioz.adaptive_quiz_game.domain.Score
import org.springframework.stereotype.Service

@Service
class QuizRoundStarterService(
    private val participantCollectorService: QuizParticipantCollectorService,
) {
    fun startRoundWhenReady(): QuizRound {
        val participants = participantCollectorService.completedParticipantsSnapshot()
            ?: error(
                "Cannot start quiz round yet. Missing participants: " +
                        participantCollectorService.missingParticipantsCount(),
            )

        return QuizRound(
            persons = participants,
            scoresheet = participants.map { Score(person = it, score = 0) },
            turns = emptyList(),
        )
    }
}
