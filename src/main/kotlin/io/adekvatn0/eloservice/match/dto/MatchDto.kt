package io.adekvatn0.eloservice.match.dto

import io.adekvatn0.eloservice.match.entity.MatchResult
import java.time.ZonedDateTime

data class MatchDto(
    val league: String,
    val player1: String,
    val player2: String,
    val eloDelta: Int,
    val result: MatchResult,
    val date: ZonedDateTime? = null
)