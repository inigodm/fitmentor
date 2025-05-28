package com.inigo.fitmentor.client.infrastructure

import com.inigo.fitmentor.client.domain.Client
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.UserId
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

/**
 * A Client.
 */
@Entity
@Table(name = "clients")
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

    @Column(name = "phone_number")
    var phonenumber: String? = null

    @Column(name = "user_id")
    lateinit var user: UUID

  fun toDomain(): Client {
    return Client(
      id = ClientId(id),
      goals = goals,
      age = age,
      injuries = injuries,
      weight = weight,
      equipmentAccess = equipmentAccess,
      phonenumber = phonenumber,
      user = UserId(user) // Assuming plans are not stored in this entity
    )
  }
  companion object {
    @JvmStatic
    fun fromDomain(client: Client) = ClientJpa().apply {
        id = client.id.value
        goals = client.goals
        age = client.age
        injuries = client.injuries
        weight = client.weight
        equipmentAccess = client.equipmentAccess
        phonenumber = client.phonenumber
        user = client.user.value
    }
  }
}
