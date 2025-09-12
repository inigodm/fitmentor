package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.fitmentor.shared.domain.UserId
import org.springframework.stereotype.Service

@Service
class FindClient(val store: ClientService) {
  fun execute(id: UserId): Client? {
    return store.findByUserId(id)
  }
}
