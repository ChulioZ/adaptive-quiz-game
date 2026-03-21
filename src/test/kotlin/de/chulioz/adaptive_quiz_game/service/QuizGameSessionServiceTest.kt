package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.Question
import de.chulioz.adaptive_quiz_game.domain.Turn
import kotlin.test.*

class QuizGameSessionServiceTest {
    private val collectorService = PlayerCollectorService()
    private val quizGameSessionService = QuizGameSessionService(collectorService)

    @Test
    fun `fails to start quiz round when not all participants have joined`() {
        collectorService.openRegistration(expectedPlayerCount = 2)
        collectorService.join(Player(age = 7, name = "Ella"))

        assertFailsWith<IllegalStateException> {
            quizGameSessionService.startRoundWhenReady()
        }
    }

    @Test
    fun `starts quiz round once all participants have joined`() {
        collectorService.openRegistration(expectedPlayerCount = 2)
        collectorService.join(Player(age = 7, name = "Ella"))
        collectorService.join(Player(age = 8, name = "Finn"))

        val round = quizGameSessionService.startRoundWhenReady()

        assertEquals(2, round.players.size)
        assertTrue(round.turns.isEmpty())
        assertEquals(listOf(0, 0), round.scoresheet.map { it.score })
        assertEquals(round.players, round.scoresheet.map { it.player })
    }

    @Test
    fun `returns null when there is no current game session`() {
        assertNull(quizGameSessionService.currentGameSession())
    }

    @Test
    fun `add turn fails if no current game session exists`() {
        val anyTurn =
            Turn(
                player = Player(age = 7, name = "Ella"),
                question = Question(question = "Q1", answers = emptyList()),
            )

        assertFailsWith<IllegalStateException> {
            quizGameSessionService.addTurn(anyTurn)
        }
    }

    @Test
    fun `adds a turn to the current session`() {
        collectorService.openRegistration(expectedPlayerCount = 2)
        val playerOne = Player(age = 7, name = "Ella")
        val playerTwo = Player(age = 8, name = "Finn")
        collectorService.join(playerOne)
        collectorService.join(playerTwo)
        quizGameSessionService.startRoundWhenReady()

        val firstTurn =
            Turn(
                player = playerOne,
                question = Question(question = "Q1", answers = emptyList()),
            )
        quizGameSessionService.addTurn(firstTurn)

        val currentSession = quizGameSessionService.currentGameSession()
        assertEquals(1, currentSession?.turns?.size)
        assertEquals(firstTurn, currentSession?.turns?.first())
    }
}
