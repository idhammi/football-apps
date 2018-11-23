package id.idham.footballmatchschedule.ui.teams.detail

import id.idham.footballmatchschedule.data.model.Player
import id.idham.footballmatchschedule.data.model.Team

interface TeamDetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamDetail(data: List<Team>)
    fun showPlayerList(data: List<Player>)
}