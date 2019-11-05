package ru.limeek.organizer.presentation.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.limeek.organizer.R
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.di.components.ViewComponent
import ru.limeek.organizer.presentation.di.modules.ViewModelModule
import ru.limeek.organizer.presentation.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var component: ViewComponent? = null

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var calendarFragment: CalendarFragment
    private lateinit var eventFragment: EventsFragment

    private val REQUEST_CODE_ASK_PERMISSIONS = 1

    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_locations -> startLocationsActivity()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewComponent().inject(this)
        setContentView(R.layout.activity_main)
        initToolbar()
        initFragments()
        checkAndRequestPermissions()
    }

    fun refreshEventsFragment() {
        eventFragment.refresh()
    }

    private fun initToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        toolbar.setNavigationOnClickListener{ drawerLayout.openDrawer(GravityCompat.START) }
        toolbar.title = title
        navView.setNavigationItemSelectedListener(navigationItemClick)
    }

    private fun startLocationsActivity() {
        val locationsIntent = Intent(this, LocationActivity::class.java)
        startActivity(locationsIntent)
        finish()
    }

    private fun initFragments() {
        eventFragment = EventsFragment()
        calendarFragment = CalendarFragment()
        supportFragmentManager.beginTransaction().replace(R.id.calendarFragContainer, calendarFragment).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, eventFragment).commit()
    }

    private fun getViewComponent(): ViewComponent {
        component = App.instance.component.newViewComponent(ViewModelModule(this))
        return component!!
    }

    private fun checkAndRequestPermissions() {
        if (baseContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                baseContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }

}
