package com.example.my11.API

import com.example.my11.DataClass.Players
import com.example.my11.DataClass.Squad
import com.example.my11.Match
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL="https://cricapi.com/api/"
const val API_KEY="NVvSuM2OIwPanROYT8QS2Y4ihwm1"
interface CricService {

//    https://cricapi.com/api/matchCalendar?apikey=NVvSuM2OIwPanROYT8QS2Y4ihwm1
    @GET("matchCalendar?apikey=$API_KEY")

    fun  matchCalender(@Query("page") page:Int):Call<Match>
 //  https://cricapi.com/api/fantasySquad?apikey=NVvSuM2OIwPanROYT8QS2Y4ihwm1&unique_id=1034809

    @GET("fantasySquad?apikey=$API_KEY")
    fun getSquad(@Query("unique_id") id:String) :Call<Squad>
//  https://cricapi.com/api/playerStats?apikey=NVvSuM2OIwPanROYT8QS2Y4ihwm1&pid=329336

    @GET("playerStats?apikey=$API_KEY")
    fun getPlayerDetails(@Query("pid")id:String) : Call<Players>

}
 object RetrofitInstance{
     val cricInstance:CricService
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cricInstance=retrofit.create(CricService::class.java)
    }
}
