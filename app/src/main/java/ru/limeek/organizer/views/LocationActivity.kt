package ru.limeek.organizer.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_location.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.presenters.LocationPresenter
import javax.inject.Inject

class LocationActivity : LocationView, AppCompatActivity() {
    private val LOG_TAG = "LocationActivity"
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var component : ViewComponent? = null
    private lateinit var fab : FloatingActionButton
    private lateinit var navigation : NavigationView
    private lateinit var drawer : DrawerLayout
    private lateinit var recView : RecyclerView
    private lateinit var recAdapter : LocationsAdapter

    @Inject
    lateinit var presenter : LocationPresenter


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
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        getViewComponent().inject(this)

        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
        drawer = drawerLayout
        fab = fabLocation
        fab.setOnClickListener(presenter.fabOnClick())
        navigation = navView
        recAdapter = LocationsAdapter(this)
        recView = recyclerView
        recView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
            adapter = recAdapter
        }

        navView.setNavigationItemSelectedListener(navigationItemClick)
    }

    override fun startDetailsActivity() {
        val detailsIntent = Intent(this, LocationDetailsActivity::class.java)
        startActivity(detailsIntent)
        finish()
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

    private fun getViewComponent() : ViewComponent {
        if(component == null){
            component = App.instance.component.newViewComponent(PresenterModule(this))
        }
        return component!!
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
        recAdapter.onDestroy()
    }
}