package id.idham.footballmatchschedule.ui.teams.detail

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.model.Player
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.ui.teams.player.PlayerActivity
import id.idham.footballmatchschedule.util.invisible
import id.idham.footballmatchschedule.util.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity

class TeamDetailPlayerFragment : Fragment(), TeamDetailView {

    private var listPlayer: MutableList<Player> = mutableListOf()
    private lateinit var progress: ProgressBar
    private lateinit var presenter: TeamDetailPresenter
    private lateinit var rvPlayers: RecyclerView
    private lateinit var adapter: TeamPlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ui = UI {
            linearLayout {
                lparams(matchParent, matchParent)
                orientation = LinearLayout.VERTICAL
                backgroundColor = ContextCompat.getColor(context, R.color.colorBackground)

                relativeLayout {
                    lparams(matchParent, wrapContent)

                    progress = progressBar {
                        padding = dip(16)
                    }.lparams(matchParent, wrapContent)

                    rvPlayers = recyclerView {
                        lparams(matchParent, matchParent)
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
        return ui.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamPlayerAdapter(context as Context, listPlayer) {
            startActivity<PlayerActivity>(PlayerActivity.EXTRA_PLAYER to it)
        }

        rvPlayers.adapter = adapter

        if (arguments != null) {
            val teamId = arguments!!.getString("teamId")
            presenter = TeamDetailPresenter(this, ApiMain().services)
            presenter.getListPlayer(teamId)
        }
    }

    override fun showLoading() {
        progress.visible()
    }

    override fun hideLoading() {
        progress.invisible()
    }

    override fun showTeamDetail(data: List<Team>) {
    }

    override fun showPlayerList(data: List<Player>) {
        listPlayer.clear()
        listPlayer.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
