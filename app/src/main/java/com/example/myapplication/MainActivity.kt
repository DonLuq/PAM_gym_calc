package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    val startActivitySources : Bundle? by lazy { intent.extras }
    val DEV_TMP_DATE : String by lazy {startActivitySources!!.getString("DATE_FROM_PICK").toString()}
//    val DEV_TMP_DATE = "YYYY-MM-DD"
    val recyclerview : RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    val DBHandler by lazy { DBHandler(this) }
    lateinit var data : List<Exercise>
    val adapter by lazy { CustomAdapter(data) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // data -> list<Exercise> with same date property
        data = DBHandler.getListOfElementsByDate(DEV_TMP_DATE)
        Log.d("xdd",data[0].weight)
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(object: CustomAdapter.onItemClickListener {
            override fun onItemClick(position: Int){
                val intent = Intent(this@MainActivity,CrimeActivity::class.java)

                intent.putExtra("UUID", data[position].uuid)
                startActivity(intent)
            }
        })

        val searchView : SearchView = findViewById(R.id.search_bar)
        searchView.onActionViewExpanded()

        //TODO Check if correct db handler is in use.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
//                    searchBase(newText)
                }
                return false
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        Log.i("CHECKED","ONRESUME_MAIN")
        super.onResume()
        val list = DBHandler.getListOfElementsByDate(DEV_TMP_DATE)
        adapter.refreshList(list)
        recyclerview.adapter?.notifyDataSetChanged()
    }

//     @SuppressLint("NotifyDataSetChanged")
//     @RequiresApi(Build.VERSION_CODES.O)
//     fun CreateCrime(view: android.view.View) {
//        val newCrime = Crime()
//
//        DBHandler.addCrime(newCrime)
//        val list = DBHandler.getCrimes()
//        adapter.refreshList(list)
//        recyclerview.adapter?.notifyDataSetChanged()
//
//        val intent = Intent(this, CrimeActivity::class.java)
//        intent.putExtra("UUID", newCrime.uuid) //TODO TUTAJ JEST PROBLEM Z NIEISTNIEJACYM ID -> SOLVED: przekazywanie uuid z ktorego w kolejnej aktywnosci odczytywane jest ID
//        startActivity(intent)
//    }

//    fun searchBase(newText : String) {
//        val list = DBHandler.searchExercise(newText)
//        adapter.refreshList(list)
//        recyclerview.adapter?.notifyDataSetChanged()
//    }


}