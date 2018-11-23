package id.idham.footballmatchschedule.ui.teams.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.ui.teams.TeamsAdapter
import id.idham.footballmatchschedule.ui.teams.detail.TeamDetailActivity
import id.idham.footballmatchschedule.util.invisible
import id.idham.footballmatchschedule.util.visible
import kotlinx.android.synthetic.main.activity_teams_search.*
import org.jetbrains.anko.startActivity

class TeamsSearchActivity : AppCompatActivity(), TeamsSearchView, SearchView.OnQueryTextListener {

    private lateinit var presenter: TeamsSearchPresenter
    private lateinit var adapter: TeamsAdapter
    private var listEvent: MutableList<Team> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = TeamsAdapter(this, listEvent) {
            startActivity<TeamDetailActivity>(
                TeamDetailActivity.EXTRA_TEAM_ID to it.teamId,
                TeamDetailActivity.EXTRA_TEAM_NAME to it.teamName
            )
        }
        rv_search.layoutManager = GridLayoutManager(applicationContext, 2)
        rv_search.adapter = adapter
        presenter = TeamsSearchPresenter(this, ApiMain().services)

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
        presenter.getSearchTeamList(newText)
        return false
    }

    override fun showLoading() {
        progress.visible()
    }

    override fun hideLoading() {
        progress.invisible()
    }

    override fun showTeamList(data: List<Team>) {
        listEvent.clear()
        for (item in data) {
            if (item.teamSport.equals("Soccer")) {
                listEvent.add(item)
            }
        }
        adapter.notifyDataSetChanged()
    }
}
