package id.idham.footballmatchschedule.ui.teams

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.League
import id.idham.footballmatchschedule.data.model.LeagueResponse
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.data.model.TeamResponse
import org.junit.Before
import org.junit.Test
import org.mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamsPresenterTest {

    private lateinit var mPresenter: TeamsPresenter
    @Mock
    private lateinit var mView: TeamsView
    @Mock
    private lateinit var mServices: ApiServices
    @Mock
    private lateinit var mCallLeague: Call<LeagueResponse>
    @Mock
    private lateinit var mCallTeam: Call<TeamResponse>
    @Captor
    private lateinit var mResLeague: ArgumentCaptor<Callback<LeagueResponse>>
    @Captor
    private lateinit var mResTeam: ArgumentCaptor<Callback<TeamResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = TeamsPresenter(mView, mServices)
    }

    @Test
    fun getLeagueList() {
        val list: MutableList<League> = mutableListOf()
        val response = LeagueResponse(list)
        val res: Response<LeagueResponse> = Response.success(response)

        Mockito.`when`(mServices.getAllLeague()).thenReturn(mCallLeague)

        mPresenter.getLeagueList()

        Mockito.verify(mView).showLoading()
        Mockito.verify(mCallLeague).enqueue(mResLeague.capture())

        mResLeague.value.onResponse(mCallLeague, res)

        Mockito.verify(mView).showLeagueList(list)
        Mockito.verify(mView).hideLoading()
    }

    @Test
    fun getTeamList() {
        val id = "4328"
        val list: MutableList<Team> = mutableListOf()
        val response = TeamResponse(list)
        val res: Response<TeamResponse> = Response.success(response)

        Mockito.`when`(mServices.getAllTeam(id)).thenReturn(mCallTeam)

        mPresenter.getTeamList(id)

        Mockito.verify(mView).showLoading()
        Mockito.verify(mCallTeam).enqueue(mResTeam.capture())

        mResTeam.value.onResponse(mCallTeam, res)

        Mockito.verify(mView).showTeamList(list)
        Mockito.verify(mView).hideLoading()
    }
}