package io.adekvatn0.eloservice.match.dto

import io.adekvatn0.eloservice.match.entity.MatchResult

data class MatchDto(
    val league: String,
    val player1: String,
    val player2: String,
    val result: MatchResult,
    val eloDelta: Int? = null,
    val timestamp: Long? = null
)