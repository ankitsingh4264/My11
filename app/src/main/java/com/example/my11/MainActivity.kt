package com.example.my11

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var locationrequest:LocationRequest
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_My11)
        setContentView(R.layout.activity_main)

        //setSupportActionBar(toolbar)

            getLoction()

            setupNav()
        setAlarm()



    }

    private fun setAlarm(){

        //val timeTrigger=System.currentTimeMillis()+10*1000

        val intent = Intent(this,MyBroadCastReceiver::class.java)
        val pendingIntent= PendingIntent.getBroadcast(this,7,intent,0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

//        val calendar = Calendar.getInstance()
//        calendar[Calendar.HOUR_OF_DAY] = 16
//        calendar[Calendar.MINUTE] = 12
//        calendar[Calendar.SECOND] = 0
        val alarmStartTime = Calendar.getInstance()
        //val now = Calendar.getInstance()
        alarmStartTime[Calendar.HOUR_OF_DAY] = 15
        alarmStartTime[Calendar.MINUTE] = 59
        alarmStartTime[Calendar.SECOND] = 0
//        if (now.after(alarmStartTime)) {
////            Log.d("Hey", "Added a day")
//            alarmStartTime.add(Calendar.DATE, 1)
//        }

        alarmManager.setRepeating(AlarmManager.RTC,alarmStartTime.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)


    }



















    private fun getLoction() {
        Dexter.withContext(this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener{
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        updateLocation()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@MainActivity,"You must accept this permission.",Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                        p1!!.continuePermissionRequest()
                    }
                }).check()
    }




    private fun updateLocation() {
        buildLocationRequest()
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationrequest, getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this@MainActivity,MyLocationService::class.java)
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE)
        return  PendingIntent.getBroadcast(this@MainActivity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }


    @Suppress("DEPRECATION")
    private fun buildLocationRequest() {
        locationrequest = LocationRequest()
        locationrequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationrequest.interval = 5000
        locationrequest.fastestInterval = 3000
        locationrequest.smallestDisplacement = 10f
    }

    private fun setupNav() {
        val navController = findNavController(R.id.hostfragment)
        findViewById<BottomNavigationView>(R.id.bottomNav)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE

    }


}