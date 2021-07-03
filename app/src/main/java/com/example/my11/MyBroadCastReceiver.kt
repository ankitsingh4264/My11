package com.example.my11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.my11.beans.Predicted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyBroadCastReceiver  : BroadcastReceiver()  {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("ankit", "onReceive: receiver ")

        CoroutineScope(IO).launch {
            Repository().getPredictedMatchesSuspend(context)
        }



    }



}