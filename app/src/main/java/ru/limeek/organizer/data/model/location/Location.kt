package ru.limeek.organizer.data.model.location

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
        @ColumnInfo(name = "longitude") var longitude : Double,
        @ColumnInfo(name = "latitude") var latitude : Double,
        @ColumnInfo(name = "location_name") var name : String,
        @ColumnInfo(name = "location_address") var address : String,
        @ColumnInfo(name = "created_by_user") var createdByUser : Boolean
        ) : Parcelable{

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id") var id : Long = 0

    @Ignore
    constructor(id : Long, latitude: Double, longitude: Double, name: String, address: String, createdByUser: Boolean) : this(longitude, latitude, name, address, createdByUser){
        this.id = id
    }

    @Ignore
    constructor(): this(0, 0.0, 0.0, "", "", false)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(id)
        dest?.writeDouble(longitude)
        dest?.writeDouble(latitude)
        dest?.writeString(name)
        dest?.writeString(address)
        dest?.writeInt(if(createdByUser) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(source: Parcel): Location {
            val id = source.readLong()
            val longitude = source.readDouble()
            val latitude = source.readDouble()
            val name = source.readString()
            val address = source.readString()
            val createdByUser  = source.readInt() == 1
            return Location(id, longitude, latitude, name!!, address!!, createdByUser)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }

    }
}
