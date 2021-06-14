package com.example.my11.beans

import com.google.gson.annotations.SerializedName

data class NewMatch(
    val creditsLeft: Int,
    val matches: List<Matche>,
    val provider: Provider,
    val ttl: Int,
    val v: String
)

data class Matche(
    val date: String,
    val dateTimeGMT: String,
    val matchStarted: Boolean,
    val squad: Boolean,
    @SerializedName("team-1")
    val team1: String,
    @SerializedName("team-2")
    val team2: String,
    val toss_winner_team: String,
    val type: String,
    val unique_id:String,
    val winner_team: String,
    val predicted_Winner_team:String,
    val selectedPlayers:HashMap<String,Int>
)



