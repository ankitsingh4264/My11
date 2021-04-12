package com.example.my11.Matches

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my11.Players.PlayViewModel
import com.example.my11.R

class PlayedMatchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_played_match, container, false)
    }

    private lateinit var playedMatchVIewModel:PlayedMatchVIewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playedMatchVIewModel = ViewModelProvider(requireActivity()).get(PlayedMatchVIewModel::class.java)

        playedMatchVIewModel.getPlayedMatch()

        playedMatchVIewModel.playedMatch.observe(requireActivity(), Observer {

            playedMatchVIewModel.getCompletedMatch(it!!)
            playedMatchVIewModel.completedMatch.observe(requireActivity(), Observer {
//                Log.i("ankit",it.toString())
            })
        })

    }

}