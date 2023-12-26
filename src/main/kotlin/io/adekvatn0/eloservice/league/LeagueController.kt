package io.adekvatn0.eloservice.league

import io.adekvatn0.eloservice.dto.Result
import io.adekvatn0.eloservice.league.dto.LeagueDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("leagues")
class LeagueController(
    private val leagueService: LeagueService
) {

    @GetMapping
    fun getLeague(
        @RequestParam name: String
    ): Result<LeagueDto> {
        return leagueService.getLeague(name)
    }

    @PostMapping
    fun createLeague(
        @RequestBody request: LeagueDto
    ): Result<LeagueDto> {
        return leagueService.createLeague(request)
    }

}