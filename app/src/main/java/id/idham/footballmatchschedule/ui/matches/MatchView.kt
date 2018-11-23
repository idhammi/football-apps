package id.idham.footballmatchschedule.ui.matches

import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.League

interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(data: List<League>)
    fun showLastEventList(data: List<Event>)
    fun showNextEventList(data: List<Event>)
}