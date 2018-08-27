package ru.limeek.organizer.locations.locationdetails.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_location_details.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.event.eventdetails.view.EventDetailsActivity
import ru.limeek.organizer.locations.locationdetails.presenter.LocationDetailsPresenter
import ru.limeek.organizer.locations.view.LocationActivity
import ru.limeek.organizer.model.Location.Location
import javax.inject.Inject

class LocationDetailsActivity : LocationDetailsView, AppCompatActivity() {
    private val logTag = "LocationDetailsActivity"
    private val PLACE_PICKER_REQUEST = 1
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var component : ViewComponent? = null

    @Inject
    lateinit var presenter : LocationDetailsPresenter

    override lateinit var name: EditText
    override lateinit var address: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        getViewComponent().inject(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = etName
        address = etAddress
        address.setOnClickListener{startPlacePicker()}

        presenter.onCreate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.location_details_menu, menu)
        val itemDelete = menu?.findItem(R.id.delete)

        if(!intent.hasExtra("location"))
            itemDelete?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->
                finish()
            R.id.delete -> {
                presenter.delete()
                startLocationActivity()
            }
            R.id.submit -> {
                presenter.submit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun startLocationActivity(){
        val backIntent = Intent(this,LocationActivity::class.java)
        startActivity(backIntent)
        finish()
    }

    override fun getLocation() : Location? {
        if(intent.hasExtra("location"))
            return intent.extras.getParcelable("location")
        return null
    }

    override fun getFromEventDetails(): Boolean? {
        if(intent.hasExtra("fromEventDetails"))
            return intent.getBooleanExtra("fromEventDetails",true)
        return null
    }

    private fun startPlacePicker() {
        if(baseContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                baseContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_ASK_PERMISSIONS)

        if(baseContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED &&
                baseContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            val ppIntent = PlacePicker.IntentBuilder().build(this)
            try {
                startActivityForResult(ppIntent, PLACE_PICKER_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
            when(requestCode){
                PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this,data)
                    presenter.createLocation(place)
                    address.setText(place.address)
                }
            }
        super.onActivityResult(requestCode, resultCode, data)
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
        presenter.onDestroy()
    }

    override fun startEventDetailsWithResult(bundle: Bundle) {
        val intent = Intent(this,EventDetailsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("location",bundle)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}