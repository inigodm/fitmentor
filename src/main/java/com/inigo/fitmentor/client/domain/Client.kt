package com.inigo.fitmentor.client.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.UserId
import com.inigo.fitmentor.shared.domain.events.ClientCreated
import com.inigo.fitmentor.shared.domain.events.ClientUpdated

class Client(
  val id: ClientId,
  val goals: String? = null,
  val age: Int? = null,
  val injuries: String? = null,
  val weight: Int? = null,
  val equipmentAccess: Int? = null,
  val phonenumber: String? = null,
  val user: UserId
) : AggregateRoot(name = "snapshots.client") {
  // TODO: consider removing
  fun ensureUserExists(store: ClientService): Client {
    if (!store.existsUser(this)) {
      throw IllegalArgumentException("User with id ${this.user} does not exist")
    }
    return this
  }

  fun save(store: ClientService): Client {
    store.save(this)
    record(ClientUpdated(clientId = this.id.value, userId = this.user.value))
    return this
  }

    fun alreadyExists(store: ClientService): Boolean {
        return store.existsClient(this)
    }

  fun create(store: ClientService): Client {
    store.save(this)
    record(ClientCreated(clientId = this.id.value, userId = this.user.value))
    return this
  }

}
