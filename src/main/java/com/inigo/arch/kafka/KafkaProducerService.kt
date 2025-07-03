package com.inigo.arch.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.inigo.arch.shared.domain.AggregateRoot
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(private val kafkaTemplate: KafkaTemplate<String?, String?>,
                           val objectMapper: ObjectMapper) {
    fun <T: AggregateRoot> sendSnapshot(aggregateRoot: T) {
        kafkaTemplate.send(aggregateRoot.name,
            aggregateRoot.uuid.toString(),
            objectMapper.writeValueAsString(aggregateRoot))
    }
}
