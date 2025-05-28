package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FindClient(val store: ClientService) {
  fun execute(id: UUID): Client? {
    return store.findOne(id)
  }
}
