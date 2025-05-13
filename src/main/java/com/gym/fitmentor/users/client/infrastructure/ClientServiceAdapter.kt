package com.gym.fitmentor.users.client.infrastructure

import com.gym.fitmentor.users.client.domain.Client
import com.gym.fitmentor.users.client.domain.ClientService
import com.gym.fitmentor.users.client.infrastructure.ClientJPA.Companion.fromDomain
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Service Implementation for managing [ClientJPA].
 */
@Service
@Transactional
open class ClientServiceAdapter(private val clientRepository: ClientRepository) : ClientService {
    override fun save(client: Client) {
        LOG.debug("Request to save Client : {}", client)
        clientRepository.save(fromDomain(client))
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<Client> {
        LOG.debug("Request to get all Clients")
        return clientRepository.findAll().stream()
            .map { obj: ClientJPA -> obj.toDomain() }
            .collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Client> {
        LOG.debug("Request to get Client : {}", id)
        return clientRepository.findById(id)
            .map(Function { obj: ClientJPA -> obj.toDomain() })
    }

    override fun delete(id: Long) {
        LOG.debug("Request to delete Client : {}", id)
        clientRepository.deleteById(id)
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ClientServiceAdapter::class.java)
    }
}
