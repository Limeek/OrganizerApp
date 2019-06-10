package ru.limeek.organizer.data.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("time")
    @Expose
    var time: Double? = null
    @SerializedName("precipDoubleensity")
    @Expose
    var precipDoubleensity: Double? = null
    @SerializedName("precipProbability")
    @Expose
    var precipProbability: Double? = null

}
