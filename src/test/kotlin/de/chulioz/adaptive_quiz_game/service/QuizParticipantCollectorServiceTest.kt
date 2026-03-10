package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Person
import kotlin.test.*

class QuizParticipantCollectorServiceTest {
    private val service = QuizParticipantCollectorService()

    @Test
    fun `collects participants until all are joined`() {
        service.openRegistration(expectedParticipants = 2)

        service.join(Person(age = 11, name = "Ada"))
        assertFalse(service.hasAllJoined())
        assertEquals(1, service.missingParticipantsCount())
        assertNull(service.completedParticipantsSnapshot())

        service.join(Person(age = 12, name = "Ben"))
        assertTrue(service.hasAllJoined())
        assertEquals(0, service.missingParticipantsCount())
        assertEquals(2, service.currentParticipants().size)
        assertEquals(
            listOf(Person(age = 11, name = "Ada"), Person(age = 12, name = "Ben")),
            service.completedParticipantsSnapshot()
        )
    }

    @Test
    fun `opening registration resets previous participants`() {
        service.openRegistration(expectedParticipants = 2)
        service.join(Person(age = 10, name = "Chris"))
        service.join(Person(age = 7, name = "Flora"))
        assertEquals(2, service.currentParticipants().size)

        service.openRegistration(expectedParticipants = 2)
        assertEquals(0, service.currentParticipants().size)
        assertEquals(2, service.missingParticipantsCount())
    }

    @Test
    fun `joining before registration is opened fails`() {
        assertFailsWith<IllegalStateException> {
            service.join(Person(age = 9, name = "Dana"))
        }
    }
}
