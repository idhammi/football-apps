package id.idham.footballmatchschedule.ui.matches.detail

import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.Team

interface MatchDetailView {
    fun showLoading()
    fun hideLoading()
    fun showEventDetail(data: List<Event>?)
    fun showHomeBadge(data: List<Team>?)
    fun showAwayBadge(data: List<Team>?)
}