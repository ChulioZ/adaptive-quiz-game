package de.chulioz.adaptive_quiz_game.domain

data class QuizGameSession(
    val players: List<Player>,
    val scoresheet: List<Score>,
    val turns: List<Turn>,
    val desiredNumberOfTurns: Int
) {
    fun addTurn(turn: Turn): QuizGameSession {
        return copy(turns = turns + turn)
    }

    fun isFinished(): Boolean {
        return turns.size >= desiredNumberOfTurns
    }
}
