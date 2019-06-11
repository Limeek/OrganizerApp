package ru.limeek.organizer.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.databinding.ActivityMainBinding
import ru.limeek.organizer.di.components.ViewViewModelComponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : MainView, AppCompatActivity()  {

    private var component : ViewViewModelComponent? = null
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var calendarFragment: CalendarFragment
    private lateinit var eventFragment : EventsFragment

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
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewComponent().inject(this)

        initBinding()
        initFragments()

        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        navView.setNavigationItemSelectedListener(navigationItemClick)
        checkAndRequestPermissions()
    }

   override fun refreshEventsFragment(){
       eventFragment.refresh()
    }

   private fun startLocationsActivity(){
       val locationsIntent = Intent(this, LocationActivity::class.java)
       startActivity(locationsIntent)
       finish()
   }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
    }

    private fun initFragments(){
        eventFragment = EventsFragment()
        calendarFragment = CalendarFragment()
        supportFragmentManager.beginTransaction().replace(R.id.calendarFragContainer, calendarFragment).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, eventFragment).commit()
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

    private fun getViewComponent(): ViewViewModelComponent {
        component = App.instance.component.newViewViewModelComponent(ViewModelModule(this))
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

}
