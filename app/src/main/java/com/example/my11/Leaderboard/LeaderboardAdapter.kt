package com.example.my11.Leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my11.R
import com.example.my11.beans.User
import kotlinx.android.synthetic.main.fragment_leaderboard.view.*
import kotlinx.android.synthetic.main.leaderboard_list.view.*
import kotlinx.android.synthetic.main.list_of_two_teams.view.*
import java.util.*

class LeaderboardAdapter (val list: ArrayList<User>) : RecyclerView.Adapter<viewholder1>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder1 {
        return viewholder1(LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder1, position: Int) {
        holder.bind(list[position])
    }
}
class viewholder1(itemView: View) : RecyclerView.ViewHolder(itemView){
    var c:Int=0

    fun bind(data: User) {

        with(itemView){

                if(adapterPosition==0 || adapterPosition==1 || adapterPosition==2)
                {
                    itemView.visibility = View.GONE
                    itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
                else
                {
                    if (data.picture!=null){
                        Glide.with(context).load(data.picture)
                            .into(itemView.player_image)
                    }
                    serial_number_leaderboard.text=(adapterPosition+1).toString()
                    name_leaderboard.text=data!!.name
                    score_leaderboard.text=data!!.totalPoints.toString()
                }

            }


    }



}


