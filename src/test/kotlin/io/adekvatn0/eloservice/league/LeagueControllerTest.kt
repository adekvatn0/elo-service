package io.adekvatn0.eloservice.league

import com.fasterxml.jackson.databind.ObjectMapper
import io.adekvatn0.eloservice.MongoRepositoryContainerTest
import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.dto.LeagueDto
import io.adekvatn0.eloservice.league.entity.League
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
class LeagueControllerTest : MongoRepositoryContainerTest() {

    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()

    @Test
    fun getLeagueByNameThenSuccess() {
        val name = "league"
        val initialElo = 1500

        leagueRepository.save(League(name, initialElo, 16, League.Points(1.0, 0.5, 0.0)))

        //test
        mockMvc.get("/leagues?name=$name")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(
                                listOf(
                                    LeagueDto(
                                        name,
                                        initialElo,
                                        16,
                                        LeagueDto.Points(1.0, 0.5, 0.0)
                                    )
                                )
                            )
                        )
                    )
                }
            }
    }
    @Test
    fun getLeaguesThenSuccess() {
        leagueRepository.save(League("name1", 1000, 8, League.Points(1.0, 0.5, 0.0)))
        leagueRepository.save(League("name2", 2000, 16, League.Points(1.0, 0.0, -1.0)))
        leagueRepository.save(League("name3", 1400, 32, League.Points(16.0, 0.0, -16.0)))

        //test
        mockMvc.get("/leagues")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(
                                listOf(
                                    LeagueDto(
                                        "name1",
                                        1000,
                                        8,
                                        LeagueDto.Points(1.0, 0.5, 0.0)
                                    ),
                                    LeagueDto(
                                        "name2",
                                        2000,
                                        16,
                                        LeagueDto.Points(1.0, 0.0, -1.0)
                                    ),
                                    LeagueDto(
                                        "name3",
                                        1400,
                                        32,
                                        LeagueDto.Points(16.0, 0.0, -16.0)
                                    )
                                )
                            )
                        )
                    )
                }
            }
    }


    @Test
    fun getLeagueNotExistsThenError() {
        val name = "random"

        //test
        mockMvc.get("/leagues?name=$name")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.error<LeagueDto>("League $name not found")
                        )
                    )
                }
            }
    }

    @Test
    fun createLeagueThenSuccess() {
        val name = "name"
        val elo = 1500
        val kfactor = 16
        val points = LeagueDto.Points(16.0, 0.0, -16.0)

        //test
        mockMvc.post("/leagues") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                LeagueDto(name, elo, kfactor, points)
            )
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(LeagueDto(name, elo, kfactor, points))
                        )
                    )
                }
            }
    }

    @Test
    fun createLeagueAndNameIsOccupiedThenError() {
        val name = "name"
        val elo = 1500
        val kfactor = 16
        val points = LeagueDto.Points(16.0, 0.0, -16.0)

        leagueRepository.save(League(name, elo, kfactor, League.Points(1.0, 0.0, -1.0)))

        //test
        mockMvc.post("/leagues") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                LeagueDto(name, elo, kfactor, points)
            )
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.error<LeagueDto>("League with name $name already exists")
                        )
                    )
                }
            }
    }

    @Test
    fun createLeagueWithDefaultProperties() {
        val name = "name"
        val elo = 1400
        val kfactor = 16
        val points = LeagueDto.Points(1.0, 0.5, 0.0)

        //test
        mockMvc.post("/leagues") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(LeagueDto(name))
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(LeagueDto(name, elo, kfactor, points))
                        )
                    )
                }
            }
    }
}