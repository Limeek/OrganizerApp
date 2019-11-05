package ru.limeek.organizer.domain.entities.event

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.Duration
import ru.limeek.organizer.R
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.util.Constants

enum class RemindTime(val millis : Long?) : Parcelable{
    NOREMIND(null),
    EXACTTIME(0),
    FIVEMIN(Duration.standardMinutes(5).millis),
    TENMINUTES(Duration.standardMinutes(10).millis),
    HALFHOUR(Duration.standardMinutes(30).millis),
    ONEHOUR(Duration.standardHours(1).millis),
    TWOHOURS(Duration.standardHours(2).millis),
    ONEDAY(Duration.standardDays(1).millis),
    TWODAYS(Duration.standardDays(2).millis),
    WEEK(Duration.standardDays(7).millis);

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemindTime> {
        override fun createFromParcel(source: Parcel?): RemindTime {
            return values()[source!!.readInt()]
        }

        override fun newArray(size: Int): Array<RemindTime?> {
            return arrayOfNulls(size)
        }
    }
}