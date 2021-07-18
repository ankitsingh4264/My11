package com.example.my11.Leaderboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.my11.R
import com.example.my11.beans.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import kotlinx.android.synthetic.main.fragment_leaderboard.view.*


class LeaderboardFragment : Fragment() {


    private lateinit var leaderboardmvvm: LeaderboardViewModel
    private lateinit var auth: FirebaseAuth
    lateinit var list:ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
 //       toolbar_text.text="LEADERBOARD"

        activity?.progress_circular!!.visibility=View.VISIBLE

        requireActivity().bottomNav.visibility=View.VISIBLE
        auth = Firebase.auth
        leaderboardmvvm= ViewModelProvider(requireActivity()).get(LeaderboardViewModel::class.java)

        list = ArrayList()

        leaderboard_rv.layoutManager= LinearLayoutManager(context)

        get_list()

    }

    private fun get_list() {

        leaderboardmvvm.gettopuser()
        leaderboardmvvm.toppers.observe(requireActivity(), Observer {
            //for(i in 1..it.size)
            //{
                Log.i("lakki","dwada")
            //}

            if (it[0].picture!=null){
                context?.let { it1 ->
                    Glide.with(it1).load(it[0].picture)
                        .into(first_player)
                }
            }
            name_first_player.text=it[0]!!.name
            points_first.text=it[0]!!.totalPoints.toString()

            if (it[1].picture!=null){
                context?.let { it1 ->
                    Glide.with(it1).load(it[1].picture)
                        .into(second_player)
                }
            }
            name_second_player.text=it[1]!!.name
            points_second.text=it[1]!!.totalPoints.toString()


            if (it[2].picture!=null){
                context?.let { it1 ->
                    Glide.with(it1).load(it[2].picture)
                        .into(third_player)
                }
            }
            name_third_player.text=it[2]!!.name
            points_third.text=it[2]!!.totalPoints.toString()
//
            val mAdapter = LeaderboardAdapter(it)
            leaderboard_rv.apply {
                adapter=mAdapter


            }
            activity?.progress_circular!!.visibility=View.GONE
        })

    }
}