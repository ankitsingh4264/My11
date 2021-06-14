package com.example.my11.Matches

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my11.beans.Matche
import com.example.my11.R
import com.example.my11.Utils
import kotlinx.android.synthetic.main.list_of_future_match.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FutureMatchAdapter(val list: ArrayList<Matche>, val clickListener: onitemClick) : RecyclerView.Adapter<viewholder1>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder1 {
        return viewholder1(LayoutInflater.from(parent.context).inflate(R.layout.list_of_future_match, parent, false))
    }

    override fun getItemCount(): Int {
        Log.i("info", "${list.size}")
        return list.size
    }

    override fun onBindViewHolder(holder: viewholder1, position: Int) {
        holder.bind(list[position],position,clickListener)
    }
    interface onitemClick{
        fun onItemClicked(position: Int)
    }

}
class viewholder1(itemView: View) :RecyclerView.ViewHolder(itemView){
    fun bind(data: Matche, position: Int, clickListener:FutureMatchAdapter.onitemClick) {
        with(itemView){
            team1.text=data.team1
            team2.text=data.team2
            val dateString = getfulldateinUTC(data.dateTimeGMT)
            date_text_view.text= getdate(data.dateTimeGMT)
            time_text_view.text=gettime(dateString!!.getDateWithServerTimeStamp().toString())
            if(Utils.SetofplayedMatches.contains(data.unique_id))
            {
                this.rl_future_match.setBackgroundResource(R.drawable.faded_cardview)
            }
            else
            {
                this.rl_future_match.setBackgroundResource(R.drawable.new_match_cardview)
            }
            this.cardViewFutureMatch.setOnClickListener {
                clickListener.onItemClicked(position)
            }

        }

    }

    fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
        try {
            return dateFormat.parse(this)
        } catch (e: ParseException) {
            return null
        }
    }


    private fun gettime(dateTimeGMT: String): String? {
        return getTime12hour(dateTimeGMT.substring(11,20))
    }

    private fun getTime12hour(t: String): String? {
        val hr=t.substring(0,2).toInt()
        return ((hr % 12).toString() + ":" + t.substring(3,5) + " " + if (hr >= 12) "PM" else "AM")
    }

    private fun getdate(dateTimeGMT: String): String? {
        return dateTimeGMT.substring(0, 10)
    }

    private fun getfulldateinUTC(dateTimeGMT: String): String? {
        return dateTimeGMT.substring(0)
    }





}


