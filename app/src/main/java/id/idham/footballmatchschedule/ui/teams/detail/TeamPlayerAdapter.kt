package id.idham.footballmatchschedule.ui.teams.detail

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import id.idham.footballmatchschedule.R.color.colorPrimaryDark
import id.idham.footballmatchschedule.R.drawable.ic_player_grey
import id.idham.footballmatchschedule.R.id.*
import id.idham.footballmatchschedule.data.model.Player
import org.jetbrains.anko.*

class TeamPlayerAdapter(
    private val context: Context,
    private val players: List<Player>,
    private val listener: (Player) -> Unit
) :
    RecyclerView.Adapter<TeamPlayerAdapter.ScheduleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            ScheduleUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindItem(context, players[position], listener)
    }

    override fun getItemCount(): Int = players.size

    class ScheduleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val playerCutout: ImageView = view.find(player_coutout)
        private val playerName: TextView = view.find(player_name)
        private val playerPos: TextView = view.find(player_pos)

        fun bindItem(context: Context, player: Player, listener: (Player) -> Unit) {
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            Glide.with(context).load(player.playerCutout)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .apply(RequestOptions().placeholder(ic_player_grey))
                .into(playerCutout)
            playerName.text = player.playerName
            playerPos.text = player.playerPos
            view.setOnClickListener { listener(player) }
        }
    }

    class ScheduleUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val outValue = TypedValue()
            ui.ctx.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

            return with(ui) {
                linearLayout {
                    lparams(matchParent, wrapContent)
                    padding = dip(16)
                    orientation = LinearLayout.HORIZONTAL
                    backgroundResource = outValue.resourceId

                    imageView {
                        id = player_coutout
                    }.lparams(dip(50), dip(50))

                    textView {
                        id = player_name
                        textSize = 14f
                        textColor = ContextCompat.getColor(context, colorPrimaryDark)
                    }.lparams(0, wrapContent) {
                        margin = dip(15)
                        weight = 1.3f
                    }

                    textView {
                        id = player_pos
                        textSize = 14f
                        gravity = Gravity.END
                    }.lparams(0, wrapContent) {
                        margin = dip(15)
                        weight = 0.7f
                    }
                }
            }
        }
    }
}