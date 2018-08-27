package ru.limeek.organizer.event.eventdetails.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.DatePicker

class DatePickerDialogFragment() : DialogFragment() {
    val logTag = "DatePickerDialog"
    var year : Int = 0
    var month : Int = 0
    var day : Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity,dateSetListener,year,month,day)
    }

    var dateSetListener = DatePickerDialog.OnDateSetListener() {
        _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val parentActivity = activity as EventDetailsActivity
            if (month / 10 == 0) {
                parentActivity.date.setText("$dayOfMonth.0${month + 1}.$year")
                if(parentActivity.time.text.toString() != "")
                    parentActivity.presenter.updateSpinnerItems()
            }
            else {
                parentActivity.date.setText("$dayOfMonth.${month + 1}.$year")
                if(parentActivity.time.text.toString() != "")
                    parentActivity.presenter.updateSpinnerItems()
            }
        Log.wtf(logTag,"$dayOfMonth.0${month + 1}.$year")

    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        day = args!!.getInt("day")
        month = args.getInt("month")
        year = args.getInt("year")
    }
}