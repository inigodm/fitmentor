package com.inigo.fitmentor.client.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.spring.BearerService
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.UserId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest(classes = [ArchApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@Transactional
class ClientControllerE2ETest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var clientRepository: ClientJpaRepository

    @Autowired
    private lateinit var bearerService: BearerService

    @Test
    fun `E2E - should create and retrieve a client successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val createRequestBody = """
            {
                "id": {"value": "$clientId"},
                "goals": "Build muscle mass",
                "age": 28,
                "injuries": "None",
                "weight": 75,
                "equipmentAccess": 2,
                "phonenumber": "555-1234",
                "user": {"value": "$userId"},
                "email": "e2e@example.com",
                "username": "e2euser"
            }
        """.trimIndent()

        // When - Create client
        mockMvc.perform(
            post("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody)
        )
            .andExpect(status().isOk)

        // Then - Retrieve client
        val token = bearerService.generateToken(
            username = "e2euser",
            email = "e2e@example.com",
            id = userId,
            clientId = clientId,
            coachId = null,
            userRole = 2
        ).removePrefix("Bearer ")

        mockMvc.perform(
            get("/api/user/clients")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(clientId.toString()))
            .andExpect(jsonPath("$.goals").value("Build muscle mass"))
            .andExpect(jsonPath("$.age").value(28))
            .andExpect(jsonPath("$.weight").value(75))
    }

    @Test
    fun `E2E - should update an existing client successfully`() {
        // Given - Create a client first
        val clientId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val client = Client(
            id = ClientId(clientId),
            goals = "Initial goal",
            age = 25,
            injuries = "None",
            weight = 70,
            equipmentAccess = 1,
            phonenumber = "111-1111",
            email = "initial@example.com",
            username = "initialuser",
            user = UserId(userId)
        )

        val savedClientJpa = ClientJpa.fromDomain(client)
        clientRepository.save(savedClientJpa)

        val updateRequestBody = """
            {
                "id": {"value": "$clientId"},
                "goals": "Updated goal - lose weight",
                "age": 26,
                "injuries": "Knee pain",
                "weight": 65,
                "equipmentAccess": 2,
                "phonenumber": "222-2222",
                "user": {"value": "$userId"},
                "email": "updated@example.com",
                "username": "updateduser"
            }
        """.trimIndent()

        // When - Update client
        mockMvc.perform(
            put("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody)
        )
            .andExpect(status().isOk)

        // Then - Verify update
        val token = bearerService.generateToken(
            username = "updateduser",
            email = "updated@example.com",
            id = userId,
            clientId = clientId,
            coachId = null,
            userRole = 2
        ).removePrefix("Bearer ")

        mockMvc.perform(
            get("/api/user/clients")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.goals").value("Updated goal - lose weight"))
            .andExpect(jsonPath("$.age").value(26))
            .andExpect(jsonPath("$.weight").value(65))
            .andExpect(jsonPath("$.injuries").value("Knee pain"))
    }

    @Test
    fun `E2E - should return 404 for non-existing client`() {
        // Given
        val nonExistingUserId = UUID.randomUUID()
        val token = bearerService.generateToken(
            username = "nonexisting",
            email = "nonexisting@example.com",
            id = nonExistingUserId,
            clientId = null,
            coachId = null,
            userRole = 2
        ).removePrefix("Bearer ")

        // When & Then
        mockMvc.perform(
            get("/api/user/clients")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isNotFound)
    }
}

