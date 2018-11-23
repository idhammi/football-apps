package id.idham.footballmatchschedule.ui.matches

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.EventResponse
import id.idham.footballmatchschedule.data.model.LeagueResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchPresenter(private val view: MatchView, private val services: ApiServices) {

    fun getLeagueList() {
        view.showLoading()
        services.getAllLeague().enqueue(object : Callback<LeagueResponse> {
            override fun onResponse(call: Call<LeagueResponse>, response: Response<LeagueResponse>) {
                if (response.code() == 200) {
                    response.body()?.leagues?.let {
                        view.showLeagueList(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<LeagueResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }

    fun getLastEventList(id: String) {
        view.showLoading()
        services.getLastEvent(id).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.code() == 200) {
                    response.body()?.events?.let {
                        view.showLastEventList(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }

    fun getNextEventList(id: String) {
        view.showLoading()
        services.getNextEvent(id).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.code() == 200) {
                    response.body()?.events?.let {
                        view.showNextEventList(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }
}