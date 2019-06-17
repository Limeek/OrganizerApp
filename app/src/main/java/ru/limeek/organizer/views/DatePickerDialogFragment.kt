package ru.limeek.organizer.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import io.reactivex.subjects.PublishSubject
import ru.limeek.organizer.util.SingleLiveEvent

class DatePickerDialogFragment : DialogFragment() {
    val logTag = "DatePickerDialog"
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    var date = SingleLiveEvent<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity!!, dateSetListener, year, month, day)
    }

    var dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val dateText = when {
            month / 10 == 0 && dayOfMonth / 10 == 0 -> "0$dayOfMonth.0${month + 1}.$year"
            month / 10 == 0 -> "$dayOfMonth.0${month + 1}.$year"
            dayOfMonth / 10 == 0 -> "0$dayOfMonth.${month + 1}.$year"
            else -> "$dayOfMonth.${month + 1}.$year"
        }
        date.value = dateText
        Log.wtf(logTag, "$dayOfMonth.0${month + 1}.$year")
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        day = args!!.getInt("day")
        month = args.getInt("month")
        year = args.getInt("year")
    }
}