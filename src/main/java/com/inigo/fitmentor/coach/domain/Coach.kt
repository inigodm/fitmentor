package com.inigo.fitmentor.coach.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.UserId
import com.inigo.fitmentor.shared.domain.events.CoachCreated
import com.inigo.fitmentor.shared.domain.events.CoachUpdated

class Coach(
  val id: CoachId,
  val photo: String? = null,
  val presentation: String? = null,
  var phonenumber: String? = null,
  val user: UserId
) : AggregateRoot(name = "snapshots.coach") {

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