package id.idham.footballmatchschedule.ui.favorites

import android.view.View
import id.idham.footballmatchschedule.data.db.FavoriteMatch

interface OnFavoriteClickListener {
    fun onItemClick(view: View, favorite: FavoriteMatch)
}