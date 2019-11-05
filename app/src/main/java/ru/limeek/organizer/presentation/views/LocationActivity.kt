package ru.limeek.organizer.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.limeek.organizer.R
import ru.limeek.organizer.presentation.adapter.LocationsAdapter
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.presentation.di.components.ViewComponent
import ru.limeek.organizer.presentation.di.modules.ViewModelModule
import ru.limeek.organizer.presentation.viewmodels.LocationViewModel
import javax.inject.Inject

class LocationActivity : AppCompatActivity() {
    private val LOG_TAG = "LocationActivity"
    private val REQUEST_CODE_ASK_PERMISSIONS = 1

    private var component: ViewComponent? = null

    private val adapter: LocationsAdapter by lazy {
        LocationsAdapter().apply {
            recyclerView.adapter = this
            onItemClick = onAdapterItemClick
        }
    }

    @Inject
    lateinit var viewModel: LocationViewModel

    private var onAdapterItemClick = fun(location: Location) {
        startDetailsActivity(location)
    }

    private val navigationItemClick = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_calendar -> startCalendarActivity()
        }
        true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_location)
        getViewComponent().inject(this)

        initToolbar()
        navView.setNavigationItemSelectedListener(navigationItemClick)
        observeLiveData()
    }

    private fun initToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        toolbar.setNavigationOnClickListener{ drawerLayout.openDrawer(GravityCompat.START) }
        toolbar.title = title
        navView.setNavigationItemSelectedListener(navigationItemClick)
    }

    private fun startDetailsActivity(location: Location? = null) {
        val detailsIntent = Intent(this, LocationDetailsActivity::class.java)
        if (location != null) {
            val bundle = Bundle()
            bundle.putParcelable("location", location)
            detailsIntent.putExtras(bundle)
        }
        startActivity(detailsIntent)
        finish()
    }

    private fun observeLiveData() {
        viewModel.locations.observe(this, Observer {
            adapter.dataset = it
            adapter.notifyDataSetChanged()
        })

        viewModel.startDetailsActivity.observe(this, Observer {
            startDetailsActivity()
        })
    }

    private fun startCalendarActivity() {
        val calendarIntent = Intent(this, MainActivity::class.java)
        startActivity(calendarIntent)
        finish()
    }

    private fun getViewComponent(): ViewComponent {
        if (component == null) {
            component = App.instance.component.newViewComponent(ViewModelModule(this))
        }
        return component!!
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }
}