package id.idham.footballmatchschedule.ui.matches.search

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.EventSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchSearchPresenter(private val view: MatchSearchView, private val services: ApiServices) {

    fun getSearchEventList(query: String) {
        view.showLoading()
        services.getSearchEvent(query).enqueue(object : Callback<EventSearchResponse> {
            override fun onResponse(call: Call<EventSearchResponse>, response: Response<EventSearchResponse>) {
                if (response.code() == 200) {
                    response.body()?.event?.let {
                        view.showEventList(it)
                    }
                }

                view.hideLoading()
            }

            override fun onFailure(call: Call<EventSearchResponse>, t: Throwable) {
                view.hideLoading()
            }
        })
    }
}