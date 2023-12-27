package io.adekvatn0.eloservice.player.dto

import io.adekvatn0.eloservice.player.entity.Player
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PlayerMapper {
    fun toDto(player: Player): PlayerDto
    fun toEntity(dto: PlayerDto): Player
}