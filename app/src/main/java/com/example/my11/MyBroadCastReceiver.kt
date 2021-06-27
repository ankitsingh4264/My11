package com.example.my11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBroadCastReceiver  : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("ankit", "onReceive: receiver ")

        Notification(context).createNotification("INDIA VS ENGLAND","Congrats you got 120 points see you on learderboard")
    }

}