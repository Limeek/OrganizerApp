package ru.limeek.organizer.views

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
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.RemindTime
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.viewmodels.EventDetailsViewModel
import javax.inject.Inject

class EventDetailsActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModel: EventDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        App.instance.component.newViewViewModelComponent(ViewModelModule(this)).inject(this)
        initViewModel()
        observeLiveData()
        initViewListeners()
        initToolbar()
    }

    private fun initToolbar(){
        toolbar.inflateMenu(R.menu.event_details_menu)
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
    }

    private fun initNotificationSpinner(list: List<RemindTime>) {
        val stringList = list.map { it.toString() }
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stringList)
        spinnerRemind.adapter = adapter
        spinnerRemind.onItemSelectedListener = onRemindTimeClick
    }

    private fun initLocationSpinner(list: List<Location>) {
        val stringList = list.map { it.name }.toMutableList()
        stringList.add(0, getString(R.string.choose))
        stringList.add(getString(R.string.custom_location))
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stringList)
        spinnerLocation.adapter = adapter
        spinnerLocation.onItemSelectedListener = onLocationClick
    }

    private fun updateRemindTime(remindTime: RemindTime) {
        if (remindTime != RemindTime.NOREMIND) {
            linearLayoutNotification.visibility = View.VISIBLE
            spinnerRemind.setSelection(RemindTime.values().indexOf(remindTime))
        } else
            linearLayoutNotification.visibility = View.GONE
    }

    private fun updateLocation(location: Location?) {
        if (location != null) {
            linearLayoutLocationChoose.visibility = View.VISIBLE
        } else {
            linearLayoutLocationChoose.visibility = View.GONE
            linearLayoutLocationCreation.visibility = View.GONE
        }
    }

    private fun showLocCreation(value: Boolean) {
        if (value)
            linearLayoutLocationCreation.visibility = View.VISIBLE
        else
            linearLayoutLocationCreation.visibility = View.GONE
    }

    private fun initViewListeners(){
        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }
        etSummary.addTextChangedListener(summaryTextWatcher)
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

    private val onLocationClick = object: OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(av: AdapterView<*>, p1: View?, position: Int, p3: Long) {
            if (position != 0 && position != av.childCount) {
                viewModel.updateEventLocation(position)
                showLocCreation(false)
            } else if (position == av.childCount - 1)
                showLocCreation(true)
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
