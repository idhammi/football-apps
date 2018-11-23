package id.idham.footballmatchschedule.ui.teams.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.model.Player
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.util.invisible
import id.idham.footballmatchschedule.util.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView

class TeamDetailOverviewFragment : Fragment(), TeamDetailView {

    private lateinit var tvTeamDesc: TextView
    private lateinit var progress: ProgressBar
    private lateinit var presenter: TeamDetailPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ui = UI {
            nestedScrollView {
                lparams(matchParent, matchParent)
                backgroundColor = ContextCompat.getColor(context, R.color.colorBackground)

                relativeLayout {
                    lparams(matchParent, wrapContent)

                    progress = progressBar {
                        padding = dip(16)
                    }.lparams(matchParent, wrapContent)

                    tvTeamDesc = textView {
                        padding = dip(16)
                    }.lparams(matchParent, wrapContent)
                }
            }
        }
        return ui.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val teamId = arguments!!.getString("teamId")
            presenter = TeamDetailPresenter(this, ApiMain().services)
            presenter.getTeamDetail(teamId)
        }
    }

    override fun showLoading() {
        progress.visible()
    }

    override fun hideLoading() {
        progress.invisible()
    }

    override fun showTeamDetail(data: List<Team>) {
        tvTeamDesc.text = data[0].teamDesc
    }

    override fun showPlayerList(data: List<Player>) {
    }
}
