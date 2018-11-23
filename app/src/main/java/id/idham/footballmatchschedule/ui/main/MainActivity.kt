package id.idham.footballmatchschedule.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.R.id.*
import id.idham.footballmatchschedule.ui.favorites.FavoriteFragment
import id.idham.footballmatchschedule.ui.matches.MatchesFragment
import id.idham.footballmatchschedule.ui.matches.search.MatchSearchActivity
import id.idham.footballmatchschedule.ui.teams.TeamsFragment
import id.idham.footballmatchschedule.ui.teams.search.TeamsSearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                matches -> {
                    loadMatchesFragment(savedInstanceState)
                    supportActionBar?.elevation = 0f
                    isFavorite = false
                    invalidateOptionsMenu()
                }
                teams -> {
                    loadTeamsFragment(savedInstanceState)
                    supportActionBar?.elevation = 12f
                    isFavorite = false
                    invalidateOptionsMenu()
                }
                favorites -> {
                    loadFavoriteFragment(savedInstanceState)
                    supportActionBar?.elevation = 0f
                    isFavorite = true
                    invalidateOptionsMenu()
                }
            }
            true
        }
        bottom_navigation.selectedItemId = matches
    }

    private fun loadMatchesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MatchesFragment(), MatchesFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun loadTeamsFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, TeamsFragment(), TeamsFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_container,
                    FavoriteFragment(), FavoriteFragment::class.java.simpleName
                )
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        if (isFavorite) {
            menu?.findItem(R.id.action_search)?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            action_search -> {
                val id = bottom_navigation.menu.findItem(bottom_navigation.selectedItemId)
                when (id.itemId) {
                    matches -> startActivity<MatchSearchActivity>()
                    teams -> startActivity<TeamsSearchActivity>()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
