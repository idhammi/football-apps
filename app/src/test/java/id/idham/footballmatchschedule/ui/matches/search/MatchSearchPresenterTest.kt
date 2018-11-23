package id.idham.footballmatchschedule.ui.matches.search

import id.idham.footballmatchschedule.data.api.ApiServices
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.EventSearchResponse
import org.junit.Before
import org.junit.Test
import org.mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchSearchPresenterTest {

    private lateinit var mPresenter: MatchSearchPresenter
    @Mock
    private lateinit var mView: MatchSearchView
    @Mock
    private lateinit var mServices: ApiServices
    @Mock
    private lateinit var mCall: Call<EventSearchResponse>
    @Captor
    private lateinit var mResponse: ArgumentCaptor<Callback<EventSearchResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = MatchSearchPresenter(mView, mServices)
    }

    @Test
    fun getSearchEventList() {
        val query = "Barcelona"
        val list: MutableList<Event> = mutableListOf()
        val response = EventSearchResponse(list)
        val res: Response<EventSearchResponse> = Response.success(response)

        Mockito.`when`(mServices.getSearchEvent(query)).thenReturn(mCall)

        mPresenter.getSearchEventList(query)

        Mockito.verify(mView).showLoading()
        Mockito.verify(mCall).enqueue(mResponse.capture())

        mResponse.value.onResponse(mCall, res)

        Mockito.verify(mView).showEventList(list)
        Mockito.verify(mView).hideLoading()
    }
}