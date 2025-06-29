package com.inigo.shared.domain.errors

class NotFoundError: RuntimeException {
    private constructor() : super("Resource not found")
    private constructor(message: String) : super(message)
    private constructor(cause: Throwable) : super("Resource not found", cause)
    private constructor(message: String, cause: Throwable) : super(message, cause)

    companion object {
        fun becauseNoCoachExistForGivenId(coachId: String): NotFoundError =
            NotFoundError("Coach with ID $coachId not found")

        fun becauseNoClientExistForGivenId(clientId: String): NotFoundError =
            NotFoundError("Client with ID $clientId not found")
    }
}