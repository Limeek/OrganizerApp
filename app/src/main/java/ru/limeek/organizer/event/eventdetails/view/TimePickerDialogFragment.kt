package ru.limeek.organizer.event.eventdetails.view

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import io.reactivex.subjects.PublishSubject

class TimePickerDialogFragment : DialogFragment() {
    var hour: Int = 0
    var minute: Int = 0
    var time: PublishSubject<String> = PublishSubject.create()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, onTimeSetListener, hour, minute, true)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        hour = args!!.getInt("hour")
        minute = args.getInt("minute")
    }

    var onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val h = if (hourOfDay / 10 == 0) "0$hourOfDay" else hourOfDay.toString()
        val m = if (minute / 10 == 0) "0$minute" else minute.toString()
        time.onNext("$h:$m")
    }
}