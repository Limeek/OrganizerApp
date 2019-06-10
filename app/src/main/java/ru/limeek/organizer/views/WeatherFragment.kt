package ru.limeek.organizer.views

import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.thbs.skycons.library.*
import ru.limeek.organizer.R
import ru.limeek.organizer.presenters.WeatherPresenterImpl

class WeatherFragment : WeatherFragmentView, Fragment() {

    override lateinit var degrees: TextView
    override lateinit var description: TextView
    override lateinit var icon: SkyconView
    override lateinit var city: TextView
    override lateinit var iconLayout: LinearLayout

    var presenter = WeatherPresenterImpl(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        degrees = view.findViewById(R.id.tvDegrees)
        description = view.findViewById(R.id.tvDescr)
        city = view.findViewById(R.id.tvCity)
        iconLayout = view.findViewById(R.id.linLayoutIcon)

        presenter.onCreate()

        return view
    }

    override fun setWeatherIcon(iconString : String){
        when(iconString){
            "clear-day" -> icon = SunView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "clear-night" -> icon = MoonView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "rain" -> icon = CloudRainView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "snow" -> icon = CloudSnowView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "sleet" -> icon = CloudSnowView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "wind" -> icon = WindView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "fog" -> icon = CloudFogView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "cloudy" -> icon = CloudView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "partly-cloudy-day" -> icon = CloudSunView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "partly-cloudy-night" -> icon = CloudMoonView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "thunder" -> icon = CloudThunderView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
        }
        addIconToLayout()
    }

    private fun addIconToLayout(){
        val linLayoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        iconLayout.layoutParams = linLayoutParams
        linLayoutParams.height = 400
        linLayoutParams.width = 400
        iconLayout.addView(icon)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}