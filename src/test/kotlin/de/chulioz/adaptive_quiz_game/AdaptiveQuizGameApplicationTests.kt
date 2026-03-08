package de.chulioz.adaptive_quiz_game

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class AdaptiveQuizGameApplicationTests {

	@Test
	fun contextLoads() {
	}

}
