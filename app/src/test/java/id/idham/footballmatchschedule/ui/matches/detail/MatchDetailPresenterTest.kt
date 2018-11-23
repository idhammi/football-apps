package id.idham.footballmatchschedule.ui.matches.detail

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.EventResponse
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.data.model.TeamResponse
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchDetailPresenterTest {

    private lateinit var mPresenter: MatchDetailPresenter
    @Mock
    private lateinit var mView: MatchDetailView
    @Mock
    private lateinit var mServices: ApiServices
    @Mock
    private lateinit var mCallEvent: Call<EventResponse>
    @Mock
    private lateinit var mCallTeam: Call<TeamResponse>
    @Captor
    private lateinit var mResEvent: ArgumentCaptor<Callback<EventResponse>>
    @Captor
    private lateinit var mResTeam: ArgumentCaptor<Callback<TeamResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = MatchDetailPresenter(mView, mServices)
    }

    @Test
    fun getDetailEvent() {
        val id = "441613"
        val list: MutableList<Event> = mutableListOf()
        val response = EventResponse(list)
        val res: Response<EventResponse> = Response.success(response)

        `when`(mServices.getDetailEvent(id)).thenReturn(mCallEvent)

        mPresenter.getDetailEvent(id)

        verify(mView).showLoading()
        verify(mCallEvent).enqueue(mResEvent.capture())

        mResEvent.value.onResponse(mCallEvent, res)

        verify(mView).showEventDetail(list)
        verify(mView).hideLoading()
    }

    @Test
    fun getHomeBadge() {
        val id = "133604"
        val list: MutableList<Team> = mutableListOf()
        val response = TeamResponse(list)
        val res: Response<TeamResponse> = Response.success(response)

        `when`(mServices.getTeam(id)).thenReturn(mCallTeam)

        mPresenter.getHomeBadge(id)

        verify(mCallTeam).enqueue(mResTeam.capture())
        mResTeam.value.onResponse(mCallTeam, res)
        verify(mView).showHomeBadge(list)
    }

    @Test
    fun getAwayBadge() {
        val id = "133604"
        val list: MutableList<Team> = mutableListOf()
        val response = TeamResponse(list)
        val res: Response<TeamResponse> = Response.success(response)

        `when`(mServices.getTeam(id)).thenReturn(mCallTeam)

        mPresenter.getAwayBadge(id)

        verify(mCallTeam).enqueue(mResTeam.capture())
        mResTeam.value.onResponse(mCallTeam, res)
        verify(mView).showAwayBadge(list)
    }
}