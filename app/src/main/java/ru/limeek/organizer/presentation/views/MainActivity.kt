package ru.limeek.organizer.presentation.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.limeek.organizer.R
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    lateinit var calendarFragment: CalendarFragment
    lateinit var eventFragment: EventsFragment

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private val REQUEST_CODE_ASK_PERMISSIONS = 1

    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_locations -> startLocationsActivity()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initToolbar()
        initFragments()
        initBottomSheet()
        checkAndRequestPermissions()
    }

    fun refreshEventsFragment() {
        eventFragment.refresh()
    }

    private fun initView(){
        floatingButton.setOnClickListener { startDetailsActivity() }
    }

    private fun startDetailsActivity(event: Event? = null) {
        val intent = Intent(this, EventDetailsActivity::class.java)
        if(event != null){
            val bundle = Bundle()
            bundle.putParcelable(Constants.EVENT, event)
            intent.putExtras(bundle)
        }
        startActivity(intent)
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
        calendarFragment = CalendarFragment()
        eventFragment = EventsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.calendarFragContainer, calendarFragment).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, eventFragment).commit()
    }

    private fun initBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(fragContainer)
    }

    private fun checkAndRequestPermissions() {
        if (baseContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                baseContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
    }
}
