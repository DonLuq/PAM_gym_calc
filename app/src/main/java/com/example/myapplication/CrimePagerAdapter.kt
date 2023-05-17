//package com.example.myapplication
//
//import android.content.Context
//import android.os.Build
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.CheckBox
//import android.widget.TextView
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.RecyclerView
//import java.time.LocalDate
//import java.time.LocalDateTime
//import java.util.*
//
//class CrimePagerAdapter(context: Context) :
//    RecyclerView.Adapter<CrimePagerAdapter.ViewPagerViewHolder>() {
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    var crimes = DBHandler(context).getCrimes()
//
//    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val titleView: TextView = itemView.findViewById(R.id.crime_title)
////        val detailsView: TextView = itemView.findViewById(R.id.crime_details)
//        val dateView: TextView = itemView.findViewById(R.id.crime_date)
////        val timeView: TextView = itemView.findViewById(R.id.crime_time)
//        val recordFirstDate : TextView = itemView.findViewById(R.id.record_1_date)
//        val recordFirstInfo : TextView = itemView.findViewById(R.id.record_1_info)
//        val recordSecondDate : TextView = itemView.findViewById(R.id.record_2_date)
//        val recordSecondInfo : TextView = itemView.findViewById(R.id.record_2_info)
//        val recordThirdDate : TextView = itemView.findViewById(R.id.record_3_date)
//        val recordThirdInfo : TextView = itemView.findViewById(R.id.record_3_info)
//        val recordFourthDate : TextView = itemView.findViewById(R.id.record_4_date)
//        val recordFourthInfo : TextView = itemView.findViewById(R.id.record_4_info)
//        val recordFiveDate : TextView = itemView.findViewById(R.id.record_5_date)
//        val recordFiveInfo : TextView = itemView.findViewById(R.id.record_5_info)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_crime,parent, false)
//        return ViewPagerViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return crimes.size
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
//        val currentCrime = crimes[position]
//
//        holder.titleView.setText(currentCrime.title)
////        holder.detailsView.setText(currentCrime.details)
//        holder.dateView.setText(LocalDate.now().toString())
////        holder.timeView.setText(LocalDateTime.now().toLocalTime().toString().split(".")[0])
//
//        val currentCrimeSplited_1 = currentCrime.record_1.split(" ")
//        holder.recordFirstInfo.setText(currentCrimeSplited_1[2] + " " + currentCrimeSplited_1[3] + " " +  currentCrimeSplited_1[4])
//        holder.recordFirstDate.setText(currentCrimeSplited_1[0] + " " + currentCrimeSplited_1[1])
//
//        val currentCrimeSplited_2 = currentCrime.record_2.split(" ")
//        holder.recordSecondInfo.setText(currentCrimeSplited_2[2] + " " + currentCrimeSplited_2[3] + " " +  currentCrimeSplited_2[4])
//        holder.recordSecondDate.setText(currentCrimeSplited_2[0] + " " + currentCrimeSplited_2[1])
//
//        val currentCrimeSplited_3 = currentCrime.record_3.split(" ")
//        holder.recordThirdInfo.setText(currentCrimeSplited_3[2] + " " + currentCrimeSplited_3[3] + " " +  currentCrimeSplited_3[4])
//        holder.recordThirdDate.setText(currentCrimeSplited_3[0] + " " + currentCrimeSplited_3[1])
//
//        val currentCrimeSplited_4 = currentCrime.record_4.split(" ")
//        holder.recordFourthInfo.setText(currentCrimeSplited_4[2] + " " + currentCrimeSplited_4[3] + " " +  currentCrimeSplited_4[4])
//        holder.recordFourthDate.setText(currentCrimeSplited_4[0] + " " + currentCrimeSplited_4[1])
//
//        val currentCrimeSplited_5 = currentCrime.record_5.split(" ")
//        holder.recordFiveInfo.setText(currentCrimeSplited_5[2] + " " + currentCrimeSplited_5[3] + " " +  currentCrimeSplited_5[4])
//        holder.recordFiveDate.setText(currentCrimeSplited_5[0] + " " + currentCrimeSplited_5[1])
//
//    }
//
//    fun refreshList(list: LinkedList<Crime>){
//        crimes = list
//        notifyDataSetChanged()
//    }
//}