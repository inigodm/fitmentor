package com.inigo.fitmentor

import com.fasterxml.jackson.core.type.TypeReference
import com.inigo.arch.UserUtils.Companion.objectMapper
import com.inigo.arch.user.domain.Token
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.random.Random

class ClientUtils {
    companion object {
        fun generateClient(mockMvc: MockMvc,
                         user: String = "00000000-0000-0000-0000-000000000004",
                           id: String = "123e4567-e89b-12d3-a456-426614174000",
                         coach: String = "123e4567-e89b-12d3-a456-426614174002",
                           token: String) {
            val userRequest = mapOf(
                "id" to id,
                "goals" to "Perder peso y ganar músculo",
                "age" to Random.nextInt(18, 60),
                "injuries" to "Mofollon",
                "weight" to Random.nextInt(60, 110),
                "equipmentAccess" to 1,
                "preferedTrainingStyle" to "Entrenamiento funcional",
                "phonenumber" to "123456789",
                "user" to user,
                "coach" to coach)
            val content = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .content(objectMapper.writeValueAsString(userRequest))
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        }

        fun getClient(mockMvc: MockMvc, token:String, clientId: String = "123e4567-e89b-12d3-a456-426614174000"): Map<String, Any>? {
            val content = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/clients/$clientId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
            return objectMapper.readValue(content, object : TypeReference<Map<String, Any>>() {})
        }
    }
}