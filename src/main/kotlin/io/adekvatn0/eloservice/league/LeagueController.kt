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
    fun findLeagues(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) page: Int = 0,
        @RequestParam(required = false) size: Int = 10,
    ): Result<List<LeagueDto>> {
        return leagueService.findLeagues(name, page, size)
    }

    @PostMapping
    fun createLeague(
        @RequestBody request: LeagueDto
    ): Result<LeagueDto> {
        return leagueService.createLeague(request)
    }
}