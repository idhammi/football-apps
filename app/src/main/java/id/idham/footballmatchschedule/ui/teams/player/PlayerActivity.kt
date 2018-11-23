package id.idham.footballmatchschedule.ui.teams.player

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import id.idham.footballmatchschedule.R
import id.idham.footballmatchschedule.data.model.Player
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLAYER = "extra_player"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val player = intent.getParcelableExtra<Player>(EXTRA_PLAYER)

        supportActionBar?.title = player.playerName

        Glide.with(applicationContext).load(player.playerFanart)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions())
            .into(iv_player)

        tv_player_weight.text = player.playerWeight
        tv_player_height.text = player.playerHeight
        tv_player_pos.text = player.playerPos
        tv_player_desc.text = player.playerDesc
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
