package id.idham.footballmatchschedule.ui.teams

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.idham.footballmatchschedule.R.id.team_badge
import id.idham.footballmatchschedule.R.id.team_name
import id.idham.footballmatchschedule.data.model.Team
import org.jetbrains.anko.*

class TeamsAdapter(private val context: Context, private val events: List<Team>, private val listener: (Team) -> Unit) :
    RecyclerView.Adapter<TeamsAdapter.ScheduleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            ScheduleUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindItem(context, events[position], listener)
    }

    override fun getItemCount(): Int = events.size

    class ScheduleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val teamBadge: ImageView = view.find(team_badge)
        private val teamName: TextView = view.find(team_name)

        fun bindItem(context: Context, team: Team, listener: (Team) -> Unit) {
            Glide.with(context).load(team.teamBadge).transition(DrawableTransitionOptions.withCrossFade())
                .into(teamBadge)
            teamName.text = team.teamName
            view.setOnClickListener { listener(team) }
        }
    }

    class ScheduleUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val outValue = TypedValue()
            ui.ctx.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

            return with(ui) {
                relativeLayout {
                    lparams(matchParent, dip(220)) {
                        margin = dip(1)
                    }
                    backgroundResource = outValue.resourceId

                    imageView {
                        id = team_badge
                        padding = dip(24)
                    }.lparams(matchParent, wrapContent)

                    textView {
                        id = team_name
                        textSize = 16f
                        leftPadding = dip(16)
                        topPadding = dip(8)
                        rightPadding = dip(16)
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    }.lparams(matchParent, wrapContent) {
                        margin = dip(15)
                        alignParentBottom()
                    }
                }
            }
        }
    }
}