package ru.limeek.organizer.presentation.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.util.SingleLiveEvent

class TimePickerDialogFragment : DialogFragment() {
    var hour: Int = 0
    var minute: Int = 0

    var time = SingleLiveEvent<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, onTimeSetListener, hour, minute, true)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        val date = DateTime.parse(args?.getString(Constants.TIME), DateTimeFormat.forPattern(Constants.FORMAT_HH_mm))
        hour = date.hourOfDay
        minute = date.minuteOfHour
    }

    var onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val h = if (hourOfDay / 10 == 0) "0$hourOfDay" else hourOfDay.toString()
        val m = if (minute / 10 == 0) "0$minute" else minute.toString()
        time.value = "$h:$m"
    }

    companion object{
        val TAG = "TIME_PICKER_DIALOG_FRAGMENT"
    }
}