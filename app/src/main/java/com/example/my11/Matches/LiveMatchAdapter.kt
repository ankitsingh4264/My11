package com.example.my11.Matches

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my11.DataClass.Matche
import com.example.my11.R
import kotlinx.android.synthetic.main.list_of_future_match.view.*

class LiveMatchAdapter(val list: ArrayList<Matche>) : RecyclerView.Adapter<viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.list_of_live_match,parent,false))
    }

    override fun getItemCount(): Int {
        Log.i("info","${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(list[position])
    }

}
class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(data: Matche) {
        with(itemView){
            team1.text=data.team1
            team2.text=data.team2
            date_text_view.text=getdate(data.dateTimeGMT)
            time_text_view.text=gettime(data.dateTimeGMT)
        }

    }

    private fun gettime(dateTimeGMT: String): CharSequence? {
        return dateTimeGMT.substring(11)
    }

    private fun getdate(dateTimeGMT: String): CharSequence? {
        return dateTimeGMT.substring(0,10)
    }
}