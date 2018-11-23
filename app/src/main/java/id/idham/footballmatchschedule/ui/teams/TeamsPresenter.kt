package id.idham.footballmatchschedule.ui.teams

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.LeagueResponse
import id.idham.footballmatchschedule.data.model.TeamResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamsPresenter(private val view: TeamsView, private val services: ApiServices) {

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

    fun getTeamList(id: String) {
        view.showLoading()
        services.getAllTeam(id).enqueue(object : Callback<TeamResponse> {
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