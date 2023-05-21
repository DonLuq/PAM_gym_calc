package com.example.myapplication

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CustomAdapter(private var mList: List<Exercise>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    fun refreshList (list: List<Exercise>){
        mList = list
        notifyDataSetChanged()
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view, mListener)
    }


    fun displayParseRecord(massStr : String, repStr : String) : String {
        val separator = " "
        val listOfMasses = massStr.split(separator)
        val listOfReps = repStr.split(separator)

        if (listOfMasses.size != listOfReps.size){
            throw java.lang.Exception("Required data is invalid, different sizes of lists are prohibited")
        }

        var returnStr= ""
        var index = 0
        while (index < listOfMasses.size) {
            returnStr += listOfMasses[index] + "[kg]:" + listOfReps[index] + "[x]  "
            index++
        }

        return returnStr
    }


    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ExerciseItem = mList[position]

        holder.titleView.setText(ExerciseItem.name)
        holder.dateView.setText(ExerciseItem.date)
        holder.recordView.setText(displayParseRecord(ExerciseItem.weight,ExerciseItem.repetitions))
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val titleView: TextView = itemView.findViewById(R.id.crimeTitle)
        val dateView: TextView = itemView.findViewById(R.id.crimeDate)
        val recordView: TextView = itemView.findViewById(R.id.crimeRecord)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }
        }

    }
}

