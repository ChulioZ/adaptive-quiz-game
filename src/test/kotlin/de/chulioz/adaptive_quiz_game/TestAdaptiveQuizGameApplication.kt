package de.chulioz.adaptive_quiz_game

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AdaptiveQuizGameApplication>().with(TestcontainersConfiguration::class).run(*args)
}
