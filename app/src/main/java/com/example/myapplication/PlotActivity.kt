package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.myapplication.DBHandler
import java.time.LocalDate

class PlotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create an instance of your DBHandler class
        val dbHandler = DBHandler(this)

        // Retrieve data from the database
        val exercises = dbHandler.getExercises()

        // Extract the data you want to plot and convert it to an array of DataPoint objects
        val dataPoints = exercises.map { exercise ->
            val date = LocalDate.parse(exercise.date)
            val xValue = date.toEpochDay()
            DataPoint(xValue.toDouble(), exercise.weight.toDouble())
        }.toTypedArray()

        // Create a LineGraphSeries using the data points
        val series = LineGraphSeries(dataPoints)

        // Create a GraphView and add the series to it
        val graphView = GraphView(this).apply {
            addSeries(series)
            title = "Weight"
        }

        // Set the GraphView as the content view of the activity
        setContentView(graphView)
    }
}
