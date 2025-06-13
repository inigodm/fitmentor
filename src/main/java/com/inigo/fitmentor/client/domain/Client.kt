package com.inigo.fitmentor.client.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.UserId

class Client(
  val id: ClientId,
  val goals: String? = null,
  val age: Int? = null,
  val injuries: String? = null,
  val weight: Int? = null,
  val equipmentAccess: Int? = null,
  val phonenumber: String? = null,
  val user: UserId
) : AggregateRoot() {
  fun ensureUserExists(store: ClientService): Client {
    if (!store.existsUser(this)) {
      throw IllegalArgumentException("User with id ${this.user} does not exist")
    }
    return this
  }

  fun save(store: ClientService): Client {
    store.save(this)
    return this
  }
}