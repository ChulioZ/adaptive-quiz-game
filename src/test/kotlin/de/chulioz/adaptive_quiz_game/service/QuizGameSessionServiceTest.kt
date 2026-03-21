package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class QuizGameSessionServiceTest {
    private val collectorService = PlayerCollectorService()
    private val starterService = QuizGameSessionService(collectorService)

    @Test
    fun `fails to start quiz round when not all participants have joined`() {
        collectorService.openRegistration(expectedPlayerCount = 2)
        collectorService.join(Player(age = 7, name = "Ella"))

        assertFailsWith<IllegalStateException> {
            starterService.startRoundWhenReady()
        }
    }

    @Test
    fun `starts quiz round once all participants have joined`() {
        collectorService.openRegistration(expectedPlayerCount = 2)
        collectorService.join(Player(age = 7, name = "Ella"))
        collectorService.join(Player(age = 8, name = "Finn"))

        val round = starterService.startRoundWhenReady()

        assertEquals(2, round.players.size)
        assertTrue(round.turns.isEmpty())
        assertEquals(listOf(0, 0), round.scoresheet.map { it.score })
        assertEquals(round.players, round.scoresheet.map { it.player })
    }
}
