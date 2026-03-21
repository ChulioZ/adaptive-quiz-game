package de.chulioz.adaptive_quiz_game.cli.ui

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
@ConditionalOnProperty(name = ["quiz.cli.autostart"], havingValue = "true", matchIfMissing = true)
class CliTerminalAutoLauncher(
    private val environment: Environment,
) {
    private val logger = LoggerFactory.getLogger(CliTerminalAutoLauncher::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun launchCliInNewTerminal() {
        if (environment.getProperty("quiz.cli.mode") == "interactive") {
            return
        }

        val workingDirectory = Paths.get("").toAbsolutePath().toString()
        val command = buildCliCommand(workingDirectory)
        val os = System.getProperty("os.name").lowercase()

        val started = when {
            os.contains("mac") -> startMacTerminal(command)
            os.contains("win") -> startWindowsTerminal(command)
            else -> startLinuxTerminal(command)
        }

        if (!started) {
            logger.warn("Could not open terminal for CLI auto-start. Run CLI manually with: {}", command)
        }
    }

    private fun buildCliCommand(workingDirectory: String): String {
        val javaBin = Paths.get(System.getProperty("java.home"), "bin", "java").toString()
        val javaLaunchPart = buildJavaLaunchPart()
        val cliArgs = listOf(
            "--quiz.cli.mode=interactive",
            "--quiz.cli.autostart=false",
            "--spring.main.web-application-type=none",
        ).joinToString(" ") { shellQuote(it) }

        return "cd ${shellQuote(workingDirectory)} && ${shellQuote(javaBin)} $javaLaunchPart $cliArgs"
    }

    private fun buildJavaLaunchPart(): String {
        val javaCommand = System.getProperty("sun.java.command")
            ?: throw IllegalStateException("Missing sun.java.command system property.")
        val commandTokens = splitCommand(javaCommand)
        require(commandTokens.isNotEmpty()) { "sun.java.command must not be empty." }

        return if (commandTokens.first().endsWith(".jar")) {
            "-jar ${shellQuote(commandTokens.first())} ${commandTokens.drop(1).joinToString(" ") { shellQuote(it) }}"
                .trim()
        } else {
            val classpath = System.getProperty("java.class.path")
                ?: throw IllegalStateException("Missing java.class.path system property.")
            "-cp ${shellQuote(classpath)} ${commandTokens.joinToString(" ") { shellQuote(it) }}"
        }
    }

    private fun splitCommand(command: String): List<String> {
        val tokens = mutableListOf<String>()
        val current = StringBuilder()
        var inQuote = false
        var quoteChar = '\u0000'

        for (char in command) {
            when {
                inQuote && char == quoteChar -> {
                    inQuote = false
                }

                !inQuote && (char == '\'' || char == '"') -> {
                    inQuote = true
                    quoteChar = char
                }

                !inQuote && char.isWhitespace() -> {
                    if (current.isNotEmpty()) {
                        tokens += current.toString()
                        current.setLength(0)
                    }
                }

                else -> current.append(char)
            }
        }

        if (current.isNotEmpty()) {
            tokens += current.toString()
        }

        return tokens
    }

    private fun startMacTerminal(command: String): Boolean {
        val scriptCommand = command
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        val script = "tell application \"Terminal\" to do script \"$scriptCommand\""
        return runCatching {
            ProcessBuilder("osascript", "-e", script).start()
            true
        }.getOrDefault(false)
    }

    private fun startWindowsTerminal(command: String): Boolean {
        return runCatching {
            ProcessBuilder("cmd", "/c", "start", "cmd", "/k", command).start()
            true
        }.getOrDefault(false)
    }

    private fun startLinuxTerminal(command: String): Boolean {
        val commands = listOf(
            listOf("x-terminal-emulator", "-e", "bash", "-lc", command),
            listOf("gnome-terminal", "--", "bash", "-lc", command),
            listOf("konsole", "-e", "bash", "-lc", command),
            listOf("xterm", "-e", "bash", "-lc", command),
        )

        return commands.any { terminalCommand ->
            runCatching {
                ProcessBuilder(terminalCommand).start()
                true
            }.getOrDefault(false)
        }
    }

    private fun shellQuote(value: String): String = "'${value.replace("'", "'\"'\"'")}'"
}
