package id.idham.footballmatchschedule.ui.favorites

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.id.schedule_pager
import id.idham.footballmatchschedule.ui.PagerAdapter
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.wrapContent

class FavoriteFragment : Fragment() {

    private lateinit var tab: TabLayout
    private lateinit var pager: ViewPager
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ui = UI {
            linearLayout {
                lparams(matchParent, matchParent)
                orientation = LinearLayout.VERTICAL

                //set tab text color
                val colorInt = ContextCompat.getColor(context, android.R.color.white)
                val csl = ColorStateList.valueOf(colorInt)

                tab = tabLayout {
                    lparams(matchParent, wrapContent)
                    backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    tabMode = TabLayout.MODE_FIXED
                    tabGravity = TabLayout.GRAVITY_FILL
                    tabTextColors = csl
                }

                viewPager {
                    id = schedule_pager
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
            }
        }
        return ui.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PagerAdapter(fragmentManager!!)
        adapter.addFragment(FavoriteMatchFragment(), "Matches")
        adapter.addFragment(FavoriteTeamFragment(), "Teams")

        pager = find(schedule_pager)
        pager.adapter = adapter
        tab.setupWithViewPager(pager)
    }
}
