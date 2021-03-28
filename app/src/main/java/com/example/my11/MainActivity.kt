package com.example.my11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.my11.API.CricService
import com.example.my11.API.RetrofitInstance
import com.example.my11.DataClass.Squad
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setSupportActionBar(toolbar)
        val navController=findNavController(R.id.hostfragment);
        bottomNav.setupWithNavController(navController);

           Repository().getSquad("1243394")

    }

//    private fun getMatch() {
//         val res=RetrofitInstance.cricInstance.matchCalender(1)
//        res.enqueue(object : retrofit2.Callback<Match>{
//            override fun onResponse(call: Call<Match>, response: Response<Match>) {
//                  val result=response.body()
//
//                    Log.i("ankit",response.toString())
//            }
//
//            override fun onFailure(call: Call<Match>, t: Throwable) {
//                Log.i("ankit","${t.message}")
//            }
//        })
//    }


}