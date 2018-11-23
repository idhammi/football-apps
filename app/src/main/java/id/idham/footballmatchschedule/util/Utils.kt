package id.idham.footballmatchschedule.util

import android.view.View
import java.text.SimpleDateFormat
import java.util.*


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun setDateFormat(date: String?): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.US)
    val mDate = changeDateTimezone(date)
    val d: Date = dateFormat.parse(mDate)

    val newDateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.US)
    return newDateFormat.format(d)
}

fun changeDateTimezone(date: String?): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val d: Date = dateFormat.parse(date)

    val tz: TimeZone = TimeZone.getTimeZone("Asia/Jakarta")
    val newDateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.US)
    newDateFormat.timeZone = tz

    return newDateFormat.format(d)
}

fun changeTimezone(time: String?): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date = dateFormat.parse(time)

    val tz: TimeZone = TimeZone.getTimeZone("Asia/Jakarta")
    val newDateFormat = SimpleDateFormat("HH:mm", Locale.US)
    newDateFormat.timeZone = tz

    return newDateFormat.format(date)
}

fun isDatePassed(date: String?): Boolean {

    val newDateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.US)

    val mDate = changeDateTimezone(date)
    val matchDate: Date = newDateFormat.parse(mDate)

    val todayDate = Calendar.getInstance().time
    val todayString = newDateFormat.format(todayDate)
    val currentDate = newDateFormat.parse(todayString)

    return currentDate >= matchDate
}

fun getMilliseconds(date: String, isStart: Boolean): Long {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.US)
        val date1 = sdf.parse(date)
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val beginCal = Calendar.getInstance()

        val minutes: Int
        minutes = if (isStart) cal1.get(Calendar.MINUTE)
        else cal1.get(Calendar.MINUTE) + 90

        beginCal.set(
            cal1.get(Calendar.YEAR),
            cal1.get(Calendar.MONTH),
            cal1.get(Calendar.DAY_OF_MONTH),
            cal1.get(Calendar.HOUR_OF_DAY),
            minutes
        )
        beginCal.timeInMillis
    } catch (e: Exception) {
        Date().time
    }
}