package com.inigo.fitmentor.coach.infrastructure

import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.shared.domain.CoachId
import com.inigo.shared.domain.UserId
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

/**
 * A Coach.
 */
@Entity
@Table(name = "coaches")
class CoachJpa : Serializable {
    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Id
    @Column(name = "id", nullable = false)
    lateinit var id: UUID

    @Column(name = "photo")
    var photo: String? = null

    @Column(name = "presentation")
    var presentation: String? = null

    @Column(name = "phone_number")
    var phonenumber: String? = null

    @Column(name = "user_id")
    lateinit var user: UUID

  fun toDomain(): Coach {
    return Coach(
      id = CoachId(id),
      photo = photo,
      presentation = presentation,
      phonenumber = phonenumber,
      user = UserId(user) // Assuming plans are not stored in this entity
    )
  }
  companion object {
    @JvmStatic
    fun fromDomain(coach: Coach) = CoachJpa().apply {
        id = coach.id.value
        photo = coach.photo
        presentation = coach.presentation
        phonenumber = coach.phonenumber
        user = coach.user.value
    }
  }
}
