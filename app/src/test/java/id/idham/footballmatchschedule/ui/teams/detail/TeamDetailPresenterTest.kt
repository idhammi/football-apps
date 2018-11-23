package id.idham.footballmatchschedule.ui.teams.detail

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.Player
import id.idham.footballmatchschedule.data.model.PlayerResponse
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.data.model.TeamResponse
import org.junit.Before
import org.junit.Test
import org.mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamDetailPresenterTest {

    private lateinit var mPresenter: TeamDetailPresenter
    @Mock
    private lateinit var mView: TeamDetailView
    @Mock
    private lateinit var mServices: ApiServices
    @Mock
    private lateinit var mCallPlayer: Call<PlayerResponse>
    @Mock
    private lateinit var mCallTeam: Call<TeamResponse>
    @Captor
    private lateinit var mResPlayer: ArgumentCaptor<Callback<PlayerResponse>>
    @Captor
    private lateinit var mResTeam: ArgumentCaptor<Callback<TeamResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = TeamDetailPresenter(mView, mServices)
    }

    @Test
    fun getTeamDetail() {
        val id = "133604"
        val list: MutableList<Team> = mutableListOf()
        val response = TeamResponse(list)
        val res: Response<TeamResponse> = Response.success(response)

        Mockito.`when`(mServices.getTeam(id)).thenReturn(mCallTeam)

        mPresenter.getTeamDetail(id)

        Mockito.verify(mView).showLoading()
        Mockito.verify(mCallTeam).enqueue(mResTeam.capture())

        mResTeam.value.onResponse(mCallTeam, res)

        Mockito.verify(mView).showTeamDetail(list)
        Mockito.verify(mView).hideLoading()
    }

    @Test
    fun getListPlayer() {
        val id = "133604"
        val list: MutableList<Player> = mutableListOf()
        val response = PlayerResponse(list)
        val res: Response<PlayerResponse> = Response.success(response)

        Mockito.`when`(mServices.getAllPlayer(id)).thenReturn(mCallPlayer)

        mPresenter.getListPlayer(id)

        Mockito.verify(mView).showLoading()
        Mockito.verify(mCallPlayer).enqueue(mResPlayer.capture())

        mResPlayer.value.onResponse(mCallPlayer, res)

        Mockito.verify(mView).showPlayerList(list)
        Mockito.verify(mView).hideLoading()
    }
}