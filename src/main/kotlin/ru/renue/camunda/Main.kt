package ru.renue.camunda

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application {
    companion object {
        internal val log = LoggerFactory.getLogger(Application::class.java)
    }

    @EventListener
    fun onEvent(event: ApplicationReadyEvent) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!")
        log.info("")
        log.info("Account: demo / demo")
        log.info("Tasklist: http://localhost:8080/camunda/app/tasklist/")
        log.info("Cockpit: http://localhost:8080/camunda/app/cockpit/")
        log.info("MailDev: http://localhost:1080/")
        log.info("")
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!")
    }
}
