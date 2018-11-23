package id.idham.footballmatchschedule.ui.teams

import id.idham.footballmatchschedule.data.model.League
import id.idham.footballmatchschedule.data.model.Team

interface TeamsView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(data: List<League>)
    fun showTeamList(data: List<Team>)
}