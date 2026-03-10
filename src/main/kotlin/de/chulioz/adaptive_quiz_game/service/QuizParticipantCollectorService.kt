package de.chulioz.adaptive_quiz_game.service

import de.chulioz.adaptive_quiz_game.domain.Person
import org.springframework.stereotype.Service

@Service
class QuizParticipantCollectorService {
    private val participants = linkedSetOf<Person>()
    private var expectedParticipants: Int? = null

    fun openRegistration(expectedParticipants: Int) {
        require(expectedParticipants > 1) { "Expected participants must be greater than 1." }
        this.expectedParticipants = expectedParticipants
        participants.clear()
    }

    fun join(person: Person) {
        check(expectedParticipants != null) {
            "Registration must be opened before persons can join."
        }
        check(participants.size < expectedParticipants!!) {
            "Maximum number of participants reached."
        }
        participants += person
    }

    fun currentParticipants(): List<Person> = participants.toList()

    fun missingParticipantsCount(): Int {
        val expected = expectedParticipants ?: return 0
        return (expected - participants.size).coerceAtLeast(0)
    }

    fun hasAllJoined(): Boolean {
        val expected = expectedParticipants ?: return false
        return participants.size >= expected
    }

    fun completedParticipantsSnapshot(): List<Person>? {
        if (!hasAllJoined()) {
            return null
        }
        return participants.toList()
    }
}
