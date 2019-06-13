package ru.limeek.organizer.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_location.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.databinding.ActivityLocationBinding
import ru.limeek.organizer.di.components.ViewViewModelComponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.LocationViewModel
import javax.inject.Inject

class LocationActivity : AppCompatActivity() {
    private val LOG_TAG = "LocationActivity"
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var component : ViewViewModelComponent? = null

    private lateinit var binding: ActivityLocationBinding

    @Inject
    lateinit var viewModel: LocationViewModel
    private var onAdapterItemClick = fun(location: Location){
        startDetailsActivity(location)
    }
    var adapter = LocationsAdapter().apply { onItemClick = onAdapterItemClick }



    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_calendar -> startCalendarActivity()
            R.id.navigation_digest ->
                if(!App.instance.deviceIsOffline())
                    startDigestActivity()
                else
                    Toast.makeText(this,getString(R.string.network_error), Toast.LENGTH_LONG).show()
        }
        true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
         return when(item?.itemId){
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.adapter = adapter

        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        navView.setNavigationItemSelectedListener(navigationItemClick)
        viewModel.onCreate()
        observeLiveData()
    }

    private fun startDetailsActivity(location: Location? = null) {
        val detailsIntent = Intent(this, LocationDetailsActivity::class.java)
        if(location != null){
            val bundle = Bundle()
            bundle.putParcelable("location", location)
            detailsIntent.putExtras(bundle)
        }
        startActivity(detailsIntent)
        finish()
    }

    private fun observeLiveData(){
        viewModel.startDetailsActivity.observe(this, Observer {
            startDetailsActivity()
        })
    }

    private fun startCalendarActivity(){
        val calendarIntent = Intent(this, MainActivity::class.java)
        startActivity(calendarIntent)
        finish()
    }

    private fun startDigestActivity(){
        if(baseContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                baseContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET), REQUEST_CODE_ASK_PERMISSIONS)

        if(baseContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED &&
                baseContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED){
            val digestIntent = Intent(this, DigestActivity::class.java)
            startActivity(digestIntent)
            finish()
        }
    }

    private fun getViewComponent() : ViewViewModelComponent {
        if(component == null){
            component = App.instance.component.newViewViewModelComponent(ViewModelModule(this))
        }
        return component!!
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }
}