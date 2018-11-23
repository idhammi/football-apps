package id.idham.footballmatchschedule.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * Created by Idham-PC on 26/01/2018.
 */

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val lstFragment = ArrayList<Fragment>()
    private val lsttitle = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return lstFragment[position]
    }

    override fun getCount(): Int {
        return lsttitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return lsttitle[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        lstFragment.add(fragment)
        lsttitle.add(title)
    }
}