package ru.limeek.organizer.model.Event

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import org.joda.time.DateTime
import ru.limeek.organizer.database.DateTimeConverter
import ru.limeek.organizer.database.RemindTimeConverter
import ru.limeek.organizer.model.Location.Location
import ru.limeek.organizer.util.Constants

@Entity(tableName = "events",
        foreignKeys = [(ForeignKey(
                entity = Location::class,
                parentColumns = [("location_id")],
                childColumns = [("event_location_id")],
                onDelete = ForeignKey.NO_ACTION
        ))],
        indices = [(Index("event_location_id"))]
)
@TypeConverters(DateTimeConverter::class,RemindTimeConverter::class)
data class Event(
                 @ColumnInfo(name = "date_time") var dateTime: DateTime,
                 @ColumnInfo(name = "summary") var summary: String,
                 @ColumnInfo(name = "remind") var remind : RemindTime
                 ) : Parcelable{

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
    @ColumnInfo(name = "event_location_id") var locationId : Long? = null

    @Ignore
    var location : Location? = null


    @Ignore
    constructor(id : Long, eventDateTime: DateTime, eventSummary: String, eventRemind: RemindTime) :
            this(eventDateTime, eventSummary, eventRemind) {
        this.id = id
    }

    @Ignore
    constructor(dateTime: DateTime, summary: String, remind: RemindTime, locationId: Long) : this(dateTime, summary, remind){
        this.locationId = locationId
    }

    @Ignore
    constructor(id:Long, dateTime: DateTime, summary: String, remind: RemindTime, locationId: Long) : this(id, dateTime, summary, remind){
        this.locationId = locationId
    }

    @Ignore
    constructor(eventDateTime: DateTime, eventSummary: String, eventRemind: RemindTime, location: Location?):
            this(eventDateTime,eventSummary,eventRemind){
        this.location = location
    }

    @Ignore
    constructor(id : Long, eventDateTime: DateTime, eventSummary: String, eventRemind: RemindTime, location: Location?):
            this(id,eventDateTime,eventSummary,eventRemind){
        this.location = location
    }

    @Ignore
    constructor(id : Long, eventDateTime: DateTime, eventSummary: String, eventRemind: RemindTime, location: Location, locationId: Long):
            this(id,eventDateTime,eventSummary,eventRemind){
        this.location = location
        this.locationId = locationId
    }


    override fun describeContents(): Int {
        return 0
    }

    fun getDate() : String{
        return dateTime.toString(Constants.FORMAT_DD_MM_YYYY)
    }

    fun getTime() : String{
        return dateTime.toString(Constants.FORMAT_HH_mm)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(id)
        dest?.writeLong(dateTime.millis)
        dest?.writeString(summary)
        dest?.writeParcelable(remind,flags)
        if(location != null)
            dest?.writeParcelable(location,flags)
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            val id = parcel.readLong()
            val eventDateTime = DateTime(parcel.readLong())
            val eventSummary = parcel.readString()
            val eventRemind = parcel.readParcelable<RemindTime>(RemindTime::class.java.classLoader)
            val location = parcel.readParcelable<Location>(Location::class.java.classLoader)
            return if (location != null)
                Event(id, eventDateTime, eventSummary, eventRemind, location, location.id)
            else
                Event(id, eventDateTime, eventSummary, eventRemind)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}
