package ru.limeek.organizer.data.model.news

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Source : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    protected constructor(parcel: Parcel) {
        this.id = parcel.readValue(String::class.java.classLoader) as String
        this.name = parcel.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Source> = object : Creator<Source> {


            override fun createFromParcel(parcel: Parcel): Source {
                return Source(parcel)
            }

            override fun newArray(size: Int): Array<Source?> {
                return arrayOfNulls(size)
            }

        }
    }

}
