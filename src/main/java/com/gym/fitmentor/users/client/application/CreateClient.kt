package com.gym.fitmentor.users.client.application

import com.gym.fitmentor.users.client.domain.Client
import com.gym.fitmentor.users.client.domain.ClientService
import org.springframework.stereotype.Service

@Service
class CreateClient(val store: ClientService) {
  fun execute(client: Client) {
    store.save(client)
  }
}
