package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "supplement_intakes")
class SupplementIntakeJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(name = "plan_id")
    var planId: UUID,
    @Column(name = "supplement_id")
    var supplementId: UUID,
    @Column(name = "supplement_name")
    var supplementName: String,
    @Column(name = "quantity")
    var quantity: Double,
    @Column(name = "unit")
    var unit: String,
    @Column(name = "time_of_day")
    var timeOfDay: String
)
