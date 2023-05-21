package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CrimePagerAdapter(context: Context, val date: String) :
    RecyclerView.Adapter<CrimePagerAdapter.ViewPagerViewHolder>() {

    val date_dev = date
    val dataBase = DBHandler(context)

    @RequiresApi(Build.VERSION_CODES.O)
    var exercises =
        dataBase.getListOfElementsByDate(date) // Get list of exercises names with values for selected previously date

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.crime_title)
        val dateView: TextView = itemView.findViewById(R.id.crime_date)
        val recordFirstDate: TextView = itemView.findViewById(R.id.record_1_date)
        val recordFirstInfo: TextView = itemView.findViewById(R.id.record_1_info)
        val recordSecondDate: TextView = itemView.findViewById(R.id.record_2_date)
        val recordSecondInfo: TextView = itemView.findViewById(R.id.record_2_info)
        val recordThirdDate: TextView = itemView.findViewById(R.id.record_3_date)
        val recordThirdInfo: TextView = itemView.findViewById(R.id.record_3_info)
        val recordFourthDate: TextView = itemView.findViewById(R.id.record_4_date)
        val recordFourthInfo: TextView = itemView.findViewById(R.id.record_4_info)
        val recordFiveDate: TextView = itemView.findViewById(R.id.record_5_date)
        val recordFiveInfo: TextView = itemView.findViewById(R.id.record_5_info)
        val listOfRecords: List<EditText> = listOf(
            itemView.findViewById(R.id.record_entry_1),
            itemView.findViewById(R.id.record_entry_2),
            itemView.findViewById(R.id.record_entry_3),
            itemView.findViewById(R.id.record_rep_1),
            itemView.findViewById(R.id.record_rep_2),
            itemView.findViewById(R.id.record_rep_3)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_crime, parent, false)
        return ViewPagerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int {
        return exercises.size
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentExercise = exercises[position]
        Log.i("EX_CONT", currentExercise.toString())
        val lastFiveRecords = dataBase.getRecordsByName(currentExercise.name, 5)

        holder.titleView.text = currentExercise.name
        Log.i("DATA_CrimeonBindHolder", date_dev)
        holder.dateView.text = date

        val listOfValues: List<String> =
            currentExercise.weight.split(" ") + currentExercise.repetitions.split(" ")
        Log.i("LISTA_WARTOSCI", listOfValues.toString())

        for ((index, element) in holder.listOfRecords.withIndex()) {
            val value = listOfValues.getOrNull(index) ?: ""
            val editable = Editable.Factory.getInstance().newEditable(value)
            element.text.replace(0, element.text.length, editable)
        }

        //TODO There is no dynamically generated variable names, so try to  make it better later
        if (0 in lastFiveRecords.indices) {
            holder.recordFirstInfo.text =
                displayParseRecord(lastFiveRecords[0].weight, lastFiveRecords[0].repetitions)
            holder.recordFirstDate.text = lastFiveRecords[0].date
        } else {
            holder.recordFirstInfo.text = "no data"
            holder.recordFirstDate.text = "no data"
        }

        if (1 in lastFiveRecords.indices) {
            holder.recordSecondInfo.text =
                displayParseRecord(lastFiveRecords[1].weight, lastFiveRecords[1].repetitions)
            holder.recordSecondDate.text = lastFiveRecords[1].date
        } else {
            holder.recordSecondInfo.text = "no data"
            holder.recordSecondDate.text = "no data"
        }

        if (2 in lastFiveRecords.indices) {
            holder.recordThirdInfo.text =
                displayParseRecord(lastFiveRecords[2].weight, lastFiveRecords[2].repetitions)
            holder.recordThirdDate.text = lastFiveRecords[2].date
        } else {
            holder.recordThirdInfo.text = "no data"
            holder.recordThirdDate.text = "no data"
        }

        if (3 in lastFiveRecords.indices) {
            holder.recordFourthInfo.text =
                displayParseRecord(lastFiveRecords[3].weight, lastFiveRecords[3].repetitions)
            holder.recordFourthDate.text = lastFiveRecords[3].date
        } else {
            holder.recordFourthInfo.text = "no data"
            holder.recordFourthDate.text = "no data"
        }

        if (4 in lastFiveRecords.indices) {
            holder.recordFiveInfo.text =
                displayParseRecord(lastFiveRecords[4].weight, lastFiveRecords[4].repetitions)
            holder.recordFiveDate.text = lastFiveRecords[4].date
        } else {
            holder.recordFiveInfo.text = "no data"
            holder.recordFiveDate.text = "no data"
        }
    }

    fun displayParseRecord(massStr: String, repStr: String): String {
        val separator = " "
        val listOfMasses = massStr.split(separator)
        val listOfReps = repStr.split(separator)

        if (listOfMasses.size != listOfReps.size) {
            throw java.lang.Exception("Required data is invalid, different sizes of lists are prohibited")
        }

        var returnStr = ""
        var index = 0
        while (index < listOfMasses.size) {
            returnStr += listOfMasses[index] + "[kg]:" + listOfReps[index] + "[x]  "
            index++
        }

        return returnStr
    }
}