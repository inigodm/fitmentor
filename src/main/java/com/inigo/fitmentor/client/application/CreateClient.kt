package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import org.springframework.stereotype.Service

@Service
class CreateClient(val store: ClientService) {
  fun execute(client: Client) {
    store.save(client)
  }
}
