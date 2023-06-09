package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_crime.*
import kotlinx.android.synthetic.main.activity_crime.view.*
import java.util.*
import kotlin.system.exitProcess


@RequiresApi(Build.VERSION_CODES.O)
class CrimeActivity : AppCompatActivity() {
    val bundle : Bundle? by lazy { intent.extras }
    val dataBase = DBHandler(this)
    val startPosition : String by lazy {bundle!!.getString("POSITION_VIEWPAGER").toString()}
    val DATE_DEV : String by lazy {bundle!!.getString("DATE").toString()}
    private lateinit var initValues: List<String>
    private lateinit var currentPosition : String
    val listOfRecords: List<EditText> by lazy {
        listOf(
            findViewById(R.id.record_entry_1),
            findViewById(R.id.record_entry_2),
            findViewById(R.id.record_entry_3),
            findViewById(R.id.record_rep_1),
            findViewById(R.id.record_rep_2),
            findViewById(R.id.record_rep_3)
        )
    }

    val viewPager : ViewPager2 by lazy {findViewById(R.id.view_pager)}
    val adapter by lazy { CrimePagerAdapter(this, DATE_DEV) }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager_layout)

        viewPager.adapter = adapter
        viewPager.currentItem = startPosition.toInt()
        initValues = getValueList(adapter.exercises[viewPager.currentItem]) // Update initValues based on the selected page

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                initValues = getValueList(adapter.exercises[viewPager.currentItem]) // Update initValues based on the selected page
                currentPosition = viewPager.currentItem.toString()
            }
        })
    }

    override fun onBackPressed() {
        //TODO Add popup if changes are not saved "Are you sure?"
        setResult(0)
        finish()
    }

    fun getValueList(): List<String> {
        val listValues = mutableListOf<String>()
        for (editText in listOfRecords) {
            listValues.add(editText.text.toString())
        }
        return listValues.toList()
    }

    fun getValueList(obj: Exercise): List<String> {
        var returnObj = obj.weight.split(" ") + obj.repetitions.split(" ")
        while (returnObj.size < 6){
            returnObj += ""
        }
        return returnObj
    }

    fun firstItem(view: View) {
        viewPager.currentItem = 0
    }

    fun lastItem(view: View) {
        viewPager.currentItem = adapter.itemCount-1
    }

    fun doDataChange(view: View) {
        currentPosition = viewPager.currentItem.toString()
        if (isChanged()){
            if (isEmpty()){
                val name = adapter.exercises[currentPosition.toInt()].name
                val weight = getValueList().subList(0,3).joinToString(" ")
                val reps = getValueList().subList(3,6).joinToString(" ")
                dataBase.addExercise(name,DATE_DEV,weight, reps)
            }
            else{
                val uuid = adapter.exercises[currentPosition.toInt()].uuid
                val name = adapter.exercises[currentPosition.toInt()].name
                val weight = getValueList().subList(0,3).joinToString(" ")
                val reps = getValueList().subList(3,6).joinToString(" ")
                dataBase.updateExercise(uuid,name,DATE_DEV,weight,reps)
            }
            //TODO Check if it maintains the changes
            adapter.exercises[viewPager.currentItem].weight = ""
            adapter.exercises[viewPager.currentItem].repetitions = ""
            adapter.exercises[viewPager.currentItem].date = Calendar.DATE.toString() //it's not in use btw
            initValues = getValueList(adapter.exercises[viewPager.currentItem])
//            adapter.notifyItemChanged(viewPager.currentItem)
            adapter.notifyDataSetChanged() // WA due to instability with changes (every second save is working) //TODO Find proper solution
        }
        else{
            Log.i("UP","isCHANGED false")
            //No change, so no reaction
        }
    }

    fun isEmpty(): Boolean {
        for (element in initValues){
            if (element != ""){
                return false
            }
        }
        return true
    }
    fun isChanged(): Boolean {
        var currentList : List<String> = getValueList()
        Log.i("UP",currentList.toString() + "CHANGED")
        Log.i("UP",initValues.toString() + "CHANGED")
        Log.i("UP",(currentList != initValues).toString())
        return currentList != initValues
    }

    fun deleteRecord(view: View) {
        currentPosition = viewPager.currentItem.toString()
        val uuid = adapter.exercises[currentPosition.toInt()].uuid
        Log.i("UP7",initValues.toString())
        dataBase.deleteExercise(uuid)
        adapter.exercises[currentPosition.toInt()].weight = "  "
        adapter.exercises[currentPosition.toInt()].repetitions = "  "
        adapter.exercises[currentPosition.toInt()].date = ""
        adapter.notifyItemChanged(viewPager.currentItem)
        initValues = getValueList(adapter.exercises[currentPosition.toInt()])
    }
}