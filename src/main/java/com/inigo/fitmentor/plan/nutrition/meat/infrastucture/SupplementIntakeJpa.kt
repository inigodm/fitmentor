package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.fitmentor.plan.nutrition.meat.domain.SupplementIntake
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
    var id: UUID,
    @Column(name = "coach_id")
    var coachId: UUID,
    @Column(name = "client_id")
    var clientId: UUID,
    @Column(name = "meal_id")
    var mealId: UUID,
    @Column(name = "supplement_id", nullable = false)
    var supplementId: UUID,
    @Column(name = "quantity")
    var quantity: Double,
    @Column(name = "unit")
    var unit: String
) {
    constructor() : this(
        id = UUID.randomUUID(),
        coachId = UUID.randomUUID(),
        clientId = UUID.randomUUID(),
        mealId = UUID.randomUUID(),
        supplementId = UUID.randomUUID(),
        quantity = 0.0,
        unit = ""
    )

    companion object {
        fun from(intake: SupplementIntake): SupplementIntakeJpa {
            return SupplementIntakeJpa(
                id = intake.id,
                coachId = intake.coachId,
                clientId = intake.clientId,
                mealId = intake.mealId,
                supplementId = intake.supplementId.value,
                quantity = intake.quantity,
                unit = intake.unit.name
            )
        }
    }
}
