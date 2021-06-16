package com.example.my11

import com.google.gson.annotations.SerializedName

data class PlayedMatch (
    val dateTimeGMT:String,
    val data:ArrayList<Data>
)



data class Data(
    val bowling: ArrayList<Score>,
    val batting: ArrayList<ScoreBat>,
    val date: String
)

data class ScoreBat(
    val scores:ArrayList<ScoreBatData>
)

data class ScoreBatData
(        @SerializedName("dismissal-by")
        val dismissalby:p?,
        val R:String="0"
)

data class Score(

 val scores:ArrayList<ScoreData>

)
data class p(
        val name:String,
        val pid:String
)

data class ScoreData(
        val W:String="0"

)