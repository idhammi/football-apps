package id.idham.footballmatchschedule.ui.matches

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.id.*
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.util.changeTimezone
import id.idham.footballmatchschedule.util.isDatePassed
import id.idham.footballmatchschedule.util.setDateFormat
import org.jetbrains.anko.*

class MatchAdapter(private val events: List<Event>, private val listener: OnMatchClickListener) :
    RecyclerView.Adapter<MatchAdapter.ScheduleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            ScheduleUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindItem(events[position], listener)
    }

    override fun getItemCount(): Int = events.size

    class ScheduleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val eventDate: TextView = view.find(event_date)
        private val eventTime: TextView = view.find(event_time)
        private val homeName: TextView = view.find(home_name)
        private val homeScore: TextView = view.find(home_score)
        private val awayName: TextView = view.find(away_name)
        private val awayScore: TextView = view.find(away_score)
        private val matchLayout: RelativeLayout = view.find(match_layout)
        private val matchAlarm: ImageView = view.find(match_alarm)

        fun bindItem(event: Event, listener: OnMatchClickListener) {

            matchAlarm.visibility = View.GONE

            if (!event.eventTime.isNullOrEmpty()) {
                val date = setDateFormat(event.eventDate + ", " + event.eventTime)
                val time = changeTimezone(event.eventTime)

                eventDate.text = date
                eventTime.text = time

                if (!isDatePassed(event.eventDate + ", " + event.eventTime)) {
                    matchAlarm.visibility = View.VISIBLE
                }
            }

            homeName.text = event.homeName
            homeScore.text = event.homeScore
            awayName.text = event.awayName
            awayScore.text = event.awayScore

            matchLayout.setOnClickListener {
                listener.onItemClick(it, event)
            }
            matchAlarm.setOnClickListener {
                listener.onItemClick(it, event)
            }
        }
    }

    class ScheduleUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val outValue = TypedValue()
            ui.ctx.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

            return with(ui) {
                linearLayout {
                    lparams(matchParent, wrapContent) {
                        leftMargin = dip(16)
                        rightMargin = dip(16)
                        bottomMargin = dip(8)
                    }
                    backgroundColor = ContextCompat.getColor(context, android.R.color.white)
                    orientation = LinearLayout.VERTICAL

                    relativeLayout {
                        padding = dip(16)
                        backgroundResource = outValue.resourceId
                        id = match_layout

                        textView {
                            id = event_date
                            textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            gravity = Gravity.CENTER
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(10)
                        }

                        textView {
                            id = event_time
                            textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            gravity = Gravity.CENTER
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(10)
                            below(event_date)
                        }

                        imageView(R.drawable.ic_access_alarm_black_24dp) {
                            id = match_alarm
                            backgroundResource = outValue.resourceId
                        }.lparams(wrapContent, wrapContent) {
                            alignParentRight()
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            weightSum = 20f

                            textView {
                                id = home_name
                                gravity = Gravity.CENTER
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                            }.lparams {
                                width = 0
                                height = matchParent
                                weight = 7.5f
                            }

                            textView {
                                id = home_score
                                typeface = Typeface.DEFAULT_BOLD
                                textSize = sp(6).toFloat()
                                gravity = Gravity.CENTER
                            }.lparams {
                                width = 0
                                height = matchParent
                                weight = 2f
                            }

                            textView(context.getString(R.string.vs)) {
                                gravity = Gravity.CENTER
                            }.lparams {
                                width = 0
                                height = matchParent
                                weight = 1f
                            }

                            textView {
                                id = away_score
                                typeface = Typeface.DEFAULT_BOLD
                                textSize = sp(6).toFloat()
                                gravity = Gravity.CENTER
                            }.lparams {
                                width = 0
                                height = matchParent
                                weight = 2f
                            }

                            textView {
                                id = away_name
                                gravity = Gravity.CENTER
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                            }.lparams {
                                width = 0
                                height = matchParent
                                weight = 7.5f
                            }
                        }.lparams(matchParent, wrapContent) {
                            below(event_time)
                        }
                    }
                }
            }
        }
    }
}