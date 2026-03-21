package de.chulioz.adaptive_quiz_game.domain

data class QuizGameSession(val players: List<Player>, val scoresheet: List<Score>, val turns: List<Turn>)
