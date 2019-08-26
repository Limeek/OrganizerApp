package ru.limeek.organizer.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.thbs.skycons.library.*
import kotlinx.android.synthetic.main.fragment_weather.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.WeatherViewModel
import javax.inject.Inject

class WeatherFragment : Fragment() {
    @Inject
    lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectComponent()
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.iconString.observe(viewLifecycleOwner, Observer{
            setWeatherIcon(it)
        })
    }

    private fun setWeatherIcon(iconString : String){
        val icon: SkyconView = when(iconString){
            "clear-day" -> SunView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "clear-night" -> MoonView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "rain" -> CloudRainView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "snow" -> CloudSnowView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "sleet" -> CloudSnowView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "wind" -> WindView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "fog" -> CloudFogView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "cloudy" -> CloudView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "partly-cloudy-day" -> CloudSunView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "partly-cloudy-night" -> CloudMoonView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            "thunder" -> CloudThunderView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
            else -> SunView(context,true,true, Color.parseColor("#000000"), android.R.color.transparent)
        }
        addIconToLayout(icon)
    }

    private fun addIconToLayout(icon: SkyconView){
        val linLayoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        linLayoutIcon.layoutParams = linLayoutParams
        linLayoutParams.height = 400
        linLayoutParams.width = 400
        linLayoutIcon.addView(icon)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun injectComponent(){
        App.instance.component.newViewViewModelComponent(ViewModelModule(this)).inject(this)
    }
}