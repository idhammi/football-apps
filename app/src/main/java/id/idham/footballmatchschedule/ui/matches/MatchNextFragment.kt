package id.idham.footballmatchschedule.ui.matches

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.id.match_alarm
import id.idham.footballmatchschedule.R.id.match_layout
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.League
import id.idham.footballmatchschedule.ui.matches.detail.MatchDetailActivity
import id.idham.footballmatchschedule.util.changeDateTimezone
import id.idham.footballmatchschedule.util.getMilliseconds
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout


class MatchNextFragment : Fragment(), MatchView, OnMatchClickListener {

    private var listEvent: MutableList<Event> = mutableListOf()
    private lateinit var rvEvents: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var presenter: MatchPresenter
    private lateinit var adapter: MatchAdapter
    private lateinit var spinner: Spinner
    private var listLeagueId: MutableList<String?> = mutableListOf()
    private var listLeagueName: MutableList<String?> = mutableListOf()
    private lateinit var selectedLeagueId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ui = UI {
            linearLayout {
                lparams(matchParent, matchParent)
                orientation = LinearLayout.VERTICAL
                backgroundColor = ContextCompat.getColor(context, R.color.colorBackground)

                spinner = spinner {
                }.lparams(matchParent, wrapContent) {
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                }

                swipeRefresh = swipeRefreshLayout {
                    setColorSchemeResources(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )

                    linearLayout {
                        lparams(matchParent, wrapContent)
                        orientation = LinearLayout.VERTICAL

                        rvEvents = recyclerView {
                            id = R.id.rv_next_match
                            lparams(matchParent, wrapContent)
                            layoutManager = LinearLayoutManager(context)
                        }
                    }

                }
            }
        }
        return ui.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchAdapter(listEvent, this)

        rvEvents.adapter = adapter
        presenter = MatchPresenter(this, ApiMain().services)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLeagueId = listLeagueId[position]!!
                presenter.getNextEventList(selectedLeagueId)
            }

        }

        presenter.getLeagueList()

        swipeRefresh.onRefresh {
            presenter.getNextEventList(selectedLeagueId)
        }
    }

    override fun showLeagueList(data: List<League>) {
        listLeagueId.clear()
        listLeagueName.clear()
        for (item in data) {
            if (item.sportName.equals("Soccer")) {
                listLeagueId.add(item.leagueId)
                listLeagueName.add(item.leagueName)
            }
        }

        selectedLeagueId = listLeagueId[0]!!

        val spinnerAdapter = ArrayAdapter(
            context as Context,
            android.R.layout.simple_spinner_dropdown_item, listLeagueName
        )
        spinner.adapter = spinnerAdapter

        presenter.getNextEventList(selectedLeagueId)

    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showLastEventList(data: List<Event>) {}

    override fun showNextEventList(data: List<Event>) {
        swipeRefresh.isRefreshing = false
        listEvent.clear()
        listEvent.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, event: Event) {
        when (view.id) {
            match_layout -> {
                startActivity<MatchDetailActivity>(MatchDetailActivity.EXTRA_EVENT_ID to event.eventId)
            }
            match_alarm -> {
                if (!event.eventTime.isNullOrEmpty()) {
                    val date = changeDateTimezone(event.eventDate + ", " + event.eventTime)
                    addToCalendar(event, date)
                }
            }
        }
    }

    private fun addToCalendar(event: Event, date: String) {
        val i = Intent(Intent.ACTION_EDIT)
        i.type = "vnd.android.cursor.item/event"
        i.putExtra(CalendarContract.Events.TITLE, event.eventName)
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getMilliseconds(date, true))
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getMilliseconds(date, false))
        startActivity(i)
    }
}
