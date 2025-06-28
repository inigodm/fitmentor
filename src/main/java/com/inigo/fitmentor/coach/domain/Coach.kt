package com.inigo.fitmentor.coach.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.CoachId
import com.inigo.shared.domain.UserId
import com.inigo.shared.domain.events.CoachCreated
import com.inigo.shared.domain.events.CoachUpdated
import jakarta.persistence.Column
import jakarta.persistence.Id
import java.util.UUID

class Coach(
  val id: CoachId,
  val photo: String? = null,
  val presentation: String? = null,
  var phonenumber: String? = null,
  val user: UserId
) : AggregateRoot() {

  fun ensureUserExists(store: CoachService): Coach {
    if (!store.existsUser(this)) {
      throw IllegalArgumentException("User with id ${this.user} does not exist")
    }
    return this
  }

  fun save(store: CoachService): Coach {
    store.save(this)
    record(CoachUpdated(coachId = this.id.value, userId = this.user.value))
    return this
  }

  fun create(store: CoachService): Coach {
    store.save(this)
    record(CoachCreated(coachId = this.id.value, userId = this.user.value))
    return this
  }

  fun alreadyExists(store: CoachService): Boolean {
    return store.existsCoach(this)
  }
}