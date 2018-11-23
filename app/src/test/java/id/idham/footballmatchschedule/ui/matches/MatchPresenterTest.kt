package id.idham.footballmatchschedule.ui.matches

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.EventResponse
import id.idham.footballmatchschedule.data.model.League
import id.idham.footballmatchschedule.data.model.LeagueResponse
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

class MatchPresenterTest {

    private lateinit var mPresenter: MatchPresenter
    @Mock
    private lateinit var mView: MatchView
    @Mock
    private lateinit var mServices: ApiServices
    @Mock
    private lateinit var mCallLeague: Call<LeagueResponse>
    @Mock
    private lateinit var mCallEvent: Call<EventResponse>
    @Captor
    private lateinit var mResLeague: ArgumentCaptor<Callback<LeagueResponse>>
    @Captor
    private lateinit var mResEvent: ArgumentCaptor<Callback<EventResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = MatchPresenter(mView, mServices)
    }

    @Test
    fun getLeagueList() {
        val list: MutableList<League> = mutableListOf()
        val response = LeagueResponse(list)
        val res: Response<LeagueResponse> = Response.success(response)

        `when`(mServices.getAllLeague()).thenReturn(mCallLeague)

        mPresenter.getLeagueList()

        verify(mView).showLoading()
        verify(mCallLeague).enqueue(mResLeague.capture())

        mResLeague.value.onResponse(mCallLeague, res)

        verify(mView).showLeagueList(list)
        verify(mView).hideLoading()
    }

    @Test
    fun getLastEventList() {
        val id = "4328"
        val list: MutableList<Event> = mutableListOf()
        val response = EventResponse(list)
        val res: Response<EventResponse> = Response.success(response)

        `when`(mServices.getLastEvent(id)).thenReturn(mCallEvent)

        mPresenter.getLastEventList(id)

        verify(mView).showLoading()
        verify(mCallEvent).enqueue(mResEvent.capture())

        mResEvent.value.onResponse(mCallEvent, res)

        verify(mView).showLastEventList(list)
        verify(mView).hideLoading()
    }

    @Test
    fun getNextEventList() {
        val id = "4328"
        val list: MutableList<Event> = mutableListOf()
        val response = EventResponse(list)
        val res: Response<EventResponse> = Response.success(response)

        `when`(mServices.getNextEvent(id)).thenReturn(mCallEvent)

        mPresenter.getNextEventList(id)

        verify(mView).showLoading()
        verify(mCallEvent).enqueue(mResEvent.capture())

        mResEvent.value.onResponse(mCallEvent, res)

        verify(mView).showNextEventList(list)
        verify(mView).hideLoading()
    }
}