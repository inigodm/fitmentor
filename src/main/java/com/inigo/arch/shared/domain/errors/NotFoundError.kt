package com.inigo.arch.shared.domain.errors

class NotFoundError: RuntimeException {
    private constructor(message: String) : super(message)

    companion object {
        fun becauseNoCoachExistForGivenId(coachId: String): NotFoundError =
            NotFoundError("Coach with ID $coachId not found")

        fun becauseNoClientExistForGivenId(clientId: String): NotFoundError =
            NotFoundError("Client with ID $clientId not found")

        fun becauseUserIsNotCoach(userId: String): NotFoundError =
            NotFoundError("User with ID $userId is not a coach")

        fun becauseUserIsNotClient(userId: String): NotFoundError =
            NotFoundError("User with ID $userId is not a client")

        fun becauseNoMealFound(mealId: String, e: Exception): NotFoundError =
            NotFoundError("Problem adding meal with ID $mealId: ${e.message}")
    }
}