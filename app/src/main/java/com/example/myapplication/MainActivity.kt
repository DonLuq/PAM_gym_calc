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
    val recyclerview : RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    val DBHandler by lazy { DBHandler(this) }
    lateinit var data : List<Exercise>
    val adapter by lazy { CustomAdapter(data) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // data -> list<Exercise> with same date property
        Log.i("MAIN","MAINACTIVITY")
        data = DBHandler.getListOfElementsByDate(DEV_TMP_DATE)
        Log.i("MAIN","MAINACTIV_LOADED")
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(object: CustomAdapter.onItemClickListener {
            override fun onItemClick(position: Int){
                val intent = Intent(this@MainActivity,CrimeActivity::class.java)

                Log.i("POSITION", position.toString())
                intent.putExtra("DATE", DEV_TMP_DATE)
                intent.putExtra("POSITION_VIEWPAGER", position.toString())
                startActivity(intent)
                finish() //TODO Now it's WA for not working properly back button, so back button will move to StartActivity instead of MainActivity it's not a bug, it's a feature
            }
        })

        val searchView : SearchView = findViewById(R.id.search_bar)
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchFilter(newText)
                }
                else{
                    adapter.refreshList(data)
                }
                return false
            }
        })

    }

    fun searchFilter(newText : String) {
        val newList : List<Exercise> = data.filter { it.name.contains(newText) }
        adapter.refreshList(newList)
        recyclerview.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        Log.i("CHECKED","ONRESUME_MAIN")
        super.onResume()
        val list = DBHandler.getListOfElementsByDate(DEV_TMP_DATE)
        adapter.refreshList(list)
        recyclerview.adapter?.notifyDataSetChanged()
    }




}