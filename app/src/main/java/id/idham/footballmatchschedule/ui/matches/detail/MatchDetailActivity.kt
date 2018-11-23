package id.idham.footballmatchschedule.ui.matches.detail

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.drawable.ic_add_to_favorites
import id.idham.footballmatchschedule.R.drawable.ic_added_to_favorites
import id.idham.footballmatchschedule.R.id.add_to_favorite
import id.idham.footballmatchschedule.R.menu.detail_menu
import id.idham.footballmatchschedule.data.api.ApiMain
import id.idham.footballmatchschedule.data.db.FavoriteMatch
import id.idham.footballmatchschedule.data.db.database
import id.idham.footballmatchschedule.data.model.Event
import id.idham.footballmatchschedule.data.model.Team
import id.idham.footballmatchschedule.util.changeTimezone
import id.idham.footballmatchschedule.util.invisible
import id.idham.footballmatchschedule.util.setDateFormat
import id.idham.footballmatchschedule.util.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class MatchDetailActivity : AppCompatActivity(), MatchDetailView {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event"
    }

    private lateinit var detailUI: DetailUI
    private lateinit var presenter: MatchDetailPresenter
    private lateinit var id: String
    private lateinit var event: Event
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.match_detail)

        id = intent.getStringExtra(MatchDetailActivity.EXTRA_EVENT_ID)

        detailUI = MatchDetailActivity.DetailUI()
        detailUI.setContentView(this)
        favoriteState()

        presenter = MatchDetailPresenter(this, ApiMain().services)
        presenter.getDetailEvent(id)
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
        detailUI.progress.visible()
        detailUI.layout.invisible()
    }

    override fun hideLoading() {
        detailUI.progress.invisible()
        detailUI.layout.visible()
    }

    override fun showEventDetail(data: List<Event>?) {
        if (data != null) {
            event = data[0]
            val date = setDateFormat(event.eventDate + ", " + event.eventTime)
            val time = changeTimezone(event.eventTime)
            detailUI.eventDate.text = date
            detailUI.eventTime.text = time
            detailUI.homeName.text = event.homeName
            detailUI.homeScore.text = event.homeScore
            detailUI.homeGoals.text = event.homeGoalDetails
            detailUI.homeShots.text = event.homeShots
            detailUI.homeKeeper.text = event.homeLineupGoalKeeper
            detailUI.homeDefense.text = event.homeLineupDefense
            detailUI.homeMidfield.text = event.homeLineupMidfield
            detailUI.homeForward.text = event.homeLineupForward
            detailUI.homeSubs.text = event.homeLineupSubtitutes
            detailUI.awayName.text = event.awayName
            detailUI.awayScore.text = event.awayScore
            detailUI.awayGoals.text = event.awayGoalDetails
            detailUI.awayShots.text = event.awayShots
            detailUI.awayKeeper.text = event.awayLineupGoalKeeper
            detailUI.awayDefense.text = event.awayLineupDefense
            detailUI.awayMidfield.text = event.awayLineupMidfield
            detailUI.awayForward.text = event.awayLineupForward
            detailUI.awaySubs.text = event.awayLineupSubtitutes

            presenter.getHomeBadge(event.homeId)
            presenter.getAwayBadge(event.awayId)
        }
    }

    override fun showHomeBadge(data: List<Team>?) {
        val badge = data?.get(0)?.teamBadge
        Glide.with(applicationContext)
            .load(badge).transition(DrawableTransitionOptions.withCrossFade())
            .into(detailUI.ivHome)
    }

    override fun showAwayBadge(data: List<Team>?) {
        val badge = data?.get(0)?.teamBadge
        Glide.with(applicationContext)
            .load(badge).transition(DrawableTransitionOptions.withCrossFade())
            .into(detailUI.ivAway)
    }

    class DetailUI : AnkoComponent<MatchDetailActivity> {

        lateinit var progress: ProgressBar
        lateinit var ivHome: ImageView
        lateinit var ivAway: ImageView
        lateinit var layout: LinearLayout
        lateinit var eventDate: TextView
        lateinit var eventTime: TextView
        lateinit var homeName: TextView
        lateinit var homeScore: TextView
        lateinit var homeGoals: TextView
        lateinit var homeShots: TextView
        lateinit var homeKeeper: TextView
        lateinit var homeDefense: TextView
        lateinit var homeMidfield: TextView
        lateinit var homeForward: TextView
        lateinit var homeSubs: TextView
        lateinit var awayName: TextView
        lateinit var awayScore: TextView
        lateinit var awayGoals: TextView
        lateinit var awayShots: TextView
        lateinit var awayKeeper: TextView
        lateinit var awayDefense: TextView
        lateinit var awayMidfield: TextView
        lateinit var awayForward: TextView
        lateinit var awaySubs: TextView

        override fun createView(ui: AnkoContext<MatchDetailActivity>) = with(ui) {
            scrollView {
                lparams(matchParent, matchParent)

                relativeLayout {
                    lparams(matchParent, matchParent)

                    progress = progressBar {
                        padding = dip(16)
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                    }

                    layout = linearLayout {
                        orientation = LinearLayout.VERTICAL

                        eventDate = textView {
                            gravity = Gravity.CENTER
                            topPadding = dip(16)
                            textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                        }.lparams(width = matchParent, height = wrapContent)

                        eventTime = textView {
                            gravity = Gravity.CENTER
                            topPadding = dip(8)
                            textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            leftPadding = dip(16)
                            rightPadding = dip(16)
                            bottomPadding = dip(16)
                            weightSum = 20f

                            linearLayout {
                                orientation = LinearLayout.VERTICAL

                                ivHome = imageView {
                                }.lparams(width = dip(80), height = dip(80)) {
                                    gravity = Gravity.CENTER
                                }
                                homeName = textView {
                                    gravity = Gravity.CENTER
                                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                                }.lparams(width = matchParent, height = matchParent)
                            }.lparams(width = dip(0), height = matchParent) {
                                weight = 7.5f
                            }

                            homeScore = textView {
                                gravity = Gravity.CENTER
                                textSize = 24f
                                typeface = Typeface.DEFAULT_BOLD
                            }.lparams(width = dip(0), height = matchParent) {
                                weight = 2f
                            }

                            textView(context.getString(R.string.vs)) {
                                gravity = Gravity.CENTER
                                textSize = 16f
                            }.lparams(width = dip(0), height = matchParent) {
                                weight = 1f
                            }

                            awayScore = textView {
                                gravity = Gravity.CENTER
                                textSize = 24f
                                typeface = Typeface.DEFAULT_BOLD
                            }.lparams(width = dip(0), height = matchParent) {
                                weight = 2f
                            }

                            linearLayout {
                                orientation = LinearLayout.VERTICAL

                                ivAway = imageView {
                                }.lparams(width = dip(80), height = dip(80)) {
                                    gravity = Gravity.CENTER
                                }
                                awayName = textView {
                                    gravity = Gravity.CENTER
                                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                                }.lparams(width = matchParent, height = matchParent)
                            }.lparams(width = dip(0), height = matchParent) {
                                weight = 7.5f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        view {
                            backgroundResource = android.R.color.darker_gray
                        }.lparams(width = matchParent, height = dip(1))

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(16)

                            homeGoals = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.goals)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.5f
                            }

                            awayGoals = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(10)
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            homeShots = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.shots)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.5f
                            }

                            awayShots = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        view {
                            backgroundResource = android.R.color.darker_gray
                        }.lparams(width = matchParent, height = dip(1))

                        textView(context.getString(R.string.lineups)) {
                            gravity = Gravity.CENTER
                            padding = dip(10)
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(10)
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            homeKeeper = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.goal_keeper)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.7f
                            }

                            awayKeeper = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(10)
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            homeDefense = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.defense)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.6f
                            }

                            awayDefense = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(10)
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            homeMidfield = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.midfield)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.6f
                            }

                            awayMidfield = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(10)
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            homeForward = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.forward)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.6f
                            }

                            awayForward = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(10)
                            leftPadding = dip(16)
                            rightPadding = dip(16)

                            homeSubs = textView {
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                            textView(context.getString(R.string.substitution)) {
                                gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 0.6f
                            }

                            awaySubs = textView {
                                gravity = Gravity.END
                            }.lparams(width = dip(0), height = wrapContent) {
                                weight = 1f
                            }

                        }.lparams(width = matchParent, height = wrapContent)
                    }
                }

            }
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    FavoriteMatch.TABLE_FAVORITE_MATCH,
                    FavoriteMatch.EVENT_ID to event.eventId,
                    FavoriteMatch.EVENT_NAME to event.eventName,
                    FavoriteMatch.EVENT_DATE to event.eventDate,
                    FavoriteMatch.EVENT_TIME to event.eventTime,
                    FavoriteMatch.HOME_NAME to event.homeName,
                    FavoriteMatch.HOME_SCORE to event.homeScore,
                    FavoriteMatch.AWAY_NAME to event.awayName,
                    FavoriteMatch.AWAY_SCORE to event.awayScore
                )
            }
            detailUI.progress.snackbar(getString(R.string.added_to_favorite)).show()
        } catch (e: SQLiteConstraintException) {
            detailUI.progress.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    FavoriteMatch.TABLE_FAVORITE_MATCH, "(EVENT_ID = {id})",
                    "id" to id
                )
            }
            detailUI.progress.snackbar(getString(R.string.removed_from_favorite)).show()
        } catch (e: SQLiteConstraintException) {
            detailUI.progress.snackbar(e.localizedMessage).show()
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
            val result = select(FavoriteMatch.TABLE_FAVORITE_MATCH)
                .whereArgs("(EVENT_ID = {id})", "id" to id)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }
}
