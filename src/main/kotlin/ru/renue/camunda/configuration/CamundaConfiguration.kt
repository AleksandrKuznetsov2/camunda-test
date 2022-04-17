package ru.renue.camunda.configuration

import org.camunda.bpm.spring.boot.starter.CamundaBpmEnterpriseAutoConfiguration
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties
import org.camunda.bpm.spring.boot.starter.rest.CamundaBpmRestJerseyAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

/**
 * WTF?
 */
@Configuration
@ConditionalOnProperty(prefix = CamundaBpmProperties.PREFIX, name = ["enabled"], havingValue = "false")
@EnableAutoConfiguration(exclude = [CamundaBpmRestJerseyAutoConfiguration::class, CamundaBpmEnterpriseAutoConfiguration::class])
class CamundaConfiguration
