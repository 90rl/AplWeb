package p.lodzka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrelloBackend

/**
 * A main method to start this application.
 */
fun main(args: Array<String>) {
    runApplication<TrelloBackend>(*args)
}