package ru.renue.camunda.service

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.InputStream
import javax.mail.util.ByteArrayDataSource


@Service
class SmtpService(private val emailSender: JavaMailSender) : JavaDelegate {

    fun sendMessage(
        to: String, subject: String, body: String, vararg attachments: InputStream
    ) {
        val from = "noreply@example.com"
        if (attachments.isNotEmpty()) {
            val message = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            helper.setFrom(from)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText("<html><body>$body</body></html>", true)
            attachments.forEach {
                helper.addInline("image.jpg", ByteArrayDataSource(it, "image/jpeg"))
            }
            emailSender.send(message)
        } else {
            val message = SimpleMailMessage()
            message.from = from
            message.setTo(to)
            message.subject = subject
            message.text = body
            emailSender.send(message)
        }
    }

    override fun execute(execution: DelegateExecution?) {
        execution?.let {
            val to = it.getVariable("email") as String
            val image = it.getVariable("result") as ByteArray
            sendMessage(to, "Hello from camunda", "Your catfact:", image.inputStream())
        }
    }
}
