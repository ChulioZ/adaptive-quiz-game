package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Player
import org.springframework.stereotype.Service

@Service
class PlayerCollectorService {
    private val players = linkedSetOf<Player>()
    private var expectedPlayerCount: Int? = null

    fun openRegistration(expectedPlayerCount: Int) {
        require(expectedPlayerCount > 1) { "Expected player count must be greater than 1." }
        this.expectedPlayerCount = expectedPlayerCount
        players.clear()
    }

    fun join(player: Player) {
        check(expectedPlayerCount != null) {
            "Registration must be opened before players can join."
        }
        check(players.size < expectedPlayerCount!!) {
            "Maximum number of players reached."
        }
        players += player
    }

    fun currentPlayers(): List<Player> = players.toList()

    fun missingPlayersCount(): Int {
        val expected = expectedPlayerCount ?: return 0
        return (expected - players.size).coerceAtLeast(0)
    }

    fun hasAllJoined(): Boolean {
        val expected = expectedPlayerCount ?: return false
        return players.size >= expected
    }

    fun completedPlayersSnapshot(): List<Player>? {
        if (!hasAllJoined()) {
            return null
        }
        return players.toList()
    }
}
