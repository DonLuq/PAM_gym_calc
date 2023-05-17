package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.media.MediaDrm
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_crime.*
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
public class CrimeActivity : AppCompatActivity() {
    val bundle : Bundle? by lazy { intent.extras }
    val title : TextView by lazy { findViewById(R.id.crime_title) }
    val date : TextView by lazy { findViewById(R.id.crime_date) }
//    val time : TextView by lazy { findViewById(R.id.crime_time) }
//    val record_1 : EditText by lazy { findViewById(R.id.record_entry_1) }
//    val record_2 : EditText by lazy { findViewById(R.id.record_entry_2) }
//    val record_3 : EditText by lazy { findViewById(R.id.record_entry_3) }
    val UUID : String by lazy {bundle!!.getString("UUID").toString()}


    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    //Add to date to support comparison of results
    val hour = c.get(Calendar.HOUR)
    val minute = c.get(Calendar.MINUTE)
    val second = c.get(Calendar.SECOND)

    val viewPager : ViewPager2 by lazy {findViewById(R.id.view_pager)}
//    val adapter by lazy { CrimePagerAdapter(this) }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager_layout)

//        viewPager.adapter = adapter
        Log.i("CHECKED","A")
//        viewPager.currentItem = DBHandler(this).getExercisePosition(UUID)
        Log.i("CHECKED","B")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putString("UUID",currentCrime.id)
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
//    fun DeleteCrime(view: View) {
//        DBHandler(this).deleteExercise( adapter.crimes[viewPager.currentItem])
//        val list = DBHandler(this).getExercises()
//        adapter.refreshList(list)
//        viewPager.adapter?.notifyDataSetChanged()
//        finish()
//    }

//    override fun onPause() {
//        val currentHolder : Crime = adapter.crimes[viewPager.currentItem]
////        currentHolder.title = title.text.toString()
////        currentHolder.date = date.text.toString() + " " + time.text.toString()
////        currentHolder.details = details.text.toString()
//        DBHandler(this).updateExercise(currentHolder)
//        super.onPause()
//    }


    override fun onBackPressed() {
        onPause()
        super.onBackPressed()
    }

    fun changeDate(view: android.view.View) {
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view : DatePicker, year : Int, month : Int, day : Int ->
                var tempText : String = ""

                tempText += year.toString() + "-"

                if (month < 9){
                    tempText += "0" + (month + 1).toString() + "-"
                }
                else{
                    tempText += (month + 1).toString() + "-"
                }

                if (day < 9){
                    tempText += "0" + day.toString()
                }
                else{
                    tempText += day.toString()
                }
                date.text = tempText

            },year,month,day)

            dpd.show()
    }

    //TO DO: Add block with time change
//    fun changeTime(view: android.view.View) {
//        val tpt = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay : Int, minute : Int ->
//            var tempText : String = ""
//
//            if (hourOfDay < 10){
//                tempText += "0" + (hourOfDay).toString() + ":"
//            }
//            else{
//                tempText += (hourOfDay).toString() + ":"
//            }
//
//            if (minute < 10){
//                tempText += "0" + minute.toString()
//            }
//            else{
//                tempText += minute.toString()
//            }
//
//            tempText += ":00"
//            time.text = tempText
//
//        },hour,minute,true)
//
//            tpt.show()
//    }

    fun firstCrime(view: android.view.View) {
        viewPager.setCurrentItem(0)
    }

//    fun lastCrime(view: android.view.View) {
//        viewPager.setCurrentItem(adapter.itemCount-1)
//    }

//    fun addRecord(view: android.view.View) {
//        val stringToSend : String = date.text.toString() + " " + time.text.toString() + " " + record_1.text.toString() + " " + record_2.text.toString() + " " + record_3.text.toString()
//        val currentHolder : Crime = adapter.crimes[viewPager.currentItem]
//        DBHandler(this).addRecord(currentHolder, stringToSend)
//        val list = DBHandler(this).getCrimes()
//        adapter.refreshList(list)
//    }


}