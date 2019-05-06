package ru.limeek.organizer.calendar.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_calendar.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.calendar.presenter.CalendarPresenter
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.digest.DigestActivity
import ru.limeek.organizer.event.view.EventsFragment
import ru.limeek.organizer.locations.view.LocationActivity
import javax.inject.Inject

class CalendarActivity : AppCalendarView, AppCompatActivity()  {
    @Inject
    lateinit var presenter : CalendarPresenter

    private var component : ViewComponent? = null

    private lateinit var eventFragment : Fragment
    private lateinit var calendar : CalendarView
    private lateinit var navigation : NavigationView
    private lateinit var drawer : DrawerLayout

    private val LOG_TAG : String = "CalendarActivity"
    private val REQUEST_CODE_ASK_PERMISSIONS = 1


    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_locations -> startLocationsActivity()
            R.id.navigation_digest ->
                if(!App.instance.deviceIsOffline())
                    startDigestActivity()
                else
                    Toast.makeText(this,getString(R.string.network_error),Toast.LENGTH_LONG).show()
        }
        true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        getViewComponent().inject(this)

        calendar = calendarView
        eventFragment = fragContainer
        drawer = drawerLayout
        navigation = navView

        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        presenter.onCreate()
        calendar.setOnDateChangeListener(presenter.onDateChange())
        navigation.setNavigationItemSelectedListener(navigationItemClick)

        checkAndRequestPermissions()
    }

   override fun refreshEventsFragment(){
       (eventFragment as EventsFragment).refreshRecyclerView()
    }

   private fun startLocationsActivity(){
       val locationsIntent = Intent(this, LocationActivity::class.java)
       startActivity(locationsIntent)
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

    private fun getViewComponent() : ViewComponent{
        if(component == null){
            component = App.instance.component.newViewComponent(PresenterModule(this))
        }
        return component!!
    }

    private fun checkAndRequestPermissions(){
        if(baseContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                baseContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }

    override fun updateCurrentDate(dateString: String) {
        tvCurrentDate.text = dateString
    }

    override fun setDate(millis: Long) {
        calendar.setDate(millis,false,true)
    }
}
