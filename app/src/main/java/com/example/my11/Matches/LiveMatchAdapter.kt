package com.example.my11.Matches

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my11.beans.Matche
import com.example.my11.R
import kotlinx.android.synthetic.main.list_of_future_match.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
            val dateString = getfulldateinUTC(data.dateTimeGMT)
//            val date = dateString.getDateWithServerTimeStamp()
//            val x=getdate(data.dateTimeGMT)
            date_text_view.text= getdate(data.dateTimeGMT)
            time_text_view.text=gettime(dateString!!.getDateWithServerTimeStamp().toString())
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
//    /** OUTPUT **/
//    String To Date Conversion Tue Jan 09 15:06:41 GMT+08:00 2018
//    Date To String Conversion 2018-01-09T07:06:41.747Z
//


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