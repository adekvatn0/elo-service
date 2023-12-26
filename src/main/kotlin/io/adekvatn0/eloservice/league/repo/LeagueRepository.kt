package io.adekvatn0.eloservice.league.repo

import io.adekvatn0.eloservice.league.entity.League
import org.springframework.data.mongodb.repository.MongoRepository

interface LeagueRepository : MongoRepository<League, String>