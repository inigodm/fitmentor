package com.gym.fitmentor.users.coach.infrastructure

import com.gym.fitmentor.users.client.domain.Client
import com.gym.fitmentor.users.client.infrastructure.ClientJPA
import com.gym.fitmentor.users.coach.domain.Coach
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID
import java.util.function.Consumer

/**
 * A Coach.
 */
@Entity
@Table(name = "coach")
class CoachJPA : Serializable {
    @Id
    @Column(name = "id")
    lateinit var id: UUID

    @Column(name = "nutrition")
    var nutrition: Boolean = false

    @Column(name = "fitness")
    var fitness: Boolean = false

    @Column(name = "experience")
    var experience: Int = 0

    @Column(name = "bio")
    lateinit var bio: String

    @Column(name = "training_style")
    lateinit var trainingStyle: String

    @Column(name = "available_days")
    lateinit var availableDays: String

    @Column(name = "city")
    lateinit var city: String

    @Column(name = "works_online")
    var worksOnline: Boolean = false

    @Column(name = "works_in_person")
    var worksInPerson: Boolean = false

    @Column(name = "price_online")
    var priceOnline: Int = -1

    @Column(name = "priceon_person")
    var priceonPerson: Int = -1

    @Column(name = "certifcations")
    lateinit var certifcations: String

    @Column(name = "user")
    lateinit var user: String

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    private var clientJPAS: MutableSet<ClientJPA> = HashSet()

    var clients: MutableSet<ClientJPA>
        get() = this.clientJPAS
        set(clientJPAS) {
            this.clientJPAS.forEach(Consumer { i: ClientJPA? -> i!!.coach = null })
            clientJPAS.forEach(Consumer { i: ClientJPA? -> i!!.coach = this.id })
            this.clientJPAS = clientJPAS
        }

    fun addClient(clientJPA: ClientJPA): CoachJPA {
        this.clientJPAS.add(clientJPA)
        clientJPA.coach = this.id
        return this
    }

    fun removeClient(clientJPA: ClientJPA): CoachJPA {
        this.clientJPAS.remove(clientJPA)
        clientJPA.coach = null
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is CoachJPA) {
            return false
        }
        return this.id != null && this.id == o.id
    }

    override fun hashCode(): Int {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return javaClass.hashCode()
    }

    fun toDomain(): Coach {
        return Coach(
            id = id,
            nutrition = nutrition,
            fitness = fitness,
            experience = experience,
            bio = bio,
            trainingStyle = trainingStyle,
            availableDays = availableDays,
            city = city,
            worksOnline = worksOnline,
            worksInPerson = worksInPerson,
            priceOnline = priceOnline,
            priceonPerson = priceonPerson,
            certifcations = certifcations,
            user = user,
            clients = clientJPAS.map { it.toDomain() }.toMutableSet()
        )
    }

    companion object {
      @JvmStatic
      fun fromDomain(coach: Coach) =
        CoachJPA().apply {
          id = coach.id
          nutrition = coach.nutrition
          fitness = coach.fitness
          experience = coach.experience
          bio = coach.bio
          trainingStyle = coach.trainingStyle
          availableDays = coach.availableDays
          city = coach.city
          worksOnline = coach.worksOnline
          worksInPerson = coach.worksInPerson
          priceOnline = coach.priceOnline
          priceonPerson = coach.priceonPerson
          certifcations = coach.certifcations
          clients = coach
            .clients
            .map { client: Client -> ClientJPA.fromDomain(client) } // Tipo explícito aquí
            .toMutableSet()
      }
    }
}
