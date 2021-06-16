package com.example.my11.Matches

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my11.beans.Predicted
import com.example.my11.R
import kotlinx.android.synthetic.main.played_match_card_view.view.*

class CompletedMatchAdapter(val context:Context,var list: ArrayList<Predicted>) : RecyclerView.Adapter<CompletedMatchAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.played_match_card_view,parent,false)
        return ViewHolder(view)
    }

    //@RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position,list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        //@RequiresApi(Build.VERSION_CODES.N)
        fun bind(position: Int, completedMatch: Predicted) {

                itemView.team1.text = list.get(position).team1
                itemView.team2.text = list[position].team2
               if (list[position].winnerTeam==list[position].predictedTeam){
                   itemView.win_loss.text="Win"
                   itemView.win_loss.setTextColor(Color.parseColor("#25ba38"))
               }else{
                   itemView.win_loss.setTextColor(Color.parseColor("#980202"))
                   itemView.win_loss.text="Loss"

               }
            var pts=0;
            for (p in list[position].predictedPlayers!!.keys)
                pts+=list[position].predictedPlayers!!.getOrDefault(p,0)

            itemView.points.text="+$pts"


        }


    }
}