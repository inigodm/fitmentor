package com.inigo.arch.shared.domain.errors


class SnapshotNotSendError(message: String) : RuntimeException(message) {
    companion object {
        fun becauseNoClientExistsForGivenId(clientId: String): SnapshotNotSendError =
            SnapshotNotSendError("Client with ID $clientId not found")

        fun becauseNoCoachExistsForGivenId(coachId: String): SnapshotNotSendError =
            SnapshotNotSendError("Client with ID $coachId clientId not found")
    }
}