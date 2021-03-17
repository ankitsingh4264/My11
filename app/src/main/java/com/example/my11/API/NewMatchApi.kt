package com.example.my11.API

import com.example.my11.DataClass.NewMatch
import com.example.my11.Match
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL_NEW_MATCH="https://cricapi.com/api/"
const val API_KEY_NEW_MATCH="06Yltmr68xaBEf7eBsHttOW1jPy2"

interface NewMatchApi {
    //NVvSuM2OIwPanROYT8QS2Y4ihwm1
    //    https://cricapi.com/api/matches?apikey=06Yltmr68xaBEf7eBsHttOW1jPy2
    @GET("matches?apikey=$API_KEY_NEW_MATCH")

    fun  matches(@Query("page") page:Int): Call<NewMatch>

}
object RetrofitInstance_NewMatch{
    val cricInstanceforNewMatchApi:NewMatchApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_NEW_MATCH)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        cricInstanceforNewMatchApi=retrofit.create(NewMatchApi::class.java)
    }
}