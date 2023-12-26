package io.adekvatn0.eloservice

import io.adekvatn0.eloservice.config.LeagueProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableConfigurationProperties(LeagueProperties::class)
@EnableMongoRepositories
class EloServiceApplication

fun main(args: Array<String>) {
	runApplication<EloServiceApplication>(*args)
}