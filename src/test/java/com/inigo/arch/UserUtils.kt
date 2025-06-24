package com.inigo.arch

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.inigo.arch.user.domain.Role
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.Map

class UserUtils {
    companion object {
        val objectMapper: ObjectMapper = ObjectMapper()

        fun generateUser(mockMvc: MockMvc,
                         uuid: String = "00000000-0000-0000-0000-000000000004",
                         name: String = "iñigo",
                         email: String = "user@test.com",
                         role: String = Role.CLIENT.name) {
            val userRequest = mapOf(
                "id" to uuid,
                "username" to name,
                "password" to "password",
                "email" to email,
                "role" to role
            )

            mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest))
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        }

        fun obtainToken(mockMvc: MockMvc,
                        name: String = "iñigo"): String {
            val userRequest = mapOf(
                "username" to name,
                "password" to "password",
            )

            val content = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest))
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn().response.contentAsString

            return objectMapper.readValue(content, object : TypeReference<Map<String, Any>>() {})["token"].toString()
        }
    }
}