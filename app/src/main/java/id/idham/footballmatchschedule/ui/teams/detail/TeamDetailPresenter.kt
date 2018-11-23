package id.idham.footballmatchschedule.ui.teams.detail

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.PlayerResponse
import id.idham.footballmatchschedule.data.model.TeamResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamDetailPresenter(private val view: TeamDetailView, private val services: ApiServices) {

    fun getTeamDetail(id: String?) {
        view.showLoading()
        services.getTeam(id).enqueue(object : Callback<TeamResponse> {
            override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
                if (response.code() == 200) {
                    response.body()?.teams?.let {
                        view.showTeamDetail(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }

    fun getListPlayer(id: String?) {
        view.showLoading()
        services.getAllPlayer(id).enqueue(object : Callback<PlayerResponse> {
            override fun onResponse(call: Call<PlayerResponse>, response: Response<PlayerResponse>) {
                if (response.code() == 200) {
                    response.body()?.player?.let {
                        view.showPlayerList(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<PlayerResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }

}