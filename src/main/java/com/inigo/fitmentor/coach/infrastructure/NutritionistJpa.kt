package com.inigo.fitmentor.coach.infrastructure

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "nutritionists")
class NutritionistJpa {
    @Id
    lateinit var id: UUID
    @Column(name = "user_id")
    lateinit var userId: UUID

    companion object {
        fun fromDomain(userId: UUID, id: UUID = UUID.randomUUID()) : NutritionistJpa {
            return NutritionistJpa().apply {
                this.id = id
                this.userId = userId
            }
        }

    }
}