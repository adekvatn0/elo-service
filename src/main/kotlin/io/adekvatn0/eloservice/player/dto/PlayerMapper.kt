package io.adekvatn0.eloservice.player.dto

import io.adekvatn0.eloservice.player.entity.Player
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PlayerMapper {
    fun toDto(player: Player): PlayerDto
    fun toEntity(dto: PlayerDto): Player
    fun toDto(stats: Player.Statistic): PlayerDto.Statistic
    fun toEntity(stats: PlayerDto.Statistic): Player.Statistic
}