package com.example.alarmapp2

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.number.IntegerWidth
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object{

        const val TYPE_ONE_TIME = 0
        const val TYPE_REPETING = 1

        const val  EXTRA_MESSAGE  = "message"
        const val  EXTRA_TYPE = "type"

        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101

        private const val DATE_FORMAT = "dd-MM-yyyy"
        private const val TIME_FORMAT = "HH-mm"

    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getIntExtra(EXTRA_TYPE,0)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = if (type == TYPE_ONE_TIME)"One Time Alarm" else "Repaeting Alarm"
        val notifId = if (type == TYPE_ONE_TIME) ID_ONETIME else ID_REPEATING

        if (message!= null)
            showAlarmNotification(context, title, message , notifId )

    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int

    ) {
        val channelID = "Channel_1"
        val channelName = "Alarm Manager channel"
        val notificationManageCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context,channelID)
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelID)
            notificationManageCompat.createNotificationChannel(channel)

        }
        val notification = builder.build()
        notificationManageCompat.notify(notifId, notification)
    }

    fun setOneTimeAlarm(context: Context, type : Int, date : String, time : String, message: String){
        if (isDateInvelid(date, DATE_FORMAT) || isDateInvelid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver:: class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        Log.e("ONE TIME", "$date $time")
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split("-").toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0])- 1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Succes Set Up One Time Alarm", Toast.LENGTH_SHORT).show()

    }

    fun setReapetingAlarm(context: Context, type: Int, time: String, message: String){
        if (isDateInvelid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver:: class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        val putExtra = intent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[0])- 1)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Succes Set Up Reapeting Alarm", Toast.LENGTH_SHORT).show()
    }
    fun cancelAlarm(context: Context, type: Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver:: class.java)

        val requiresCode = when (type){
            TYPE_ONE_TIME -> ID_ONETIME
            TYPE_REPETING -> ID_REPEATING
            else -> Log.i("cancel Alarm", "cancelAlarm : Unknow type of alarm")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, requiresCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        if (type == TYPE_ONE_TIME) {
            Toast.makeText(context, "Cancel One Time Alarm", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(context, "Cancel Repeating  Alarm", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isDateInvelid(date: String, format : String):Boolean{
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        }catch (e: ParseException){
            true
        }
    }
}