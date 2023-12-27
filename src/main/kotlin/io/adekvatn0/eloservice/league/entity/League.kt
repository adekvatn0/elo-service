package io.adekvatn0.eloservice.league.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("elo_League")
data class League(
    @Indexed
    val name: String,
    val initialElo: Int,
    val kfactor: Int,
    val points: Points,
    @Id
    val id: ObjectId? = null
) {
    data class Points(
        val win: Double,
        val draw: Double,
        val lose: Double
    )
}