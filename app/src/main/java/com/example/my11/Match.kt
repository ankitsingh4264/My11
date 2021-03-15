package com.example.my11

data class Match (
    val creditsLeft: Int,
    val provider:Provider,
    val ttl:Int,
    val v:Int,
    val cache:Boolean,
    val data:ArrayList<Data>
)

data class Provider(
    val source: String,
    val url:String,
    val pubDdate:String
)

data class Data(
    val unique_id: String,
    val name: String,
    val date: String
)