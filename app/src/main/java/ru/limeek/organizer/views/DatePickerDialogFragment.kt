package ru.limeek.organizer.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.util.SingleLiveEvent
import timber.log.Timber

class DatePickerDialogFragment : DialogFragment() {
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    var date = SingleLiveEvent<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity!!, dateSetListener, year, month, day)
    }

    private var dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val dateText = when {
            month / 10 == 0 && dayOfMonth / 10 == 0 -> "0$dayOfMonth.0${month + 1}.$year"
            month / 10 == 0 -> "$dayOfMonth.0${month + 1}.$year"
            dayOfMonth / 10 == 0 -> "0$dayOfMonth.${month + 1}.$year"
            else -> "$dayOfMonth.${month + 1}.$year"
        }
        date.value = dateText
        Timber.e("$dayOfMonth.0${month + 1}.$year")
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        val date = DateTime.parse(args?.getString(Constants.DATE), DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY))
        day = date.dayOfMonth
        month = date.monthOfYear - 1
        year = date.year
    }

    companion object{
        val TAG = "DATE_PICKER_FRAG"
    }
}