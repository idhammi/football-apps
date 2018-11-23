package id.idham.footballmatchschedule.ui.teams.search

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.data.model.TeamResponse
import org.junit.Test

import org.junit.Before
import org.mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamsSearchPresenterTest {

    private lateinit var mPresenter: TeamsSearchPresenter
    @Mock
    private lateinit var mView: TeamsSearchView
    @Mock
    private lateinit var mServices: ApiServices
    @Mock
    private lateinit var mCall: Call<TeamResponse>
    @Captor
    private lateinit var mResponse: ArgumentCaptor<Callback<TeamResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = TeamsSearchPresenter(mView, mServices)
    }

    @Test
    fun getSearchTeamList() {
        val query = "Barcelona"
        val list: MutableList<Team> = mutableListOf()
        val response = TeamResponse(list)
        val res: Response<TeamResponse> = Response.success(response)

        Mockito.`when`(mServices.getSearchTeam(query)).thenReturn(mCall)

        mPresenter.getSearchTeamList(query)

        Mockito.verify(mView).showLoading()
        Mockito.verify(mCall).enqueue(mResponse.capture())

        mResponse.value.onResponse(mCall, res)

        Mockito.verify(mView).showTeamList(list)
        Mockito.verify(mView).hideLoading()
    }
}