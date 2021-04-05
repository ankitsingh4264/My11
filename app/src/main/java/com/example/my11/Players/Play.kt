package com.example.my11.Players

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my11.DataClass.Matche
import com.example.my11.DataClass.Players

import com.example.my11.Players.slected.c
import com.example.my11.R
import com.example.my11.Utils
import kotlinx.android.synthetic.main.fragment_play.*


class Play : Fragment(),TeamAdapter.onitemClick {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    private  lateinit var playViewMode:PlayViewModel
    private lateinit var team1Players:ArrayList<Players>
    private lateinit var team2Players:ArrayList<Players>
    private lateinit var adapter1:TeamAdapter
    private lateinit var adapter2: TeamAdapter
    private lateinit var currMatch:Matche
    var team1name:String="";
    var team2name:String="";
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        team1Players=ArrayList();
        team2Players=ArrayList();
        playViewMode= ViewModelProvider(requireActivity()).get(PlayViewModel::class.java)
        currMatch= Utils.FutureMatchtoPlay!!
        team1name=currMatch.team1
        team2name=currMatch.team2

        team1.text=currMatch.team1
        team2.text=currMatch.team2
        step_progress.currentProgress=0;
        playViewMode.getTeams(currMatch.unique_id)
        playViewMode.teams.observe(requireActivity(),
        Observer { it ->

            //team 1
            playViewMode.getPlayersDetails1(it[0].players)
            playViewMode.playerinfo1.observe(requireActivity(), Observer {
                 team1Players=it
                for (player in team1Players) player.teamName=team1name

                if (team2Players.size!=0){
                    Log.i("anki",team1Players.size.toString()+" "+team2Players.size)
                    Log.i("anki",team1Players.toString())
                    Log.i("anki",team2Players.toString())

                    adapter1= TeamAdapter(requireContext(),team1Players,this)
                    recycler_view_team_1.apply {
                        adapter=adapter1
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                    }
                    adapter2= TeamAdapter(requireContext(),team2Players,this)
                    recycler_view_team_2.apply {
                        adapter=adapter2
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                    }


                }
            })

            //team2
            playViewMode.getPlayersDetails2(it[1].players)
            playViewMode.playerinfo2.observe(requireActivity(), Observer {
                 team2Players=it
                for (player in team2Players) player.teamName=team2name
                if (team1Players.size!=0){
                    Log.i("anki",team1Players.size.toString()+" "+team2Players.size)
                    Log.i("anki",team1Players.toString())
                    Log.i("anki",team2Players.toString())
                    adapter1= TeamAdapter(requireContext(),team1Players,this)
                    recycler_view_team_1.apply {
                        adapter=adapter1
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                    }
                    adapter2= TeamAdapter(requireContext(),team2Players,this)
                    recycler_view_team_2.apply {
                        adapter=adapter2
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                    }

                }


            })



        })


    }




    @SuppressLint("ResourceAsColor")
    override fun onItemClicked(position: Int, team: String) {
        val prec=c;
        if (team==team1name){
            team1Players.get(position).selected= !team1Players.get(position).selected
            if ( team1Players.get(position).selected) c++;
            else c--;
            adapter1.notifyItemChanged(position)

        }else{
            team2Players.get(position).selected= !team2Players.get(position).selected
            if (  team2Players.get(position).selected ) c++;
            else c--;
            adapter2.notifyItemChanged(position)
        }
    if (c==3 || prec==3){
        adapter1.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
        if (prec!=3)
        btn_fab.setBackgroundColor(Color.parseColor("#980202"))
        else btn_fab.setBackgroundColor(Color.parseColor("#D3D3D3"))

    }
        step_progress.currentProgress=c;


    }


}
object slected{
    var c=0;
}
