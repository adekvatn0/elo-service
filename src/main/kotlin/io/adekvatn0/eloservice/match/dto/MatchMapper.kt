package io.adekvatn0.eloservice.match.dto

import io.adekvatn0.eloservice.match.entity.Match
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MatchMapper {

    fun toDto(match: Match): MatchDto
    fun toEntity(dto: MatchDto): Match
}