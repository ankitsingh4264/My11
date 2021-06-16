package com.example.my11.beans

import com.google.gson.annotations.SerializedName

data class CompletedMatch(
	val creditsLeft: Int? = null,
	val data: Details? = null,
	val cache3: Boolean? = null,
	val provider: Provider? = null,
	val V: String? = null,
	val dateTimeGMT: String? = null,
	val ttl: Int? = null
)

data class ManOfTheMatch(
	val name: String? = null,
	val pid: String? = null
)

data class TeamItem(
	val players: List<PlayersItem?>? = null,
	val name: String? = null
)

data class ScoresItem(
	val bowler: String? = null,
	val R: String? = null,
	@SerializedName("dismissal-by")
    val dismissalBy: DismissalBy?=null,
	@SerializedName("0s")
	val jsonMember0s: Int? = null,
	val nB: String? = null,
	val W: String? = null,
	val pid: String? = null,
	val wD: String? = null,
	@SerializedName("6s")
	val jsonMember6s: Int? = null,
	val econ: String? = null,
	val M: String? = null,
	@SerializedName("4s")
	val jsonMember4s: Int? = null,
	val O: String? = null,
	val batsman:String?=null
)

data class Details(
	val batting: List<BattingItem?>? = null,
	val tossWinnerTeam: String? = null,
	val fielding: List<FieldingItem?>? = null,
	val manOfTheMatch: ManOfTheMatch? = null,
	val team: List<TeamItem?>? = null,
	val winner_team: String? = null,
	val matchStarted: Boolean? = null,
	val bowling: List<BowlingItem?>? = null
)

data class FieldingItem(
	val scores: List<ScoresItem?>? = null,
	val title: String? = null
)

data class Provider(
	val source: String? = null,
	val pubDate: String? = null,
	val url: String? = null
)

data class DismissalBy(
	val name: String? = null,
	val pid: String? = null
)

data class BowlingItem(
	val scores: List<ScoresItem?>? = null,
	val title: String? = null
)

data class PlayersItem(
	val name: String? = null,
	val pid: String? = null
)

data class BattingItem(
	val scores: List<ScoresItem?>? = null,
	val title: String? = null
)

