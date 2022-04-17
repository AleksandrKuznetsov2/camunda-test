package ru.renue.camunda.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class MailDevTestService(
    private val rest: RestTemplate,
    @Value("\${mail-dev-api-path}")
    private val mailDevPath: String
) {

    fun checkEmail(to: String, subject: String) {
        rest.getForObject<Array<Email>>("$mailDevPath/email")
            .find {
                it.subject == subject && it.to?.any { it.address == to }!!
            } ?: throw AssertionError("Email not found")
    }
}

@Suppress("ArrayInDataClass")
private data class Email(
    val text: String = "",
    val subject: String = "",
    val from: Array<Address>? = null,
    val to: Array<Address>? = null,
)

private data class Address(
    val address: String = "",
    val name: String = "",
)
