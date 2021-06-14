package com.example.my11.Players

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.beans.Players
import com.example.my11.beans.Team
import com.example.my11.Repository

class PlayViewModel : ViewModel() {
    private  val repo:Repository= Repository();

    var teams: MutableLiveData<ArrayList<Team>> = MutableLiveData()
    var playerinfo1:MutableLiveData<ArrayList<Players>> = MutableLiveData();
    var playerinfo2:MutableLiveData<ArrayList<Players>> = MutableLiveData();
    fun getTeams(matchId:String){
        teams=repo.getSquad(matchId);
    }
    fun getPlayersDetails1(players: ArrayList<Players>) {
        playerinfo1=repo.getPlayerDetails(players)
    }
    fun getPlayersDetails2(players: ArrayList<Players>) {
        playerinfo2=repo.getPlayerDetails(players)
    }



}