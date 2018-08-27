package ru.limeek.organizer.model.Event

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.Duration
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App

enum class RemindTime(val millis : Long?, val durationName : String) : Parcelable{
    NOREMIND(null, App.instance.getString(R.string.no_remind)),
    EXACTTIME(0, App.instance.getString(R.string.remind_exact_time)),
    FIVEMIN(Duration.standardMinutes(5).millis, App.instance.getString(R.string.remind_five_minutes)),
    TENMINUTES(Duration.standardMinutes(10).millis, App.instance.getString(R.string.remind_ten_minutes)),
    HALFHOUR(Duration.standardMinutes(30).millis, App.instance.getString(R.string.remind_half_hour)),
    ONEHOUR(Duration.standardHours(1).millis, App.instance.getString(R.string.remind_one_hour)),
    TWOHOURS(Duration.standardHours(2).millis, App.instance.getString(R.string.remind_two_hours)),
    ONEDAY(Duration.standardDays(1).millis, App.instance.getString(R.string.remind_one_day)),
    TWODAYS(Duration.standardDays(2).millis, App.instance.getString(R.string.remind_two_days)),
    WEEK(Duration.standardDays(7).millis, App.instance.getString(R.string.remind_one_week));

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return durationName
    }

    companion object CREATOR : Parcelable.Creator<RemindTime> {
        override fun createFromParcel(source: Parcel?): RemindTime {
            return RemindTime.values()[source!!.readInt()]
        }

        override fun newArray(size: Int): Array<RemindTime?> {
            return arrayOfNulls(size)
        }
    }
}