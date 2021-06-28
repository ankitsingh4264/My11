package com.example.my11


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService


class Notification(val context: Context) {

    val CHANNEL_ID="Results"
    fun createNotification(title:String,msg:String){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance)



            val notificationManager: NotificationManager =
                getSystemService(context,NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.trophy)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(7,builder.build())


    }


}