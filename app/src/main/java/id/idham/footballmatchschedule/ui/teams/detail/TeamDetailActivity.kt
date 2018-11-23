package id.idham.footballmatchschedule.ui.teams.detail

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.drawable.ic_add_to_favorites
import id.idham.footballmatchschedule.R.drawable.ic_added_to_favorites
import id.idham.footballmatchschedule.R.id.add_to_favorite
import id.idham.footballmatchschedule.R.menu.detail_menu
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.db.FavoriteTeam
import id.idham.footballmatchschedule.data.db.database
import id.idham.footballmatchschedule.data.model.Player
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.ui.PagerAdapter
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class TeamDetailActivity : AppCompatActivity(), TeamDetailView {

    companion object {
        const val EXTRA_TEAM_ID = "extra_team_id"
        const val EXTRA_TEAM_NAME = "extra_team_name"
    }

    private lateinit var presenter: TeamDetailPresenter
    private lateinit var id: String
    private lateinit var team: Team
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        id = intent.getStringExtra(EXTRA_TEAM_ID)
        val name = intent.getStringExtra(EXTRA_TEAM_NAME)

        setSupportActionBar(toolbar)
        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val args = Bundle()
        args.putString("teamId", id)

        val overview = TeamDetailOverviewFragment()
        overview.arguments = args

        val player = TeamDetailPlayerFragment()
        player.arguments = args

        adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(overview, "Overview")
        adapter.addFragment(player, "Players")

        pager.adapter = adapter
        tablayout.setupWithViewPager(pager)

        favoriteState()

        presenter = TeamDetailPresenter(this, ApiMain().services)
        presenter.getTeamDetail(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showTeamDetail(data: List<Team>) {
        team = data[0]
        tv_year.text = team.teamFormedYear
        tv_stadium.text = team.teamStadium

        Glide.with(applicationContext).load(team.teamBadge)
            .transition(DrawableTransitionOptions.withCrossFade()).into(iv_badge)
        Glide.with(applicationContext).load(team.teamStadiumImage)
            .transition(DrawableTransitionOptions.withCrossFade()).into(iv_stadium)
    }

    override fun showPlayerList(data: List<Player>) {
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    FavoriteTeam.TABLE_FAVORITE_TEAM,
                    FavoriteTeam.TEAM_ID to team.teamId,
                    FavoriteTeam.TEAM_BADGE to team.teamBadge,
                    FavoriteTeam.TEAM_NAME to team.teamName
                )
            }
            Snackbar.make(parent_layout, getString(R.string.added_to_favorite), Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(parent_layout, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    FavoriteTeam.TABLE_FAVORITE_TEAM, "(TEAM_ID = {id})",
                    "id" to id
                )
            }
            Snackbar.make(parent_layout, getString(R.string.removed_from_favorite), Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(parent_layout, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        } else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    private fun favoriteState() {
        database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                .whereArgs("(TEAM_ID = {id})", "id" to id)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }
}
