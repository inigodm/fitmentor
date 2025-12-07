package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientStore
import org.springframework.stereotype.Service

@Service
class UpdateClient(val store: ClientStore) {
  fun execute(client: Client) {
    client.ensureUserExists(store)
    client.save(store)
    client.publishEvents()
  }
}
