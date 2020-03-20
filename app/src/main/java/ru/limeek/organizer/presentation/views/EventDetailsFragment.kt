package ru.limeek.organizer.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.android.support.DaggerFragment
import ru.limeek.organizer.R
import ru.limeek.organizer.databinding.FragmentEventDetailsBinding
import ru.limeek.organizer.domain.entities.event.RemindTime
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.viewmodels.EventDetailsViewModel
import javax.inject.Inject

class EventDetailsFragment: DaggerFragment() {
    @Inject
    internal lateinit var viewModel: EventDetailsViewModel
    private lateinit var binding: FragmentEventDetailsBinding

    private lateinit var locationStringList: List<String>
    private lateinit var notificationStringList: List<String>

    private val args: EventDetailsFragmentArgs by navArgs()

    private val REQ_CODE_LOCATION = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.includeToolbar.toolbar.title = getString(R.string.event_details)
        binding.includeToolbar.toolbar.inflateMenu(R.menu.event_details_menu)
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        binding.includeToolbar.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.includeToolbar.toolbar.setOnMenuItemClickListener {
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
        viewModel.remindTimeList.observe(viewLifecycleOwner, Observer { initNotificationSpinner(it) })
        viewModel.userLocationList.observe(viewLifecycleOwner, Observer { initLocationSpinner(it) })

        viewModel.date.observe(viewLifecycleOwner, Observer { binding. etDate.setText(it) })
        viewModel.time.observe(viewLifecycleOwner, Observer { binding.etTime.setText(it) })
        viewModel.summary.observe(viewLifecycleOwner, Observer { binding.etSummary.setText(it) })
        viewModel.remindTime.observe(viewLifecycleOwner, Observer { updateRemindTime(it) })
        viewModel.location.observe(viewLifecycleOwner, Observer { updateLocation(it) })
        viewModel.finish.observe(viewLifecycleOwner, Observer{ findNavController().popBackStack() })
    }

    private fun initNotificationSpinner(list: List<RemindTime>) {
        context?.let{
            val stringValues = resources.getStringArray(R.array.remind_array).toMutableList().subList(0, list.size)
            val adapter = ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, stringValues)
            binding.spinnerRemind.adapter = adapter
            binding.spinnerRemind.onItemSelectedListener = onRemindTimeClick
        }
    }

    private fun initLocationSpinner(list: List<Location>) {
        context?.let {
            val stringList = list.map { it.name }.toMutableList()
            stringList.add(0, getString(R.string.choose))
            stringList.add(getString(R.string.new_location))
            stringList.add(getString(R.string.custom_location))
            locationStringList = stringList
            val adapter = ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, locationStringList)
            binding.spinnerLocation.adapter = adapter
            binding.spinnerLocation.onItemSelectedListener = onLocationClick
        }
    }

    private fun updateRemindTime(remindTime: RemindTime) {
        if (remindTime != RemindTime.NOREMIND) {
            binding.switchNotification.isChecked = true
            binding.linearLayoutNotification.visibility = View.VISIBLE
            binding.spinnerRemind.setSelection(RemindTime.values().indexOf(remindTime))
        } else {
            binding.switchNotification.isChecked = false
            binding.linearLayoutNotification.visibility = View.GONE
        }
    }

    private fun updateLocation(location: Location?) {
        if (location != null) {
            binding.switchLocation.isChecked = true
            binding.linearLayoutLocationChoose.visibility = View.VISIBLE
            if(location.createdByUser)
                binding.spinnerLocation.setSelection(locationStringList.indexOf(location.name))
        } else {
            binding.switchLocation.isChecked = false
            binding.linearLayoutLocationChoose.visibility = View.GONE
            binding.linearLayoutLocationCreation.visibility = View.GONE
        }
    }

    private fun showLocCreation(value: Boolean) {
        binding.linearLayoutLocationCreation.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun showNotificationLayout(value: Boolean){
        binding.linearLayoutNotification.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun showLocationChooseLayout(value: Boolean){
        binding.linearLayoutLocationChoose.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun initViewListeners(){
        binding.etDate.setOnClickListener { showDatePicker() }
        binding.etTime.setOnClickListener { showTimePicker() }
        binding.etSummary.addTextChangedListener(summaryTextWatcher)
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked -> showNotificationLayout(isChecked) }
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked -> showLocationChooseLayout(isChecked) }
    }

    private fun initViewModel() {
        if(args.event != null)
            viewModel.init(args.event!!)
        else
            viewModel.init()
    }

    private fun showDatePicker(){
        val datePickerDialog = DatePickerDialogFragment()
        datePickerDialog.arguments = Bundle().apply { putString(Constants.DATE, viewModel.date.value) }
        datePickerDialog.date.observe(viewLifecycleOwner, Observer{ viewModel.updateDate(it) })
        datePickerDialog.show(childFragmentManager, DatePickerDialogFragment.TAG)
    }

    private fun showTimePicker(){
        val timePickerDialog = TimePickerDialogFragment()
        timePickerDialog.arguments = Bundle().apply { putString(Constants.TIME, viewModel.time.value) }
        timePickerDialog.time.observe(viewLifecycleOwner, Observer{ viewModel.updateTime(it) })
        timePickerDialog.show(childFragmentManager, TimePickerDialogFragment.TAG)
    }

    private fun startLocCreateActivity(){
        val directions = EventDetailsFragmentDirections.actionEventDetailsFragmentToLocationDetailsFragment(true, Location())
        findNavController().navigate(directions)
    }

    private val onLocationClick = object: AdapterView.OnItemSelectedListener {
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

    private val onRemindTimeClick = object: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            viewModel.updateEventRemindTime(position)
        }
    }

    private val summaryTextWatcher = object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewModel.updateSummary(p0.toString())
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}