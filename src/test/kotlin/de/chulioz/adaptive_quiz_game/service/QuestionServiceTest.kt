package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.Turn
import kotlin.test.*

class QuestionServiceTest {
    private val service = QuestionService()
    private val player = Player(age = 10, name = "Ada")

    @Test
    fun `returns a question when no turns were played yet`() {
        val nextQuestion = service.nextQuestion(player, previousTurns = emptyList())

        assertNotNull(nextQuestion)
        assertTrue(nextQuestion in service.questions)
    }

    @Test
    fun `returns the only remaining question not present in previous turns`() {
        val remainingQuestion = service.questions.first()
        val previousTurns =
            service.questions
                .drop(1)
                .map { Turn(player = player, question = it) }

        val nextQuestion = service.nextQuestion(player, previousTurns)

        assertEquals(remainingQuestion, nextQuestion)
    }

    @Test
    fun `returns null when all questions were already asked`() {
        val previousTurns = service.questions.map { Turn(player = player, question = it) }

        val nextQuestion = service.nextQuestion(player, previousTurns)

        assertNull(nextQuestion)
    }
}
