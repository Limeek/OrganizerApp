package ru.limeek.organizer.digest

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_digest.*
import ru.limeek.organizer.R
import ru.limeek.organizer.calendar.view.CalendarActivity
import ru.limeek.organizer.locations.view.LocationActivity

class DigestActivity : AppCompatActivity(){

    private lateinit var drawer: DrawerLayout
    private lateinit var navigation: NavigationView

    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_calendar -> startCalendarActivity()
            R.id.navigation_locations -> startLocationsActivity()
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
        setContentView(R.layout.activity_digest)

        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        drawer = drawerLayout
        navigation = navView
        navigation.setNavigationItemSelectedListener(navigationItemClick)
    }

    private fun startCalendarActivity(){
        val calendarIntent = Intent(this, CalendarActivity::class.java)
        startActivity(calendarIntent)
        finish()
    }

    private fun startLocationsActivity(){
        val locationsIntent = Intent(this, LocationActivity::class.java)
        startActivity(locationsIntent)
        finish()
    }
}