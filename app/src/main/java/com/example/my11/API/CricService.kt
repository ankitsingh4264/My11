package com.example.my11.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL="https://cricapi.com/api/"
const val API_KEY="NVvSuM2OIwPanROYT8QS2Y4ihwm1"
interface CricService {
//    https://cricapi.com/api/matchCalendar?apikey=NVvSuM2OIwPanROYT8QS2Y4ihwm1

    @GET("matchCalender?apiKey=${API_KEY}")
    fun  matchCalender()

}
object RetrofitInstance{
    lateinit var cricInstance:CricService
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cricInstance=retrofit.create(CricService::class.java)
    }
}
