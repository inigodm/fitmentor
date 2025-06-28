package com.inigo.fitmentor

import com.fasterxml.jackson.core.type.TypeReference
import com.inigo.arch.UserUtils.Companion.objectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.time.temporal.ChronoUnit

class PlanUtils {
    companion object {
        fun generatePlan(mockMvc: MockMvc,
                         id: String = "00000000-0000-0000-0000-000000000004",
                         client: String = "123e4567-e89b-12d3-a456-426614174000",
                         coach: String = "123e4567-e89b-12d3-a456-426614174002",
                         token: String) {
            val userRequest = mapOf(
                "id" to id,
                "active" to true,
                "client" to client,
                "coach" to coach,
                "description" to "Plan de entrenamiento personalizado",
                "type" to "Entrenamiento funcional",
                "goals" to "Mejorar fuerza y resistencia",
                "equipment" to "Mancuernas, Esteras, Bandas elásticas",
                "startDate" to Instant.now(),
                "endDate" to Instant.now().plus(30, ChronoUnit.DAYS),
                "createdAt" to Instant.now(),
                "updatedAt" to Instant.now())
            val content = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/plans")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .content(objectMapper.writeValueAsString(userRequest))
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        }

        fun getPlan(mockMvc: MockMvc, token:String, clientId: String = "123e4567-e89b-12d3-a456-426614174000"): List<Map<String, Any>>? {
            val content = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/plans/client/$clientId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
            return objectMapper.readValue(content, object : TypeReference<List<Map<String, Any>>>() {})
        }
    }
}