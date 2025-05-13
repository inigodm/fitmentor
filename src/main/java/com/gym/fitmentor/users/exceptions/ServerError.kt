package com.gym.fitmentor.users.exceptions

class ServerError : RuntimeException {
  constructor() : super()
  constructor(message: String) : super(message)
  constructor(message: String, cause: Throwable) : super(message, cause)
  constructor(cause: Throwable) : super(cause)
}
