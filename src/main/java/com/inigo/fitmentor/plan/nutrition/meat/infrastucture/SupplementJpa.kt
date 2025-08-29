package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "foods")
class SupplementJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(name = "name")
    var name: String,
    @Column(name = "description")
    var description: String
)