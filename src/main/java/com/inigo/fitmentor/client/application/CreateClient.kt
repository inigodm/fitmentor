package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.shared.domain.events.ClientCreated
import org.springframework.stereotype.Service

@Service
class CreateClient(val store: ClientService) {
  fun execute(client: Client) {
    client.ensureUserExists(store)
      .save(store)
      .record(ClientCreated(client.id.value, client.user.value))
    client.publishEvents()
  }
}
