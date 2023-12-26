package io.adekvatn0.eloservice.league

import io.adekvatn0.eloservice.config.LeagueProperties
import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.dto.LeagueDto
import io.adekvatn0.eloservice.league.dto.LeagueMapper
import io.adekvatn0.eloservice.league.repo.LeagueRepository
import org.springframework.stereotype.Service

@Service
class LeagueService(
    private val leagueRepository: LeagueRepository,
    private val leagueProps: LeagueProperties,
    private val mapper: LeagueMapper
) {

    fun createLeague(leagueDto: LeagueDto): Result<LeagueDto> = leagueRepository.findById(leagueDto.name)
        .map {
            Result.error<LeagueDto>("League with name ${leagueDto.name} already exists")
        }.orElseGet {
            if (leagueDto.initialElo == 0) {
                leagueDto.initialElo = leagueProps.initialElo
            }
            if (leagueDto.kfactor == 0) {
                leagueDto.kfactor = leagueProps.kFactor
            }
            if (leagueDto.points == null) {
                leagueDto.points = LeagueDto.Points(
                    leagueProps.points.win,
                    leagueProps.points.draw,
                    leagueProps.points.lose
                )
            }


            val league = mapper.toEntity(leagueDto)

            val saved = leagueRepository.save(league)
            Result.success(mapper.toDto(saved))
        }

    fun getLeague(name: String): Result<LeagueDto> = leagueRepository.findById(name)
        .map {
            Result.success(mapper.toDto(it))
        }.orElseGet {
            Result.error("League $name not found")
        }
}