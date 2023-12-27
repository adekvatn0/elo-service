package io.adekvatn0.eloservice.player.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document("elo_Player")
@CompoundIndex(def = "{'name': 1, 'league': 1}", unique = true)
data class Player(
    val name: String,
    val league: String,
    val elo: Int = 0,
    val wins: Int = 0,
    val loses: Int = 0,
    val draws: Int = 0,
    @Id
    val id: ObjectId? = null
)