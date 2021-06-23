package com.example.my11.beans

data class Predicted(
        var predictedPlayers: HashMap<String, Int> = HashMap(),
        var predictedTeam: String = "",
        var matchId: String = "",
        var dateTimeGMT: String?="",
        var winnerTeam: String?="",
        var team1:String?="",
        var team2:String?="",
        var points:Int=0

)