package io.adekvatn0.eloservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("league")
data class LeagueProperties(
    val initialElo: Int,
    val kFactor: Int,
    val points: Points
) {

    data class Points(
        val win: Double,
        val draw: Double,
        val lose: Double
    )
}