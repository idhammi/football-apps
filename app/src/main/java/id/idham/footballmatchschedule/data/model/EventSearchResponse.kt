package id.idham.footballmatchschedule.data.model

import com.google.gson.annotations.SerializedName

data class EventSearchResponse(
    @SerializedName("event")
    val event: List<Event>
)