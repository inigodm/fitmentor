package com.gym.fitmentor.users.client.application

import com.inigo.fitmentor.client.domain.Client
import com.gym.fitmentor.users.client.domain.ClientService
import org.springframework.stereotype.Service

@Service
class UpdateClient(val store: ClientService) {
  fun execute(client: Client) {
    store.save(client)
  }
}
