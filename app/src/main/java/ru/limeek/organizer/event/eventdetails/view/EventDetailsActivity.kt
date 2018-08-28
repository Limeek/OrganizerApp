package ru.limeek.organizer.event.eventdetails.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_event_details.*
import org.joda.time.DateTime
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.calendar.view.CalendarActivity
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.event.eventdetails.presenter.EventDetailsPresenter
import ru.limeek.organizer.locations.locationdetails.view.LocationDetailsActivity
import ru.limeek.organizer.model.Event.Event
import ru.limeek.organizer.model.Event.RemindTime
import ru.limeek.organizer.model.Location.LocationSpinnerItem
import javax.inject.Inject

class EventDetailsActivity : EventDetailsView,AppCompatActivity(), View.OnClickListener {

    val logTag = "EventDetailsActivity"

    private var component : ViewComponent? = null

    private val PLACE_PICKER_REQUEST = 1
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private val LOCATION_DETAILS_REQUEST = 2

    override lateinit var date: EditText
    override lateinit var summary: EditText
    override lateinit var time: EditText
    override lateinit var remind: Spinner
    override lateinit var notificationAdapter: ArrayAdapter<RemindTime>
    override lateinit var notification: SwitchCompat
    override lateinit var notificationLayout: LinearLayout
    override lateinit var locationAddress: EditText
    override lateinit var locationAdapter: ArrayAdapter<LocationSpinnerItem>
    override lateinit var locationChooseLayout: LinearLayout

    override var locationSwitch: SwitchCompat? = null
    override var locationSpinner: Spinner? = null
    override lateinit var locationCreationLayout: LinearLayout


    @Inject
    lateinit var presenter : EventDetailsPresenter

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_details_menu,menu)
        val itemDelete = menu?.findItem(R.id.action_delete)
        val itemEdit = menu?.findItem(R.id.action_edit)
        if(!intent.hasExtra("event"))
            itemDelete?.isVisible = false
        if(!intent.hasExtra("uneditable"))
            itemEdit?.isVisible = false
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getViewComponent().inject(this)

        super.onCreate(savedInstanceState)

        if (!intent.hasExtra("uneditable")){
            setContentView(R.layout.activity_event_details)
            locationSwitch = switchLocation
            locationSwitch!!.setOnClickListener(this)
            locationSpinner = spinnerLocation
            locationChooseLayout = linearLayoutLocationChoose
            presenter.setupLocationSpinner()
            locationAddress = etLocation
            locationAddress.setOnClickListener(this)
        }
        else {
            setContentView(R.layout.activity_event_details_uneditable)
            locationAddress = etLocation
            locationAddress.setOnClickListener(this)
        }

        date = etDate
        time = etTime
        summary = etSummary
        remind = spinnerRemind
        date.setOnClickListener(this)
        time.setOnClickListener(this)
        notification = switchNotification
        notificationLayout = linearLayoutNotification
        notification.setOnClickListener(this)
        locationCreationLayout = linearLayoutLocationCreation

        setupNotificationSpinnerAdapter()

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.onCreate()
    }


    override fun getEvent(): Event? {
        if(intent.hasExtra("event"))
            return intent.getParcelableExtra("event")
        return null
    }

    override fun getEventId(): Long? {
        if(intent.hasExtra("eventId"))
            return intent.getLongExtra("eventId",0)
        return null
    }

    override fun getContext(): Context {
        return this
    }

    override fun getUneditable(): Boolean? {
        if(intent.hasExtra("uneditable"))
            return intent.extras.getBoolean("uneditable")
        return null
    }

    override fun setupNotificationSpinnerAdapter() {
        notificationAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,presenter.remindTimeList)
        notificationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        remind.adapter = notificationAdapter
        remind.onItemSelectedListener = presenter.notificationSpinnerOnItemSelected()
    }

    override fun setupPlaceSpinnerAdapter(items: List<LocationSpinnerItem>) {
        locationAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,items)
        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        locationSpinner!!.adapter = locationAdapter
        locationSpinner!!.onItemSelectedListener = presenter.locationSpinnerOnItemsSelected()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> finish()
            R.id.action_submit -> {
                presenter.submit()
                return true
            }
            R.id.action_delete ->{
                presenter.delete()
                startCalendarActivity()
                return true
            }
            R.id.action_edit ->{
                startEditableDetailsActivity()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.etDate -> presenter.createDateDialog().show(supportFragmentManager,"DatePicker")
            R.id.etTime -> presenter.createTimeDialog().show(supportFragmentManager,"TimePicker")
            R.id.switchNotification -> notificationSwitchClick()
            R.id.switchLocation -> placeSwitchClick()
            R.id.etLocation ->
                if (getUneditable()!=null) startMap(presenter.getMapUri())
                else startPlacePicker()
        }
    }

    private fun notificationSwitchClick() {
        when {
            time.text.toString() == "" -> showErrorNotificationAndHideLayout()
            presenter.getDateTimeFromEditTexts() <= DateTime.now() -> {
                Toast.makeText(this,R.string.expired_event_notification,Toast.LENGTH_LONG).show()
                notification.isChecked = false
                notificationLayout.visibility = View.GONE
            }
            else -> {
                if (notification.isChecked) notificationLayout.visibility = View.VISIBLE
                else notificationLayout.visibility = View.GONE
            }
        }
    }

    private fun placeSwitchClick() {
        if (locationSwitch!!.isChecked) locationChooseLayout.visibility = View.VISIBLE
        else {
            locationChooseLayout.visibility = View.GONE
        }
    }


    override fun showErrorNotificationAndHideLayout() {
        Toast.makeText(this,R.string.notification_exception,Toast.LENGTH_LONG).show()
        notification.isChecked = false
        notificationLayout.visibility = View.GONE
    }

    override fun startPlacePicker() {
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
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when (requestCode){
                PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this,data)
                    presenter.createLocation(place)
                    locationAddress.setText(place.address)
                }
                LOCATION_DETAILS_REQUEST ->{
                    presenter.onLocationDetailsResult(data!!.getBundleExtra("location").getLong("locationId"))
                }
            }
        }
    }

    private fun startMap(uri : String){
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(mapIntent)
    }

    override fun startCalendarActivity(){
        val intentMainActivity = Intent(this, CalendarActivity::class.java)
        startActivity(intentMainActivity)
        finish()
    }

    private fun startEditableDetailsActivity(){
        val intent = Intent(this, EventDetailsActivity::class.java)
        intent.putExtra("event",presenter.event)
        startActivity(intent)
        finish()
    }

    override fun startLocationDetailsActivity(){
        val intent = Intent(this, LocationDetailsActivity::class.java)
        intent.putExtra("fromEventDetails", true)
        startActivityForResult(intent,LOCATION_DETAILS_REQUEST)
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
    }


}
