package ru.limeek.organizer.model.weather
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Currently {

    @SerializedName("time")
    @Expose
    var time: Double? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("nearestStormDistance")
    @Expose
    var nearestStormDistance: Double? = null
    @SerializedName("precipDoubleensity")
    @Expose
    var precipDoubleensity: Double? = null
    @SerializedName("precipProbability")
    @Expose
    var precipProbability: Double? = null
    @SerializedName("temperature")
    @Expose
    var temperature: Double? = null
    @SerializedName("apparentTemperature")
    @Expose
    var apparentTemperature: Double? = null
    @SerializedName("dewPoDouble")
    @Expose
    var dewPoDouble: Double? = null
    @SerializedName("humidity")
    @Expose
    var humidity: Double? = null
    @SerializedName("pressure")
    @Expose
    var pressure: Double? = null
    @SerializedName("windSpeed")
    @Expose
    var windSpeed: Double? = null
    @SerializedName("windGust")
    @Expose
    var windGust: Double? = null
    @SerializedName("windBearing")
    @Expose
    var windBearing: Double? = null
    @SerializedName("cloudCover")
    @Expose
    var cloudCover: Double? = null
    @SerializedName("uvIndex")
    @Expose
    var uvIndex: Double? = null
    @SerializedName("visibility")
    @Expose
    var visibility: Double? = null
    @SerializedName("ozone")
    @Expose
    var ozone: Double? = null

}
