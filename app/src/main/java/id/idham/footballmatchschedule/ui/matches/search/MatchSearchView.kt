package id.idham.footballmatchschedule.ui.matches.search

import id.idham.footballmatchschedule.data.model.Event

interface MatchSearchView {
    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<Event>)
}