package id.idham.footballmatchschedule.ui.favorites

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
import android.widget.LinearLayout
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.id.match_alarm
import id.idham.footballmatchschedule.R.id.match_layout
import id.idham.footballmatchschedule.data.db.FavoriteMatch
import id.idham.footballmatchschedule.data.db.database
import id.idham.footballmatchschedule.ui.matches.detail.MatchDetailActivity
import id.idham.footballmatchschedule.util.changeDateTimezone
import id.idham.footballmatchschedule.util.getMilliseconds
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteMatchFragment : Fragment(), AnkoComponent<Context>, OnFavoriteClickListener {

    private var favorites: MutableList<FavoriteMatch> = mutableListOf()
    private lateinit var rvEvents: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adapter: FavoriteMatchAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoriteMatchAdapter(favorites, this)
        rvEvents.adapter = adapter
        showFavorite()

        swipeRefresh.onRefresh {
            favorites.clear()
            showFavorite()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(this@FavoriteMatchFragment.context as Context))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            backgroundColor = ContextCompat.getColor(context, R.color.colorBackground)
            topPadding = dip(16)

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
                        lparams(matchParent, wrapContent)
                        layoutManager = LinearLayoutManager(context)
                    }
                }

            }
        }
    }

    private fun showFavorite() {
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteMatch.TABLE_FAVORITE_MATCH)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(view: View, favorite: FavoriteMatch) {
        when (view.id) {
            match_layout -> {
                startActivity<MatchDetailActivity>(MatchDetailActivity.EXTRA_EVENT_ID to favorite.eventId)
            }
            match_alarm -> {
                if (!favorite.eventTime.isNullOrEmpty()) {
                    val date = changeDateTimezone(favorite.eventDate + ", " + favorite.eventTime)
                    addToCalendar(favorite, date)
                }
            }
        }
    }

    private fun addToCalendar(favorite: FavoriteMatch, date: String) {
        val i = Intent(Intent.ACTION_EDIT)
        i.type = "vnd.android.cursor.item/event"
        i.putExtra(CalendarContract.Events.TITLE, favorite.eventName)
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getMilliseconds(date, true))
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getMilliseconds(date, false))
        startActivity(i)
    }
}
