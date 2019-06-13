package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_digest.*
import ru.limeek.organizer.R

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
        val calendarIntent = Intent(this, MainActivity::class.java)
        startActivity(calendarIntent)
        finish()
    }

    private fun startLocationsActivity(){
        val locationsIntent = Intent(this, LocationActivity::class.java)
        startActivity(locationsIntent)
        finish()
    }
}