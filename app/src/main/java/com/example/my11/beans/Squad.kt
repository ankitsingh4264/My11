package com.example.my11.beans

data class Squad (
    val squad : ArrayList<Team>
)

data class Team(
    val name:String,
    val players: ArrayList<Players>
)
data class Players(
    val pid:String,
    val name:String,
    val playingRole:String?,
    val data:Dataa,
    val imageURL:String?,
    var selected:Boolean=false,
    var teamName:String?

)
data class Dataa(
    val bowling:playType,
    val batting:playType
)
data class playType(
        val listA:playDetail,
        val firstClass:playDetail,
        val T20Is:playDetail,
        val ODIs:playDetail,
        val tests:playDetail,

)
data class playDetail(
        val Wkts:String?="0",
        val Mat :String="0",
        val Runs:String?="0"
)