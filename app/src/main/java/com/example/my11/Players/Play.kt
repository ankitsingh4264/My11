package com.example.my11.Players

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my11.DataClass.Players
import com.example.my11.R


class Play : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    private  lateinit var playViewMode:PlayViewModel
    private lateinit var team1Players:ArrayList<Players>
    private lateinit var team2Players:ArrayList<Players>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        team1Players=ArrayList();
        team2Players=ArrayList();
        playViewMode= ViewModelProvider(requireActivity()).get(PlayViewModel::class.java)

        playViewMode.getTeams("1243394")
        playViewMode.teams.observe(requireActivity(),
        Observer { it ->

            //team 1
            playViewMode.getPlayersDetails1(it[0].players)
            playViewMode.playerinfo1.observe(requireActivity(), Observer {
                 team1Players=it

                if (team2Players.size!=0){
                    Log.i("anki",team1Players.size.toString()+" "+team2Players.size)
                    Log.i("anki",team1Players.toString())
                    Log.i("anki",team2Players.toString())

                }
            })

            //team2
            playViewMode.getPlayersDetails2(it[1].players)
            playViewMode.playerinfo2.observe(requireActivity(), Observer {
                 team2Players=it
                if (team1Players.size!=0){
                    Log.i("anki",team1Players.size.toString()+" "+team2Players.size)
                    Log.i("anki",team1Players.toString())
                    Log.i("anki",team2Players.toString())

                }


            })



        })


    }


}