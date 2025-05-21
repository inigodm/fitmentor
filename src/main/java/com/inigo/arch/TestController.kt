package com.inigo.arch

import org.springframework.web.bind.annotation.*

@RestController
class TestController {
    @GetMapping("/test")
    fun test(): String {
        return "Hello, World!"
    }
}