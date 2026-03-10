package de.chulioz.adaptive_quiz_game.domain

data class QuizRound(val persons: List<Person>, val scoresheet: List<Score>, val turns: List<Turn>)
