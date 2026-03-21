package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import kotlin.test.*

class PlayerCollectorServiceTest {
    private val service = PlayerCollectorService()

    @Test
    fun `collects participants until all are joined`() {
        service.openRegistration(expectedPlayerCount = 2)

        service.join(Player(age = 11, name = "Ada"))
        assertFalse(service.hasAllJoined())
        assertEquals(1, service.missingPlayersCount())
        assertNull(service.completedPlayersSnapshot())

        service.join(Player(age = 12, name = "Ben"))
        assertTrue(service.hasAllJoined())
        assertEquals(0, service.missingPlayersCount())
        assertEquals(2, service.currentPlayers().size)
        assertEquals(
            listOf(Player(age = 11, name = "Ada"), Player(age = 12, name = "Ben")),
            service.completedPlayersSnapshot()
        )
    }

    @Test
    fun `opening registration resets previous participants`() {
        service.openRegistration(expectedPlayerCount = 2)
        service.join(Player(age = 10, name = "Chris"))
        service.join(Player(age = 7, name = "Flora"))
        assertEquals(2, service.currentPlayers().size)

        service.openRegistration(expectedPlayerCount = 2)
        assertEquals(0, service.currentPlayers().size)
        assertEquals(2, service.missingPlayersCount())
    }

    @Test
    fun `joining before registration is opened fails`() {
        assertFailsWith<IllegalStateException> {
            service.join(Player(age = 9, name = "Dana"))
        }
    }
}
