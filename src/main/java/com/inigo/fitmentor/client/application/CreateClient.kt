package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientStore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateClient(val store: ClientStore) {
  fun execute(client: Client) {
    if (client.alreadyExists(store)) {
      LOG.warn("Client already exists with ID: ${client.id}")
      return
    }
    client.ensureUserExists(store)
    client.create(store)
    client.publishEvents()
  }

  companion object {
    private val LOG: Logger = LoggerFactory.getLogger(CreateClient::class.java)
  }
}
