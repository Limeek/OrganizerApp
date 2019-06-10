package ru.limeek.organizer.data.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Flags {

    @SerializedName("sources")
    @Expose
    var sources: List<String>? = null
    @SerializedName("isd-stations")
    @Expose
    var isdStations: List<String>? = null
    @SerializedName("units")
    @Expose
    var units: String? = null

}
