package com.inigo.fitmentor.client.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.spring.BearerService
import com.inigo.arch.spring.LoggedInUser
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.client.application.CreateClient
import com.inigo.fitmentor.client.application.FindClient
import com.inigo.fitmentor.client.application.UpdateClient
import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.UserId
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ArchApplication::class]
)
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var findClient: FindClient

    @MockkBean(relaxed = true)
    private lateinit var updateClient: UpdateClient

    @MockkBean(relaxed = true)
    private lateinit var createClient: CreateClient

    @MockkBean(relaxed = true)
    private lateinit var bearerService: BearerService

    @Test
    fun `should get client successfully when client exists`() {
        // Given
        val userId = UUID.randomUUID()
        val clientId = UUID.randomUUID()
        val token = "valid-token"

        val client = Client(
            id = ClientId(clientId),
            goals = "Lose weight",
            age = 30,
            injuries = "None",
            weight = 80,
            equipmentAccess = 1,
            phonenumber = "123456789",
            email = "test@example.com",
            username = "testuser",
            user = UserId(userId)
        )

        every { bearerService.parseToken(token) } returns LoggedInUser(
            name = "testuser",
            email = "test@example.com",
            id = userId,
            userRole = 2,
            clientId = clientId.toString(),
            coachId = null
        )
        every { findClient.execute(UserId(userId)) } returns client

        // When & Then
        mockMvc.perform(
            get("/api/user/clients")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(clientId.toString()))
            .andExpect(jsonPath("$.goals").value("Lose weight"))
            .andExpect(jsonPath("$.age").value(30))
            .andExpect(jsonPath("$.injuries").value("None"))
            .andExpect(jsonPath("$.weight").value(80))
            .andExpect(jsonPath("$.equipmentAccess").value(1))
            .andExpect(jsonPath("$.phonenumber").value("123456789"))
            .andExpect(jsonPath("$.user").value(userId.toString()))

        verify(exactly = 1) { bearerService.parseToken(token) }
        verify(exactly = 1) { findClient.execute(UserId(userId)) }
    }

    @Test
    fun `should return 404 when client does not exist`() {
        // Given
        val userId = UUID.randomUUID()
        val token = "valid-token"

        every { bearerService.parseToken(token) } returns LoggedInUser(
            name = "testuser",
            email = "test@example.com",
            id = userId,
            userRole = 2,
            clientId = null,
            coachId = null
        )
        every { findClient.execute(UserId(userId)) } returns null

        // When & Then
        mockMvc.perform(
            get("/api/user/clients")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isNotFound)

        verify(exactly = 1) { bearerService.parseToken(token) }
        verify(exactly = 1) { findClient.execute(UserId(userId)) }
    }

    @Test
    fun `should create client successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        justRun { createClient.execute(any()) }

        val requestBody = """
            {
                "id": {"value": "$clientId"},
                "goals": "Gain muscle",
                "age": 25,
                "injuries": "Knee",
                "weight": 75,
                "equipmentAccess": 2,
                "phonenumber": "987654321",
                "user": {"value": "$userId"},
                "email": "client@example.com",
                "username": "newclient"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { createClient.execute(any()) }
    }

    @Test
    fun `should return 400 when creating client with missing required fields`() {
        // Given
        val requestBody = """
            {
                "goals": "Gain muscle",
                "age": 25
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { createClient.execute(any()) }
    }

    @Test
    fun `should update client successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        justRun { updateClient.execute(any()) }

        val requestBody = """
            {
                "id": {"value": "$clientId"},
                "goals": "Maintain fitness",
                "age": 30,
                "injuries": "None",
                "weight": 80,
                "equipmentAccess": 1,
                "phonenumber": "111222333",
                "user": {"value": "$userId"},
                "email": "updated@example.com",
                "username": "updateduser"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { updateClient.execute(any()) }
    }

    @Test
    fun `should return 400 when updating client with missing required fields`() {
        // Given
        val requestBody = """
            {
                "goals": "Maintain fitness",
                "age": 30
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { updateClient.execute(any()) }
    }

    @Test
    fun `should handle token with extra whitespace`() {
        // Given
        val userId = UUID.randomUUID()
        val clientId = UUID.randomUUID()
        val token = "valid-token"

        val client = Client(
            id = ClientId(clientId),
            goals = "Lose weight",
            age = 30,
            injuries = "None",
            weight = 80,
            equipmentAccess = 1,
            phonenumber = "123456789",
            email = "test@example.com",
            username = "testuser",
            user = UserId(userId)
        )

        every { bearerService.parseToken(token) } returns LoggedInUser(
            name = "testuser",
            email = "test@example.com",
            id = userId,
            userRole = 2,
            clientId = clientId.toString(),
            coachId = null
        )
        every { findClient.execute(UserId(userId)) } returns client

        // When & Then
        mockMvc.perform(
            get("/api/user/clients")
                .header("Authorization", "Bearer   $token  ")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(clientId.toString()))

        verify(exactly = 1) { bearerService.parseToken(token) }
    }
}

