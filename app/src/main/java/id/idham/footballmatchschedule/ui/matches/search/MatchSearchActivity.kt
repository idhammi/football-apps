package id.idham.footballmatchschedule.ui.matches.search

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.ui.matches.MatchAdapter
import id.idham.footballmatchschedule.ui.matches.OnMatchClickListener
import id.idham.footballmatchschedule.ui.matches.detail.MatchDetailActivity
import id.idham.footballmatchschedule.util.changeDateTimezone
import id.idham.footballmatchschedule.util.getMilliseconds
import id.idham.footballmatchschedule.util.invisible
import id.idham.footballmatchschedule.util.visible
import kotlinx.android.synthetic.main.activity_match_search.*
import org.jetbrains.anko.startActivity

class MatchSearchActivity : AppCompatActivity(), MatchSearchView, OnMatchClickListener, SearchView.OnQueryTextListener {

    private lateinit var presenter: MatchSearchPresenter
    private lateinit var adapter: MatchAdapter
    private var listEvent: MutableList<Event> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = MatchAdapter(listEvent, this)
        rv_search.layoutManager = LinearLayoutManager(applicationContext)
        rv_search.adapter = adapter
        presenter = MatchSearchPresenter(this, ApiMain().services)

        searchView.setOnQueryTextListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        presenter.getSearchEventList(newText)
        return false
    }

    override fun showLoading() {
        progress.visible()
    }

    override fun hideLoading() {
        progress.invisible()
    }

    override fun showEventList(data: List<Event>) {
        listEvent.clear()
        for (item in data) {
            if (item.eventSport.equals("Soccer")) {
                listEvent.add(item)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, event: Event) {
        when (view.id) {
            R.id.match_layout -> {
                startActivity<MatchDetailActivity>(MatchDetailActivity.EXTRA_EVENT_ID to event.eventId)
            }
            R.id.match_alarm -> {
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
