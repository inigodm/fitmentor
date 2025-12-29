package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientStore
import com.inigo.fitmentor.shared.infrastructure.UserService
import org.springframework.stereotype.Service

@Service
class UpdateClient(val store: ClientStore, val userService: UserService) {
  fun execute(client: Client) {
    client.ensureUserExists(store, userService)
    client.save(store)
    client.publishEvents()
  }
}
