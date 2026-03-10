package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Person
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class QuizRoundStarterServiceTest {
    private val collectorService = QuizParticipantCollectorService()
    private val starterService = QuizRoundStarterService(collectorService)

    @Test
    fun `fails to start quiz round when not all participants have joined`() {
        collectorService.openRegistration(expectedParticipants = 2)
        collectorService.join(Person(age = 7, name = "Ella"))

        assertFailsWith<IllegalStateException> {
            starterService.startRoundWhenReady()
        }
    }

    @Test
    fun `starts quiz round once all participants have joined`() {
        collectorService.openRegistration(expectedParticipants = 2)
        collectorService.join(Person(age = 7, name = "Ella"))
        collectorService.join(Person(age = 8, name = "Finn"))

        val round = starterService.startRoundWhenReady()

        assertEquals(2, round.persons.size)
        assertTrue(round.turns.isEmpty())
        assertEquals(listOf(0, 0), round.scoresheet.map { it.score })
        assertEquals(round.persons, round.scoresheet.map { it.person })
    }
}
