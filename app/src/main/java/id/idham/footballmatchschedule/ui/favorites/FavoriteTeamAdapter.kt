package id.idham.footballmatchschedule.ui.favorites

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
import id.idham.footballmatchschedule.data.db.FavoriteTeam
import org.jetbrains.anko.*

class FavoriteTeamAdapter(
    private val context: Context,
    private val favorites: List<FavoriteTeam>,
    private val listener: (FavoriteTeam) -> Unit
) :
    RecyclerView.Adapter<FavoriteTeamAdapter.ScheduleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(FavoriteUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindItem(context, favorites[position], listener)
    }

    override fun getItemCount(): Int = favorites.size

    class ScheduleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val teamBadge: ImageView = view.find(team_badge)
        private val teamName: TextView = view.find(team_name)

        fun bindItem(context: Context, favorite: FavoriteTeam, listener: (FavoriteTeam) -> Unit) {
            Glide.with(context).load(favorite.teamBadge).transition(DrawableTransitionOptions.withCrossFade())
                .into(teamBadge)
            teamName.text = favorite.teamName
            view.setOnClickListener { listener(favorite) }
        }
    }

    class FavoriteUI : AnkoComponent<ViewGroup> {
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