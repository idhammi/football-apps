package id.idham.footballmatchschedule.ui.matches

import android.view.View
import id.idham.footballmatchschedule.data.model.Event

interface OnMatchClickListener {
    fun onItemClick(view: View, event: Event)
}