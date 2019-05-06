package ru.limeek.organizer.event.eventdetails.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.DatePicker
import io.reactivex.subjects.PublishSubject

class DatePickerDialogFragment : DialogFragment() {
    val logTag = "DatePickerDialog"
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    var date: PublishSubject<String> = PublishSubject.create()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity, dateSetListener, year, month, day)
    }

    var dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val dateText = when {
            month / 10 == 0 && dayOfMonth / 10 == 0 -> "0$dayOfMonth.0${month + 1}.$year"
            month / 10 == 0 -> "$dayOfMonth.0${month + 1}.$year"
            dayOfMonth / 10 == 0 -> "0$dayOfMonth.${month + 1}.$year"
            else -> "$dayOfMonth.${month + 1}.$year"
        }
        date.onNext(dateText)
        Log.wtf(logTag, "$dayOfMonth.0${month + 1}.$year")
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        day = args!!.getInt("day")
        month = args.getInt("month")
        year = args.getInt("year")
    }
}