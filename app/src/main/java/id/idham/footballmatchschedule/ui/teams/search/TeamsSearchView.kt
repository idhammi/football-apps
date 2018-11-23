package id.idham.footballmatchschedule.ui.teams.search

import id.idham.footballmatchschedule.data.model.Team

interface TeamsSearchView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
}