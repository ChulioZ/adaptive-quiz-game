package de.chulioz.adaptive_quiz_game.cli.ui

import java.text.MessageFormat
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CliMessageFormattingTest {
    @Test
    fun `game start placeholders are formatted for english and german`() {
        val englishMessage = formatMessage(Locale.ENGLISH, "game.start", 3, 5)
        val germanMessage = formatMessage(Locale.GERMAN, "game.start", 3, 5)

        assertTrue(englishMessage.contains("3"))
        assertTrue(englishMessage.contains("5"))
        assertFalse(englishMessage.contains("{0}"))
        assertFalse(englishMessage.contains("{1}"))

        assertTrue(germanMessage.contains("3"))
        assertTrue(germanMessage.contains("5"))
        assertFalse(germanMessage.contains("{0}"))
        assertFalse(germanMessage.contains("{1}"))
    }

    private fun formatMessage(locale: Locale, key: String, vararg args: Any): String {
        val bundle = ResourceBundle.getBundle("i18n.cli", locale)
        return MessageFormat(bundle.getString(key), locale).format(args)
    }
}
