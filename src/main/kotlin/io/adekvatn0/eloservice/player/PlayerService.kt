package io.adekvatn0.eloservice.player

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.repo.LeagueRepository
import io.adekvatn0.eloservice.player.dto.PlayerDto
import io.adekvatn0.eloservice.player.dto.PlayerMapper
import io.adekvatn0.eloservice.player.repo.PlayerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
    private val leagueRepository: LeagueRepository,
    private val mapper: PlayerMapper
) {

    fun findPlayers(league: String, name: String?, page: Int, size: Int) = name?.let {
        playerRepository.findByLeagueAndName(league, name)
            .map {
                Result.success(listOf(mapper.toDto(it)))
            }.orElseGet {
                Result.error("Player $name not found")
            }
    } ?: run {
        val players = playerRepository.findAllByLeague(league, PageRequest.of(page, size, Sort.by("elo").descending()))
        Result.success(players.map { mapper.toDto(it) }.toList())
    }

    fun createPlayer(dto: PlayerDto): Result<PlayerDto> = playerRepository.findByLeagueAndName(dto.league, dto.name)
        .map {
            Result.error<PlayerDto>("Player with name ${dto.name} in league ${dto.league} already exists")
        }.orElseGet {
            val league = leagueRepository.findByName(dto.league)
            if (!league.isPresent) {
                return@orElseGet Result.error("League ${dto.league} does not exists")
            }

            val player = mapper.toEntity(dto)
                .copy(elo = league.get().initialElo)

            val saved = playerRepository.save(player)

            Result.success(mapper.toDto(saved))
        }
}