package com.example.my11.Matches

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my11.DataClass.CompletedMatch
import com.example.my11.DataClass.Predicted
import com.example.my11.PlayedMatch
import com.example.my11.Repository

class PlayedMatchVIewModel : ViewModel() {
    private  val repo: Repository = Repository();
    var playedMatch:MutableLiveData<ArrayList<Predicted>>  = MutableLiveData();
    var completedMatch:MutableLiveData<ArrayList<CompletedMatch>>  = MutableLiveData();

    fun getPlayedMatch(){
        playedMatch=repo.getPredictedMatches()

    }
    fun getCompletedMatch(matches:ArrayList<Predicted>){
        completedMatch=repo.getCompletedMatches(matches)
    }
}