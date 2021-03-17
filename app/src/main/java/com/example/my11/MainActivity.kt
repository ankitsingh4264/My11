package com.example.my11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.my11.API.RetrofitInstance
import com.example.my11.API.RetrofitInstance_NewMatch
import com.example.my11.DataClass.Matche
import com.example.my11.DataClass.NewMatch
import com.example.my11.Matches.FutureMatchAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        getMatch()


    }

    private fun getMatch() {


//        val res= RetrofitInstance_NewMatch.cricInstanceforNewMatchApi.matches(1)
//        res.enqueue(object : retrofit2.Callback<NewMatch>{
//            override fun onResponse(call: Call<NewMatch>, response: Response<NewMatch>) {
//                    val result=response.body()?.matches
//                //Log.i("raj", result.toString())
//
//                for (i in 0 .. result!!.size - 1)
//                {
//                    if(result?.get(i).matchStarted)
//                    {
//                        FutureMatch.add(result?.get(i))
//                    }
//
//
//                }
//                Log.i("Raj",FutureMatch.size.toString())
//                Log.i("Raj",result!!.size.toString())
//                val mAdapter=FutureMatchAdapter(FutureMatch)
//                recycler.adapter=mAdapter
//            }
//
//            override fun onFailure(call: Call<NewMatch>, t: Throwable) {
//                Log.i("ankit",t.message.toString())
//            }
//
//        })





         val res= RetrofitInstance.cricInstance.matchCalender(1)
        res.enqueue(object : retrofit2.Callback<Match>{
            override fun onResponse(call: Call<Match>, response: Response<Match>) {
                  val result=response.body()

                    Log.i("ankit",response.toString())
            }

            override fun onFailure(call: Call<Match>, t: Throwable) {
                Log.i("ankit","${t.message}")
            }
        })
    }
}