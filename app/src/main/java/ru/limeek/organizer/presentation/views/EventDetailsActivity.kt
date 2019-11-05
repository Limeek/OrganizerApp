package ru.limeek.organizer.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.limeek.organizer.R
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.event.RemindTime
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.presentation.di.modules.ViewModelModule
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.viewmodels.EventDetailsViewModel
import javax.inject.Inject

class EventDetailsActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModel: EventDetailsViewModel

    private lateinit var locationStringList: List<String>
    private lateinit var notificationStringList: List<String>

    private val REQ_CODE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        App.instance.component.newViewComponent(ViewModelModule(this)).inject(this)
        initViewModel()
        observeLiveData()
        initViewListeners()
        initToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQ_CODE_LOCATION -> {
                    viewModel.initLocationSpinnerItems()
                    viewModel.updateEventLocation(data!!.extras?.getParcelable<Location>(Constants.LOCATION)!!)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initToolbar(){
        toolbar.title = title
        toolbar.inflateMenu(R.menu.event_details_menu)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_submit -> {
                    viewModel.submitEvent()
                    true
                }
                R.id.action_delete ->{
                    viewModel.deleteEvent()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeLiveData() {
        viewModel.remindTimeList.observe(this, Observer { initNotificationSpinner(it) })
        viewModel.userLocationList.observe(this, Observer { initLocationSpinner(it) })

        viewModel.date.observe(this, Observer { etDate.setText(it) })
        viewModel.time.observe(this, Observer { etTime.setText(it) })
        viewModel.summary.observe(this, Observer { etSummary.setText(it) })
        viewModel.remindTime.observe(this, Observer { updateRemindTime(it) })
        viewModel.location.observe(this, Observer { updateLocation(it) })
        viewModel.finish.observe(this, Observer{ finish() })
    }

    private fun initNotificationSpinner(list: List<RemindTime>) {
        val stringValues = resources.getStringArray(R.array.remind_array).toMutableList().subList(0, list.size)
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stringValues)
        spinnerRemind.adapter = adapter
        spinnerRemind.onItemSelectedListener = onRemindTimeClick
    }

    private fun initLocationSpinner(list: List<Location>) {
        val stringList = list.map { it.name }.toMutableList()
        stringList.add(0, getString(R.string.choose))
        stringList.add(getString(R.string.new_location))
        stringList.add(getString(R.string.custom_location))
        locationStringList = stringList
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, locationStringList)
        spinnerLocation.adapter = adapter
        spinnerLocation.onItemSelectedListener = onLocationClick
    }

    private fun updateRemindTime(remindTime: RemindTime) {
        if (remindTime != RemindTime.NOREMIND) {
            switchNotification.isChecked = true
            linearLayoutNotification.visibility = View.VISIBLE
            spinnerRemind.setSelection(RemindTime.values().indexOf(remindTime))
        } else {
            switchNotification.isChecked = false
            linearLayoutNotification.visibility = View.GONE
        }
    }

    private fun updateLocation(location: Location?) {
        if (location != null) {
            switchLocation.isChecked = true
            linearLayoutLocationChoose.visibility = View.VISIBLE
            if(location.createdByUser)
                spinnerLocation.setSelection(locationStringList.indexOf(location.name))
        } else {
            switchLocation.isChecked = false
            linearLayoutLocationChoose.visibility = View.GONE
            linearLayoutLocationCreation.visibility = View.GONE
        }
    }

    private fun showLocCreation(value: Boolean) {
        linearLayoutLocationCreation.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun showNotificationLayout(value: Boolean){
        linearLayoutNotification.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun showLocationChooseLayout(value: Boolean){
        linearLayoutLocationChoose.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun initViewListeners(){
        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }
        etSummary.addTextChangedListener(summaryTextWatcher)
        switchNotification.setOnCheckedChangeListener { _, isChecked -> showNotificationLayout(isChecked) }
        switchLocation.setOnCheckedChangeListener { _, isChecked -> showLocationChooseLayout(isChecked) }
    }

    private fun initViewModel() {
        when {
            intent.hasExtra(Constants.EVENT) -> viewModel.init(intent.getParcelableExtra(Constants.EVENT) as Event)
            else -> viewModel.init()
        }
    }

    private fun showDatePicker(){
        val datePickerDialog = DatePickerDialogFragment()
        datePickerDialog.arguments = Bundle().apply { putString(Constants.DATE, viewModel.date.value) }
        datePickerDialog.date.observe(this, Observer{ viewModel.updateDate(it) })
        datePickerDialog.show(supportFragmentManager, DatePickerDialogFragment.TAG)
    }

    private fun showTimePicker(){
        val timePickerDialog = TimePickerDialogFragment()
        timePickerDialog.arguments = Bundle().apply { putString(Constants.TIME, viewModel.time.value) }
        timePickerDialog.time.observe(this, Observer{ viewModel.updateTime(it) })
        timePickerDialog.show(supportFragmentManager, TimePickerDialogFragment.TAG)
    }

    private fun startLocCreateActivity(){
        val intent = Intent(this, LocationDetailsActivity::class.java)
        intent.putExtra(Constants.FROM_EVENT_DETAILS, true)
        startActivityForResult(intent, 1)
    }

    private val onLocationClick = object: OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(av: AdapterView<*>, p1: View?, position: Int, p3: Long) {
            when {
                av.getItemAtPosition(position) == getString(R.string.custom_location) -> showLocCreation(true)
                av.getItemAtPosition(position) == getString(R.string.new_location) -> {
                    startLocCreateActivity()
                    showLocCreation(false)
                }
                position != 0 -> {
                    viewModel.updateEventLocation(position)
                    showLocCreation(false)
                }
                else -> showLocCreation(false)
            }
        }
    }

    private val onRemindTimeClick = object: OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            viewModel.updateEventRemindTime(position)
        }
    }

    private val summaryTextWatcher = object: TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            viewModel.updateSummary(p0.toString())
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}
