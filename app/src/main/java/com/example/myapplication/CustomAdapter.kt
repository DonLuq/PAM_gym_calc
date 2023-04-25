package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.*

class CustomAdapter(private var mList: List<Crime>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    fun refreshList (list: LinkedList<Crime>){
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

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.titleView.setText(ItemsViewModel.title)

        holder.dateView.setText(ItemsViewModel.record_1.split(" ")[0] +" " + ItemsViewModel.record_1.split(" ")[1])

        val currentDate = LocalDate.now()
        val crimeDate = ItemsViewModel.record_1.split(" ")[0].split("-")
//        if (currentDate.dayOfMonth == Integer.parseInt(crimeDate[2])){
//            holder.clickedView.setBackgroundColor(0x00FF00);
//            holder.clickedView.setText("TRUE")
//        }
//        else{
//            holder.clickedView.setBackgroundColor(0xFF0000);
//            holder.clickedView.setText("FALSE")
//        }

        val recordText = ItemsViewModel.record_1.split(" ")
        holder.recordView.setText(recordText[2] + " " + recordText[3] + " " + recordText[4])
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val titleView: TextView = itemView.findViewById(R.id.crimeTitle)
//        val clickedView: TextView = itemView.findViewById(R.id.crimeSolved)
        val dateView: TextView = itemView.findViewById(R.id.crimeDate)
        val recordView: TextView = itemView.findViewById(R.id.crimeRecord)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }
        }

    }
}

