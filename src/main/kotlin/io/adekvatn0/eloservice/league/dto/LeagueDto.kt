package io.adekvatn0.eloservice.league.dto

data class LeagueDto(
    val name: String,
    var initialElo: Int = 0,
    var kfactor: Int = 0,
    var points: Points? = null
) {
    data class Points(
        val win: Double,
        val draw: Double,
        val lose: Double
    )
}