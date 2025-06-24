package com.inigo.fitmentor

import com.fasterxml.jackson.core.type.TypeReference
import com.inigo.arch.UserUtils.Companion.objectMapper
import com.inigo.arch.user.domain.Token
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.random.Random

class CoachUtils {
    companion object {
        fun generateCoach(mockMvc: MockMvc,
                         user: String = "00000000-0000-0000-0000-000000000004",
                           id: String = "123e4567-e89b-12d3-a456-426614174000",
                          photo: String = "https://es-us.noticias.yahoo.com/logotipo-g-google-bonito-180000909.html",
                           token: String) {
            val userRequest = mapOf(
                "id" to id,
                "phonenumber" to "123456789",
                "presentation" to "15 años preparador fisico en deportes de equipo",
                "photo" to photo,
                "user" to user)
            val content = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/coaches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .content(objectMapper.writeValueAsString(userRequest))
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        }

        fun getCoach(mockMvc: MockMvc, token:String, coachId: String = "123e4567-e89b-12d3-a456-426614174000"): Map<String, Any>? {
            val content = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/coaches/$coachId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
            return objectMapper.readValue(content, object : TypeReference<Map<String, Any>>() {})
        }
    }
}