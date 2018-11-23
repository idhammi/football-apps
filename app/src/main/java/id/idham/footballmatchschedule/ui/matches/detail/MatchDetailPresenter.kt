package id.idham.footballmatchschedule.ui.matches.detail

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.EventResponse
import id.idham.footballmatchschedule.data.model.TeamResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchDetailPresenter(private val view: MatchDetailView, private val services: ApiServices) {

    fun getDetailEvent(id: String?) {
        view.showLoading()
        services.getDetailEvent(id).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.code() == 200) {
                    response.body()?.events?.let {
                        view.showEventDetail(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }

    fun getHomeBadge(id: String?) {
        services.getTeam(id).enqueue(object : Callback<TeamResponse> {
            override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
                if (response.code() == 200) {
                    response.body()?.teams?.let {
                        view.showHomeBadge(it)
                    }
                }
            }

            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {

            }
        })
    }

    fun getAwayBadge(id: String?) {
        services.getTeam(id).enqueue(object : Callback<TeamResponse> {
            override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
                if (response.code() == 200) {
                    response.body()?.teams?.let {
                        view.showAwayBadge(it)
                    }
                }
            }

            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
            }
        })
    }

}