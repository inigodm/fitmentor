package com.gym.fitmentor.users.client.infrastructure

import com.inigo.fitmentor.client.domain.Client
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

/**
 * A Client.
 */
@Entity
@Table(name = "client")
class ClientJpa : Serializable {
    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Id
    @Column(name = "id", nullable = false)
    lateinit var id: UUID

    @Column(name = "goals")
    var goals: String? = null

    @Column(name = "age")
    var age: Int? = null

    @Column(name = "injuries")
    var injuries: String? = null

    @Column(name = "weight")
    var weight: Int? = null

    @Column(name = "equipment_access")
    var equipmentAccess: Int? = null

    @Column(name = "prefered_training_style")
    var preferedTrainingStyle: String? = null

    @Column(name = "phonenumber")
    var phonenumber: String? = null

    @Column(name = "user")
    lateinit var user: UUID

    @Column(name = "coach")
    var coach: UUID? = null

  fun toDomain(): Client {
    return Client(
      id = id,
      goals = goals,
      age = age,
      injuries = injuries,
      weight = weight,
      equipmentAccess = equipmentAccess,
      preferedTrainingStyle = preferedTrainingStyle,
      phonenumber = phonenumber,
      user = user,
      coach = coach
    )
  }
  companion object {
    @JvmStatic
    fun fromDomain(client: Client) = ClientJpa().apply {
        id = client.id
        goals = client.goals
        age = client.age
        injuries = client.injuries
        weight = client.weight
        equipmentAccess = client.equipmentAccess
        preferedTrainingStyle = client.preferedTrainingStyle
        phonenumber = client.phonenumber
        user = client.user
        coach = client.coach
    }
  }
}
