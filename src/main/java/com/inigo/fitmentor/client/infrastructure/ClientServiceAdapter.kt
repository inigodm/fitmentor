package com.inigo.fitmentor.client.infrastructure

import com.inigo.arch.user.infrastucture.jpa.UserJpaRepository
import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.fitmentor.client.infrastructure.ClientJpa.Companion.fromDomain
import com.inigo.shared.domain.ClientId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Service Implementation for managing [ClientJpa].
 */
@Service
@Transactional
open class ClientServiceAdapter(
    private val clientRepository: ClientRepository,
    private val userJpaRepository: UserJpaRepository
    ) : ClientService {
    override fun save(client: Client) {
        LOG.debug("Request to save Client : {}", client)
        clientRepository.save(fromDomain(client))
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<Client> {
        LOG.debug("Request to get all Clients")
        return clientRepository.findAll().stream()
            .map { obj: ClientJpa -> obj.toDomain() }
            .collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    override fun findByClientId(id: ClientId): Client? {
        LOG.debug("Request to get Client : {}", id.value)
        return clientRepository.findById(id.value)
            .map(Function { obj: ClientJpa -> obj.toDomain() }).orElse(null)
    }

    override fun delete(id: UUID) {
        LOG.debug("Request to delete Client : {}", id)
        clientRepository.deleteById(id)
    }

    override fun existsUser(client: Client) : Boolean {
        LOG.debug("Request to check existence of client : {}  as user : {}", client.id.value, client.user.value)
        return userJpaRepository.findById(client.user.value).isPresent
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ClientServiceAdapter::class.java)
    }
}
