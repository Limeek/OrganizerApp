package ru.limeek.organizer.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Hourly {

    @SerializedName("summary")
    @Expose
    var summary: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("data")
    @Expose
    var data: List<Datum_>? = null

}
