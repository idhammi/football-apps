package id.idham.footballmatchschedule.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
    @SerializedName("idPlayer")
    var playerId: String? = null,
    @SerializedName("idTeam")
    var teamId: String? = null,
    @SerializedName("strPlayer")
    var playerName: String? = null,
    @SerializedName("strDescriptionEN")
    var playerDesc: String? = null,
    @SerializedName("strPosition")
    var playerPos: String? = null,
    @SerializedName("strHeight")
    var playerHeight: String? = null,
    @SerializedName("strWeight")
    var playerWeight: String? = null,
    @SerializedName("strCutout")
    var playerCutout: String? = null,
    @SerializedName("strThumb")
    var playerThumb: String? = null,
    @SerializedName("strFanart1")
    var playerFanart: String? = null
) : Parcelable