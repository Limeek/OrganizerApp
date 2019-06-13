package ru.limeek.organizer.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.places.ui.PlacePicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_event_details.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.RemindTime
import ru.limeek.organizer.data.model.location.LocationSpinnerItem
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.presenters.EventDetailsPresenter
import ru.limeek.organizer.util.Constants
import javax.inject.Inject

class EventDetailsActivity : EventDetailsView,AppCompatActivity(), View.OnClickListener {

    val logTag = "EventDetailsActivity"

    private var component : ViewComponent? = null

    private val PLACE_PICKER_REQUEST = 1
    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private val LOCATION_DETAILS_REQUEST = 2

    private lateinit var notificationAdapter: ArrayAdapter<RemindTime>
    private lateinit var locationAdapter: ArrayAdapter<LocationSpinnerItem>

    @Inject
    lateinit var presenter : EventDetailsPresenter

    private var compositeDisposable = CompositeDisposable()

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
            switchLocation.setOnClickListener(this)
            presenter.setupLocationSpinner()
            etLocation.setOnClickListener(this)
            etDate.setOnClickListener(this)
            etTime.setOnClickListener(this)
        }
        else {
            setContentView(R.layout.activity_event_details_uneditable)
            etLocation.setOnClickListener(this)
        }

        switchNotification.setOnClickListener(this)
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
        notificationAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, presenter.remindTimeList)
        notificationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerRemind.adapter = notificationAdapter
        spinnerRemind.onItemSelectedListener = presenter.notificationSpinnerOnItemSelected()
    }

    override fun setupPlaceSpinnerAdapter(items: List<LocationSpinnerItem>) {
        locationAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, items)
        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerLocation!!.adapter = locationAdapter
        spinnerLocation!!.onItemSelectedListener = presenter.locationSpinnerOnItemsSelected()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> finish()
            R.id.action_submit -> {
                presenter.submit()
                this.finish()
                return true
            }
            R.id.action_delete ->{
                presenter.delete()
                this.finish()
                startCalendarActivity()
                return true
            }
            R.id.action_edit ->{
                this.finish()
                startEditableDetailsActivity()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.etDate -> createDateDialog()
            R.id.etTime -> createTimeDialog()
            R.id.switchNotification -> notificationSwitchClick()
            R.id.switchLocation -> placeSwitchClick()
            R.id.etLocation ->
                if (getUneditable()!=null) startMap(presenter.getMapUri())
                else startPlacePicker()
        }
    }

    private fun notificationSwitchClick() {
        when {
            etTime.text.toString() == "" -> showErrorNotificationAndHideLayout()
            presenter.getDateTimeFromEditTexts() <= DateTime.now() -> {
                Toast.makeText(this,R.string.expired_event_notification,Toast.LENGTH_LONG).show()
                switchNotification.isChecked = false
                linearLayoutNotification.visibility = View.GONE
            }
            else -> {
                if (switchNotification.isChecked) linearLayoutNotification.visibility = View.VISIBLE
                else linearLayoutNotification.visibility = View.GONE
            }
        }
    }

    private fun placeSwitchClick() {
        if (switchLocation!!.isChecked) linearLayoutLocationChoose.visibility = View.VISIBLE
        else {
            linearLayoutLocationChoose.visibility = View.GONE
        }
    }


    override fun showErrorNotificationAndHideLayout() {
        Toast.makeText(this,R.string.notification_exception,Toast.LENGTH_LONG).show()
        switchNotification.isChecked = false
        linearLayoutNotification.visibility = View.GONE
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
                    etLocation.setText(place.address)
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
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
    }

    private fun startEditableDetailsActivity(){
        val intent = Intent(this, EventDetailsActivity::class.java)
        intent.putExtra("event",presenter.event)
        startActivity(intent)
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

    private fun createDateDialog() {
        val datePickerDialogFragment = DatePickerDialogFragment()
        val bundle = Bundle()
        val dateTime = DateTime.parse(getDate(), DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY))
        bundle.putInt("day", dateTime.dayOfMonth)
        bundle.putInt("month", dateTime.monthOfYear - 1)
        bundle.putInt("year", dateTime.year)
        datePickerDialogFragment.arguments = bundle

        compositeDisposable.add(
                datePickerDialogFragment.date
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            updateDate(it)
                            if(getTime().isNotBlank())
                                presenter.updateSpinnerItems()
                        }
        )

        datePickerDialogFragment.show(supportFragmentManager, "DatePicker")
    }

    private fun createTimeDialog() {
        val timePickerDialogFragment = TimePickerDialogFragment()
        val bundle = Bundle()
        bundle.putInt("hour", DateTime.now().hourOfDay)
        bundle.putInt("minute", DateTime.now().minuteOfHour)
        timePickerDialogFragment.arguments = bundle

        compositeDisposable.add(
                timePickerDialogFragment.time
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            updateTime(it)
                            presenter.updateSpinnerItems()
                        }
        )

        timePickerDialogFragment.show(supportFragmentManager, "TimePicker")
    }



    override fun getDate(): String {
        return etDate.text.toString()
    }

    override fun getLocation(): String {
        return etLocation.text.toString()
    }

    override fun getTime(): String {
        return etTime.text.toString()
    }

    override fun getSummary(): String {
        return etSummary.text.toString()
    }

    override fun isLocationChecked(): Boolean {
        return switchLocation != null && switchLocation.isChecked
    }

    override fun isNotificationChecked(): Boolean {
        return switchNotification.isChecked
    }

    override fun notifyLocationAdapter() {
        locationAdapter.notifyDataSetChanged()
    }

    override fun notifyNotificationAdapter() {
        notificationAdapter.notifyDataSetChanged()
    }

    override fun setLocationSpinnerSelecetion(position: Int) {
        spinnerLocation.setSelection(position)
    }

    override fun setRemindSpinnerSelection(position: Int) {
        spinnerRemind.setSelection(position)
    }

    override fun updateDate(date: String) {
        etDate.setText(date)
    }

    override fun updateLocationCreationVisibility(value: Boolean) {
        linearLayoutLocationCreation.visibility = if(value)
            View.VISIBLE
        else
            View.GONE
    }

    override fun updateNotification(isEnabled: Boolean) {
        switchNotification.isChecked = isEnabled
        if(isEnabled)
            linearLayoutNotification.visibility = View.VISIBLE
        else
            linearLayoutNotification.visibility = View.GONE
    }

    override fun updateLocationAddress(address: String) {
        etLocation.setText(address)
    }

    override fun updateLocationChooseVisibility(value: Boolean) {
        linearLayoutLocationChoose.visibility = if(value)
            View.VISIBLE
        else
            View.GONE
    }

    override fun updateLocationSwitch(value: Boolean) {
        switchLocation.isChecked = value
    }

    override fun updateSummary(summary: String) {
        etSummary.setText(summary)
    }

    override fun updateTime(time: String) {
        etTime.setText(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        component = null
    }


}
