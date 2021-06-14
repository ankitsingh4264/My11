package com.example.my11.Players

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my11.beans.Matche
import com.example.my11.beans.Players
import com.example.my11.beans.Predicted
import com.example.my11.Players.slected.c
import com.example.my11.R
import com.example.my11.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.winner_team_cardview.view.*


class Play : Fragment(),TeamAdapter.onitemClick {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    private  lateinit var playViewMode:PlayViewModel
    private lateinit var team1Players:ArrayList<Players>
    private lateinit var team2Players:ArrayList<Players>
    private lateinit var adapter1:TeamAdapter
    private lateinit var adapter2: TeamAdapter
    private lateinit var currMatch:Matche
    var count:Int = 0
    var team1name:String="";
    var team2name:String="";
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        c=0;
//        showProgress(true)
        team1Players=ArrayList();
        requireActivity().bottomNav.visibility=View.GONE
        team2Players=ArrayList();
        playViewMode= ViewModelProvider(requireActivity()).get(PlayViewModel::class.java)
        currMatch= Utils.FutureMatchtoPlay!!
        count = Utils.CountForNullPlayer!!
        team1name=currMatch.team1
        team2name=currMatch.team2

        team1.text=currMatch.team1
        team2.text=currMatch.team2
        step_progress.currentProgress=0
        var flag=0

        playViewMode.getTeams(currMatch.unique_id)
        playViewMode.teams.observe(requireActivity(),
            Observer { it ->

                //team 1
                playViewMode.getPlayersDetails1(it[0].players)
                playViewMode.playerinfo1.observe(requireActivity(), Observer {
                    team1Players = it
                    for (player in team1Players) {
                        player.teamName = team1name
                        if (player.name == null) count++;
                        Log.i("rajeev", count.toString())
                    }
                    if (count > 5) {
                        flag = 1;
                    } else if (team2Players.size != 0) {
//                        Log.i("anki", team1Players.size.toString() + " " + team2Players.size)
//                        Log.i("anki", team1Players.toString())
//                        Log.i("anki", team2Players.toString())



                        adapter1 = TeamAdapter(requireContext(), team1Players, this)
                        recycler_view_team_1.apply {
                            adapter = adapter1
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                        adapter2 = TeamAdapter(requireContext(), team2Players, this)
                        recycler_view_team_2.apply {
                            adapter = adapter2
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }


                    }
                })

                //team2
                playViewMode.getPlayersDetails2(it[1].players)
                playViewMode.playerinfo2.observe(requireActivity(), Observer {
                    team2Players = it
                    for (player in team2Players) {
                        player.teamName = team2name
                        if (player.name == null) count++;
                        Log.i("rajeev", count.toString())
                    }
                    if (count > 5) {
                        flag = 1
                        Toast.makeText(
                            requireActivity(),
                            "This match can't be Played.",
                            Toast.LENGTH_SHORT
                        ).show()
                        animationView.visibility=View.GONE
                        view.findNavController().navigate(R.id.action_play_to_homeFragment)
                    } else if (team1Players.size != 0) {
//                        Log.i("anki", team1Players.size.toString() + " " + team2Players.size)
//                        Log.i("anki", team1Players.toString())
//                        Log.i("anki", team2Players.toString())
                        animationView.visibility=View.GONE
                        adapter1 = TeamAdapter(requireContext(), team1Players, this)
                        recycler_view_team_1.apply {
                            adapter = adapter1
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                        adapter2 = TeamAdapter(requireContext(), team2Players, this)
                        recycler_view_team_2.apply {
                            adapter = adapter2
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }

                    }


                })

                btn_fab.setOnClickListener {

                    playClick();

                }

            })




    }

    private fun playClick() {
        if (c!=3) {
            Toast.makeText(requireActivity(),"Complete Your Team",Toast.LENGTH_SHORT).show()
            return
        }
        showDialog()
    }

      var predictedTeam:String="";

     fun showDialog(){

         val mDialogView = LayoutInflater.from(requireContext()).inflate(
                 R.layout.winner_team_cardview,
                 null
         )
         mDialogView.winner_team_1_tv.text=team1name
         mDialogView.winner_team_2_tv.text=team2name
         val dialogBuilder=AlertDialog.Builder(requireActivity()).setView(mDialogView);

         val alertDialog = dialogBuilder.show();
         mDialogView.winner_team_1.setOnClickListener {
             mDialogView.rl_predict.setBackgroundResource(R.drawable.red_border)
            predictedTeam=team1name;
             mDialogView.winner_team_1.setBackgroundResource(R.drawable.faded_one_side);
             mDialogView.winner_team_2.setBackgroundResource(R.color.white);

         }

         mDialogView.winner_team_2.setOnClickListener {
             mDialogView.rl_predict.setBackgroundResource(R.drawable.red_border)
             predictedTeam=team2name;
             mDialogView.winner_team_1.setBackgroundResource(R.color.white);
             mDialogView.winner_team_2.setBackgroundResource(R.drawable.faded_one_side_2);

         }


         mDialogView.rl_predict.setOnClickListener {

           //place the bet

             if (predictedTeam=="") return@setOnClickListener
             val p:HashMap<String,Int> = HashMap();
             for (player in team1Players) {
                 if (player.selected) p.put(player.pid,0);
             }
             for (player in team2Players) {
                 if (player.selected) p.put(player.pid,0);
             }
             val currPredicted:Predicted= Predicted(p,predictedTeam,Utils.FutureMatchtoPlay!!.unique_id, Utils.FutureMatchtoPlay!!.dateTimeGMT,team1 = team1name,team2 = team2name)
             Utils.prediction=currPredicted
             alertDialog.dismiss()
             view?.findNavController()!!.navigate(R.id.action_play_to_completed)


         }
         mDialogView.rl_cancel.setOnClickListener {
             //dismiss
             alertDialog.dismiss()
         }





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
