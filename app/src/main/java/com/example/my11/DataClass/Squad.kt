package com.example.my11.DataClass

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
    val playingRole:String?
)