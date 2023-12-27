package io.adekvatn0.eloservice.league.dto

import io.adekvatn0.eloservice.league.entity.League
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LeagueMapper {
    fun toDto(league: League): LeagueDto
    fun toEntity(dto: LeagueDto): League
    fun toDto(points: League.Points): LeagueDto.Points
    fun toEntity(dto: LeagueDto.Points): League.Points
}