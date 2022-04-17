package ru.renue.camunda.service

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.InputStream

@Service
class PicsumService(private val rest: RestTemplate) : JavaDelegate {
    fun getImage(with: Int = 600, height: Int = 300): InputStream {
        val response = rest.getForEntity<Resource>(getImageUrl(with, height))
        return response.body?.inputStream ?: throw RuntimeException()
    }

    fun getImageUrl(with: Int = 600, height: Int = 300): String {
        return "https://picsum.photos/$with/$height"
    }

    override fun execute(execution: DelegateExecution?) {
        execution?.setVariable("image", getImageUrl())
    }
}
