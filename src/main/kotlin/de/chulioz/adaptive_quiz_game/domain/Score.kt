package de.chulioz.adaptive_quiz_game.domain

data class Score(val player: Player, val score: Int) {
    fun addOne() = Score(player, score + 1)
}
