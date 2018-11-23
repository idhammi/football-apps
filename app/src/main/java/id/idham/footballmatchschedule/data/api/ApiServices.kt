package id.idham.footballmatchschedule.data.api

import id.idham.footballmatchschedule.BuildConfig
import id.idham.footballmatchschedule.data.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/all_leagues.php")
    fun getAllLeague(): Call<LeagueResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/eventspastleague.php")
    fun getLastEvent(@Query("id") id: String): Call<EventResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/eventsnextleague.php")
    fun getNextEvent(@Query("id") id: String): Call<EventResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupevent.php")
    fun getDetailEvent(@Query("id") id: String?): Call<EventResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupteam.php")
    fun getTeam(@Query("id") id: String?): Call<TeamResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/lookup_all_teams.php")
    fun getAllTeam(@Query("id") id: String?): Call<TeamResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/lookup_all_players.php")
    fun getAllPlayer(@Query("id") id: String?): Call<PlayerResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/searchevents.php")
    fun getSearchEvent(@Query("e") query: String): Call<EventSearchResponse>

    @GET("api/v1/json/${BuildConfig.TSDB_API_KEY}/searchteams.php")
    fun getSearchTeam(@Query("t") query: String): Call<TeamResponse>
}