package io.adekvatn0.eloservice.player

import com.fasterxml.jackson.databind.ObjectMapper
import io.adekvatn0.eloservice.MongoRepositoryContainerTest
import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.entity.League
import io.adekvatn0.eloservice.player.dto.PlayerDto
import io.adekvatn0.eloservice.player.entity.Player
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@AutoConfigureMockMvc
class PlayerControllerTest : MongoRepositoryContainerTest() {

    @Autowired
    lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()

    @Test
    fun findPlayerByNameThenSuccess() {
        val name = "name"
        val league = "league"
        val elo = 1234

        playerRepository.save(Player(name, league, elo))
        leagueRepository.save(League(league, 123, 16, League.Points(1.0, 0.5, 0.0)))

        mockMvc.get("/leagues/$league/players?name=$name")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(listOf(PlayerDto(name, league, elo)))
                        )
                    )
                }
            }
    }

    @Test
    fun findPlayersSortedAndPagedThenSuccess() {
        val league = "league"

        repeat(10) {
            playerRepository.save(Player("name$it", league, elo = it))
        }

        mockMvc.get("/leagues/$league/players?page=1&size=4")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(
                                listOf(
                                    PlayerDto("name5", league, 5, PlayerDto.Statistic()),
                                    PlayerDto("name4", league, 4),
                                    PlayerDto("name3", league, 3),
                                    PlayerDto("name2", league, 2)
                                )
                            )
                        )
                    )
                }
            }
    }

    @Test
    fun findPlayersDefaultPageAndSizeThenTopTen() {
        val league = "league"

        repeat(20) {
            playerRepository.save(Player("name$it", league, elo = it))
        }

        mockMvc.get("/leagues/$league/players")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(
                                listOf(
                                    PlayerDto("name19", league, 19),
                                    PlayerDto("name18", league, 18),
                                    PlayerDto("name17", league, 17),
                                    PlayerDto("name16", league, 16),
                                    PlayerDto("name15", league, 15),
                                    PlayerDto("name14", league, 14),
                                    PlayerDto("name13", league, 13),
                                    PlayerDto("name12", league, 12),
                                    PlayerDto("name11", league, 11),
                                    PlayerDto("name10", league, 10)
                                )
                            )
                        )
                    )
                }
            }
    }

    @Test
    fun findPlayerByNameAndNoUserExistThenError() {
        val name = "name"
        val league = "league"

        mockMvc.get("/leagues/$league/players?name=$name")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.error<PlayerDto>("Player $name not found")
                        )
                    )
                }
            }
    }

    @Test
    fun createPlayerThenSuccess() {
        val leagueName = "league"
        val initialElo = 1500
        val name = "name"
        val kFactor = 16

        val league = leagueRepository.save(
            League(
                leagueName,
                initialElo,
                kFactor,
                League.Points(1.0, 0.5, 0.0)
            )
        )

        mockMvc.post("/leagues/$leagueName/players") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(PlayerDto(name, leagueName))
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.success(
                                PlayerDto(
                                    name,
                                    leagueName,
                                    league.initialElo
                                )
                            )
                        )
                    )
                }
            }
    }

    @Test
    fun createPlayerAndNameIsOccupiedThenError() {
        val name = "name"
        val league = "league"


        playerRepository.save(Player(name, league, 1234))

        mockMvc.post("/leagues/$league/players") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(PlayerDto(name, league))
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            Result.error<List<PlayerDto>>("Player with name $name in league $league already exists")
                        )
                    )
                }
            }
    }
}