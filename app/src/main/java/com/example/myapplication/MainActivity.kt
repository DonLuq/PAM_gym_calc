package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private val startActivitySources: Bundle? by lazy { intent.extras }
    private val DEV_TMP_DATE: String by lazy { startActivitySources!!.getString("DATE_FROM_PICK").toString() }
    private lateinit var recyclerview: RecyclerView
    private var data: List<Exercise>? = null
    private lateinit var adapter: CustomAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Create an instance of the adapter
        adapter = CustomAdapter(data ?: emptyList())

        // Set the click listener for the adapter
        adapter.setOnItemClickListener(object : CustomAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, CrimeActivity::class.java)
                intent.putExtra("DATE", DEV_TMP_DATE)
                intent.putExtra("POSITION_VIEWPAGER", position.toString())
                startActivity(intent)
                finish() // Workaround to handle the back button behavior
            }
        })

        // Set the adapter on the RecyclerView
        recyclerview.adapter = adapter

        // Initialize the search view
        val searchView: SearchView = findViewById(R.id.search_bar)
        searchView.onActionViewExpanded()

        // Set the query text listener for the search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchFilter(newText)
                } else {
                    adapter.refreshList(data ?: emptyList())
                }
                return false
            }
        })

        // Load data from the database
        loadData()
    }

    /**
     * Filter the data based on the search query.
     */
    private fun searchFilter(newText: String) {
        val newList: List<Exercise> = data?.filter { it.name.contains(newText) } ?: emptyList()
        adapter.refreshList(newList)
        recyclerview.adapter?.notifyDataSetChanged()
    }

    /**
     * Load data from the database and update the adapter.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val dbHandler = DBHandler(this)
        data = dbHandler.getListOfElementsByDate(DEV_TMP_DATE)
        adapter.refreshList(data ?: emptyList())
        recyclerview.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
