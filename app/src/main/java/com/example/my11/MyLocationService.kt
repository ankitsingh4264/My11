package com.example.my11

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationResult

class MyLocationService : BroadcastReceiver() {

    companion object{
        val ACTION_PROCESS_UPDATE = "com.example.my11.UPDATE_LOCATION"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!=null)
        {
            val action = intent.action!!
            if(action == ACTION_PROCESS_UPDATE)
            {
                val result= LocationResult.extractResult(intent!!)

                if(result!=null)
                {
                    val loc = result.lastLocation
                    Utils.latitude = loc.latitude.toString()
                    Utils.longitude = loc.longitude.toString()
                    Log.i("Rajeev",Utils.latitude+" "+Utils.longitude)
                }
            }
        }

    }

}