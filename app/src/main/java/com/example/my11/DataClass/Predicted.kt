package com.example.my11.DataClass

data class Predicted(
        var predictedPlayers: HashMap<String, Int>? = null,
        var predictedTeam: String = "",
        var matchId: String = "",
        var dateTimeGMT: String?="",

        )