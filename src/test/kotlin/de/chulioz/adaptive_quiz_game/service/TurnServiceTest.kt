package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import de.chulioz.adaptive_quiz_game.domain.Question
import de.chulioz.adaptive_quiz_game.domain.Turn
import kotlin.test.*

class TurnServiceTest {
    private val questionService = QuestionService()
    private val collectorService = PlayerCollectorService()
    private val sessionService = QuizGameSessionService(collectorService)
    private val turnService = TurnService(sessionService, questionService)

    @Test
    fun `next turn fails when there is no current game session`() {
        assertFailsWith<IllegalStateException> {
            turnService.nextTurn()
        }
    }

    @Test
    fun `next turn starts with first player when no previous turns exist`() {
        val players = startSession(
            desiredNumberOfFullRounds = 2,
            Player(age = 8, name = "Ada"),
            Player(age = 9, name = "Ben")
        )

        val nextTurn = turnService.nextTurn()
        assertNotNull(nextTurn)

        assertEquals(players.first(), nextTurn.player)
        assertEquals(1, sessionService.currentGameSession()?.turns?.size)
        assertEquals(nextTurn, sessionService.currentGameSession()?.turns?.first())
    }

    @Test
    fun `next turn rotates to next player when previous turns exist`() {
        val players = startSession(
            desiredNumberOfFullRounds = 2,
            Player(age = 8, name = "Ada"),
            Player(age = 9, name = "Ben")
        )
        sessionService.addTurn(
            Turn(
                player = players.first(),
                question = Question(question = "already-used", answers = emptyList()),
            ),
        )

        val nextTurn = turnService.nextTurn()
        assertNotNull(nextTurn)

        assertEquals(players[1], nextTurn.player)
        assertEquals(2, sessionService.currentGameSession()?.turns?.size)
        assertEquals(nextTurn, sessionService.currentGameSession()?.turns?.last())
    }

    @Test
    fun `next turn wraps around to first player after last player`() {
        val players =
            startSession(
                desiredNumberOfFullRounds = 3,
                Player(age = 8, name = "Ada"),
                Player(age = 9, name = "Ben"),
                Player(age = 10, name = "Chris"),
            )
        sessionService.addTurn(
            Turn(
                player = players[2],
                question = Question(question = "already-used", answers = emptyList()),
            ),
        )

        val nextTurn = turnService.nextTurn()
        assertNotNull(nextTurn)

        assertEquals(players.first(), nextTurn.player)
    }

    @Test
    fun `next turn fails when there are no more questions`() {
        val players =
            startSession(
                desiredNumberOfFullRounds = questionService.questions.size + 1,
                Player(age = 8, name = "Ada"),
                Player(age = 9, name = "Ben"),
            )
        questionService.questions.forEach { question ->
            sessionService.addTurn(Turn(player = players.first(), question = question))
        }

        assertFailsWith<IllegalStateException> {
            turnService.nextTurn()
        }
    }

    @Test
    fun `next turn returns null when game session is finished`() {
        val players =
            startSession(
                desiredNumberOfFullRounds = 1,
                Player(age = 8, name = "Ada"),
                Player(age = 9, name = "Ben"),
            )
        val firstExistingTurn =
            Turn(
                player = players.first(),
                question = Question(question = "already-used", answers = emptyList()),
            )
        sessionService.addTurn(firstExistingTurn)
        val secondExistingTurn =
            Turn(
                player = players[1],
                question = Question(question = "also-already-used", answers = emptyList()),
            )
        sessionService.addTurn(secondExistingTurn)

        val nextTurn = turnService.nextTurn()

        assertNull(nextTurn)
        assertEquals(listOf(firstExistingTurn, secondExistingTurn), sessionService.currentGameSession()?.turns)
    }

    private fun startSession(desiredNumberOfFullRounds: Int = 1, vararg players: Player): List<Player> {
        collectorService.openRegistration(expectedPlayerCount = players.size)
        players.forEach(collectorService::join)
        sessionService.startGameSessionWhenReady(desiredNumberOfFullRounds = desiredNumberOfFullRounds)
        return players.toList()
    }
}
