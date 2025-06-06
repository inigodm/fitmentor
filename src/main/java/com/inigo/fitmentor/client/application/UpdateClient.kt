package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.shared.domain.events.ClientUpdated
import org.springframework.stereotype.Service

@Service
class UpdateClient(val store: ClientService) {
  fun execute(client: Client) {
    client.ensureClientExists(store)
    client.save(store)
    client.record(ClientUpdated(client.id.value, client.user.value))
    client.publishEvents()
  }
}
