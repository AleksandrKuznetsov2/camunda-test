package ru.renue.camunda.service

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class CafactService(private val rest: RestTemplate) : JavaDelegate {
    fun getFact() : String {
        val response = rest.getForObject<CatfactDto>("https://catfact.ninja/fact")
        return response.fact
    }

    override fun execute(execution: DelegateExecution?) {
        execution?.setVariable("fact", getFact())
    }
}


private data class CatfactDto(val fact: String = "")
