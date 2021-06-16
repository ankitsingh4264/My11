package com.example.my11.Players

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my11.beans.Players
import com.example.my11.Players.slected.c
import com.example.my11.R
import kotlinx.android.synthetic.main.list_of_two_teams.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class TeamAdapter(val context: Context,var list:ArrayList<Players>,val clickListener: onitemClick) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view= LayoutInflater.from(context).inflate(R.layout.list_of_two_teams,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position),position)


    }

    override fun getItemCount(): Int {
        return list.size;
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        @SuppressLint("ResourceAsColor")
        fun bind(currPlayer:Players, position: Int){
            val bsr=calbsr(currPlayer)
            val bowlbsr=calbowlsr(currPlayer)
            if(currPlayer.name==null)
                itemView.name_of_player.text="N/A"
            else
                itemView.name_of_player.text=currPlayer.name
            itemView.bowlsr.text=bowlbsr
            itemView.batsr.text=bsr
            if (currPlayer.imageURL!=null){
                Glide.with(context).load(currPlayer.imageURL)
                        .into(itemView.image_of_player)
            }

            if (currPlayer.selected) {
                itemView.team_players_card_view.setCardBackgroundColor(Color.parseColor("#FFFCC9"))
                itemView.img_add.setImageResource(R.drawable.remove_circle)
                Log.i("ankitt",currPlayer.toString())

            }else{
                itemView.team_players_card_view.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                itemView.img_add.setImageResource(R.drawable.add_circle)
                if (c==3){
                    itemView.team_players_card_view.setCardBackgroundColor(Color.parseColor("#D3D3D3"))
                }
            }


            itemView.img_add.setOnClickListener {

                if (c==3 ){
                    if (!currPlayer.selected) return@setOnClickListener

                }
                Log.i("ank",currPlayer.selected.toString())
                clickListener.onItemClicked(position,currPlayer.teamName!!)


            }




        }



    }
    fun calbsr(players: Players): String? {
        Log.i("anki",players.toString())
        var tot=0F;
        var runs=0F;
        if (players.data ==  null) return "0";
        //lista
        if (players.data.batting.listA!=null && (players.data.batting.listA.Runs!=null && !players.data.batting.listA.Runs.equals("-"))){
            tot+=players.data.batting.listA.Mat.toFloat()
            runs+=players.data.batting.listA.Runs!!.toFloat()

        }
        //Firstclass
        if (players.data.batting.firstClass!=null &&( players.data.batting.firstClass.Runs!=null && !players.data.batting.firstClass.Runs.equals("-"))){
            tot+=players.data.batting.firstClass.Mat.toFloat()
            runs+=players.data.batting.firstClass.Runs!!.toFloat()

        }
        //T20
        if (players.data.batting.T20Is!=null && (players.data.batting.T20Is.Runs!=null && !players.data.batting.T20Is.Runs.equals("-"))){
            tot+=players.data.batting.T20Is.Mat.toFloat()
            runs+=players.data.batting.T20Is.Runs!!.toFloat()

        }
        //odi
        if (players.data.batting.ODIs!=null &&(players.data.batting.ODIs.Runs!=null && !players.data.batting.ODIs.Runs.equals("-"))){
            tot+=players.data.batting.ODIs.Mat.toFloat()
            runs+=players.data.batting.ODIs.Runs!!.toFloat()

        }

        //test

        if (players.data.batting.tests!=null &&( players.data.batting.tests.Runs!=null && !players.data.batting.tests.Runs.equals("-"))){
            tot+=players.data.batting.tests.Mat.toFloat()
            runs+=players.data.batting.tests.Runs!!.toFloat()

        }

        val sr:Float =(runs)/(tot)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(sr)
    }
    fun calbowlsr(players: Players): String? {
        Log.i("anki",players.toString())
        var tot=0F;
        var runs=0F;
        if (players.data==null) return "0"
        //lista
        if (players.data.bowling.listA!=null && (players.data.bowling.listA.Wkts!=null && !players.data.bowling.listA.Wkts.equals("-"))){
            tot+=players.data.bowling.listA.Mat.toFloat()
            runs+=players.data.bowling.listA.Wkts!!.toFloat()

        }
        //Firstclass
        if (players.data.bowling.firstClass!=null && (players.data.bowling.firstClass.Wkts!=null && !players.data.bowling.firstClass.Wkts.equals("-"))){
            tot+=players.data.bowling.firstClass.Mat.toFloat()
            runs+=players.data.bowling.firstClass.Wkts!!.toFloat()

        }
        //T20
        if (players.data.bowling.T20Is!=null && (players.data.bowling.T20Is.Wkts!=null && !players.data.bowling.T20Is.Wkts.equals("-"))){
            tot+=players.data.bowling.T20Is.Mat.toFloat()
            runs+=players.data.bowling.T20Is.Wkts!!.toFloat()

        }
        //odi
        if (players.data.bowling.ODIs!=null && (players.data.bowling.ODIs.Wkts!=null && !players.data.bowling.ODIs.Wkts.equals("-"))){
            tot+=players.data.bowling.ODIs.Mat.toFloat()
            runs+=players.data.bowling.ODIs.Wkts!!.toFloat()

        }

        //test

        if (players.data.bowling.tests!=null && (players.data.bowling.tests.Wkts!=null && !players.data.bowling.tests.Wkts.equals("-"))){
            tot+=players.data.bowling.tests.Mat.toFloat()
            runs+=players.data.bowling.tests.Wkts!!.toFloat()

        }

        val sr:Float =(runs)/(tot)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(sr)
    }
    interface onitemClick{
        fun onItemClicked(position: Int,team:String)
    }

}