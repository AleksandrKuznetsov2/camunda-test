package ru.renue.camunda.service

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.InputStream

@Service
class ImaginaryService(
    private val rest: RestTemplate,
    @Value("\${imaginary.base-path}")
    private val basePath: String
) : JavaDelegate {

    fun watermark(source: String, watermark: String): InputStream {
        val response = rest.getForEntity<Resource>("$basePath/watermark?text=$watermark&opacity=1&noreplicate=true&textwidth=300&url=$source")
        return response.body?.inputStream ?: throw RuntimeException()
    }

    override fun execute(execution: DelegateExecution?) {
        execution?.let {
            val image = it.getVariable("image") as String
            val fact = it.getVariable("fact") as String
            it.setVariable("result", watermark(image, fact))
        }
    }
}
