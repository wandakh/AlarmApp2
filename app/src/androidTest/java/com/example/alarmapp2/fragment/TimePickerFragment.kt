package com.example.alarmapp2.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var mListener: DialogTimeListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as DialogTimeListener?
    }

    override fun onDetach() {
        super.onDetach()
        if (mListener != null) {

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val formaHour24 = true
        return TimePickerDialog(activity, this, hour, minute, formaHour24)
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        mListener?.onDialogTimeSet(tag, hourOfDay, minute)
    }

    interface DialogTimeListener {
        fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int)
    }



}