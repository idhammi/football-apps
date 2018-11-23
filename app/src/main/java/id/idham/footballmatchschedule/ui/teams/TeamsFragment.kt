package id.idham.footballmatchschedule.ui.teams

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.model.League
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.ui.teams.detail.TeamDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamsFragment : Fragment(), TeamsView {

    private var listTeam: MutableList<Team> = mutableListOf()
    private lateinit var rvEvents: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var presenter: TeamsPresenter
    private lateinit var adapter: TeamsAdapter
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
                            id = R.id.rv_teams
                            lparams(matchParent, wrapContent)
                            layoutManager = GridLayoutManager(context, 2)
                        }
                    }

                }
            }
        }
        return ui.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamsAdapter(context as Context, listTeam) {
            startActivity<TeamDetailActivity>(
                TeamDetailActivity.EXTRA_TEAM_ID to it.teamId,
                TeamDetailActivity.EXTRA_TEAM_NAME to it.teamName
            )
        }

        rvEvents.adapter = adapter
        presenter = TeamsPresenter(this, ApiMain().services)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLeagueId = listLeagueId[position]!!
                presenter.getTeamList(selectedLeagueId)
            }

        }

        presenter.getLeagueList()

        swipeRefresh.onRefresh {
            presenter.getTeamList(selectedLeagueId)
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

        if (context != null) {
            val spinnerAdapter = ArrayAdapter(
                context as Context,
                android.R.layout.simple_spinner_dropdown_item, listLeagueName
            )
            spinner.adapter = spinnerAdapter

            presenter.getTeamList(selectedLeagueId)
        }
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showTeamList(data: List<Team>) {
        swipeRefresh.isRefreshing = false
        listTeam.clear()
        listTeam.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
