package id.idham.footballmatchschedule.ui.teams.search

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.TeamResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamsSearchPresenter(private val view: TeamsSearchView, private val services: ApiServices) {

    fun getSearchTeamList(query: String) {
        view.showLoading()
        services.getSearchTeam(query).enqueue(object : Callback<TeamResponse> {
            override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
                if (response.code() == 200) {
                    response.body()?.teams?.let {
                        view.showTeamList(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }
}