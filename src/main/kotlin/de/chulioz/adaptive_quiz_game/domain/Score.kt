package de.chulioz.adaptive_quiz_game.domain

data class Score(val person: Person, val score: Int) {
    fun addOne() = Score(person, score + 1)
}
