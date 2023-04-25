package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    val DEV_TMP_DATE = "YYYY-MM-DD"
    val recyclerview : RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    val DBHandler by lazy { DBHandler(this) }
    lateinit var data : LinkedList<Crime>
    val adapter by lazy { CustomAdapter(data) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.i("CHECKED","ONCREATE_MAIN")
        setContentView(R.layout.activity_main)

        data = DBHandler.getCrimes()
        //TO DO
        /* Nowy handler do bazy danych musi zwraca dla podanej daty ostatnie recordy, jezeli kilka to zwraca ten z wyzszym ID \
        zwracane recordy sa w formie  LinkedList<Crime> gdzie Crime to dany typ cwiczenia, brak cwiczenia w bazie ma zwracac \
        objekt Crime z nazwą cwiczenia oraz zdefiniowanymi pustymi "" brakujacymi atrybutami, mozesz pozmieniac nazwy ale zostawiam to narazie\
        do czasu refactoru */

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(object: CustomAdapter.onItemClickListener {
            override fun onItemClick(position: Int){
                val intent = Intent(this@MainActivity,CrimeActivity::class.java)

                intent.putExtra("UUID", DBHandler.getCrimes()[position].uuid) // Napisac funkcje DBhandler do wyciągania pojedynczej linijki z danym UUID(to taki powiedzmy hash)
                startActivity(intent)
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
                    searchBase(newText)
                }
                return false
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        Log.i("CHECKED","ONRESUME_MAIN")
        super.onResume()
        val list = DBHandler.getCrimes()
        adapter.refreshList(list)
        recyclerview.adapter?.notifyDataSetChanged()
    }

     @SuppressLint("NotifyDataSetChanged")
     @RequiresApi(Build.VERSION_CODES.O)
     fun CreateCrime(view: android.view.View) {
        val newCrime = Crime()

        DBHandler.addCrime(newCrime)
        val list = DBHandler.getCrimes()
        adapter.refreshList(list)
        recyclerview.adapter?.notifyDataSetChanged()

        val intent = Intent(this, CrimeActivity::class.java)
        intent.putExtra("UUID", newCrime.uuid) //TODO TUTAJ JEST PROBLEM Z NIEISTNIEJACYM ID -> SOLVED: przekazywanie uuid z ktorego w kolejnej aktywnosci odczytywane jest ID
        startActivity(intent)
    }

    fun searchBase(newText : String) {
        val list = DBHandler.searchName(newText)
        adapter.refreshList(list)
        recyclerview.adapter?.notifyDataSetChanged()
    }


}