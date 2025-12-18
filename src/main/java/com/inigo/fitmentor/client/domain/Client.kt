package com.inigo.fitmentor.client.domain

import com.inigo.arch.user.domain.Role.CLIENT
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.FitmentorUser
import com.inigo.fitmentor.shared.domain.UserId
import com.inigo.fitmentor.shared.domain.events.ClientCreated
import com.inigo.fitmentor.shared.domain.events.ClientUpdated
import com.inigo.fitmentor.shared.infrastructure.UserService

class Client(
    val id: ClientId,
    val goals: String? = null,
    val age: Int? = null,
    val injuries: String? = null,
    val weight: Int? = null,
    val equipmentAccess: Int? = null,
    user: UserId,
    phonenumber: String? = null,
    email: String,
    username: String,
) : FitmentorUser(aggregateName = "snapshots.client",
    phonenumber = phonenumber,
    email = email,
    username = username,
    user = user,
    role = CLIENT) {
    // TODO: consider removing
    fun ensureUserExists(store: ClientStore, userService: UserService): Client {
        if (!store.existsUser(this)) {
            userService.createUser(this)
        }
        return this
    }

    fun save(store: ClientStore): Client {
        this.user = store.save(this).user
        record(ClientUpdated(clientId = this.id.value, userId = this.user!!.value))
        return this
    }

    fun alreadyExists(store: ClientStore): Boolean {
        return store.existsClient(this)
    }

    fun create(store: ClientStore): Client {
        this.user = store.save(this).user
        record(ClientCreated(clientId = this.id.value, userId = this.user!!.value))
        return this
    }

}
