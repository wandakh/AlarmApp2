package com.example.alarmapp2.room


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.databinding.ActivityMainBinding
import com.example.alarmapp.fragment.TimePickerFragment
import com.example.alarmapp.room.Alarm
import com.example.alarmapp.room.AlarmDB
import kotlinx.android.synthetic.main.activity_repeating_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class repeating_alarm_Activity : AppCompatActivity(), View.OnClickListener, TimePickerFragment.DialogTimeListener {

    private var binding : ActivityMainBinding? = null

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    private  lateinit var alarmReceiver: AlarmReceiver

    val db by lazy { AlarmDB (this) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_repeating_alarm)

        btn_set_time_repeating.setOnClickListener(this)

        btn_add_set_repeating_alarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_set_time_repeating -> {
                val timePickerFragmentRepeating = TimePickerFragment()
                timePickerFragmentRepeating.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_add_set_repeating_alarm -> {
                val repeatTime = tv_repeating_time.text.toString()
                val repeatMessage = et_note_repeating.text.toString()

                alarmReceiver.setReapetingAlarm(
                    this, AlarmReceiver.TYPE_REPETING,
                    repeatTime,
                    repeatMessage,

                    )
                CoroutineScope(Dispatchers.IO).launch {
                    db.alarmDao().addAlarm(
                        Alarm(
                            0,
                            repeatTime,
                            "Rpeating Alarm",
                            repeatMessage,
                            AlarmReceiver.TYPE_REPETING
                        )
                    )
                    finish()
                }

            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormatRepeating = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_REPEAT_TAG -> tv_repeating_time.text = timeFormatRepeating.format(calendar.time)
            else ->{

            }
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        binding = null
    }

}