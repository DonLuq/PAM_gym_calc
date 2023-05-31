package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.example.myapplication.DBHandler
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlotActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot) // Assuming your layout file is named activity_plot.xml

        // Find the GraphView in your layout
        val graphView = findViewById<GraphView>(R.id.graph_view)

        // Create an instance of your DBHandler class
        val dbHandler = DBHandler(this)

        // Retrieve data from the database
        val exercises = dbHandler.getExercises()

        // Log the size of the retrieved data
        Log.d("PlotActivity", "Number of exercises: ${exercises.size}")

        // Create a map to hold the series for each exercise type
        val exerciseSeriesMap = mutableMapOf<String, MutableList<DataPoint>>()

        // Create a date formatter
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for (exercise in exercises) {
            // Convert weight and repetitions to lists of Double
            val weightList = exercise.weight.split(" ").mapNotNull { it.toDoubleOrNull() }
            val repetitionsList = exercise.repetitions.split(" ").mapNotNull { it.toDoubleOrNull() }

            // If weight or repetitions is not numeric, show an error message and skip this exercise
            if (weightList.size != repetitionsList.size) {
                Log.e("PlotActivity", "Error: Mismatch in number of weights and repetitions for exercise ${exercise.id}")
            } else {
                val progress = weightList.zip(repetitionsList).sumByDouble { it.first * it.second }

                // Convert the exercise date to a LocalDate
                val exerciseDate = LocalDate.parse(exercise.date, formatter)

                // Add the data point to the series for this exercise type
                val dataPoint = DataPoint(exerciseDate.toEpochDay().toDouble(), progress)
                exerciseSeriesMap.getOrPut(exercise.name) { mutableListOf() }.add(dataPoint)
            }
        }

        // Log the exercise series map
        for ((type, dataPoints) in exerciseSeriesMap) {
            Log.d("PlotActivity", "Exercise type: $type")
            for (dataPoint in dataPoints) {
                Log.d("PlotActivity", "Data Point: (${dataPoint.x}, ${dataPoint.y})")
            }
        }

        // Adjust the viewport of the graph
        graphView.apply {
            title = "Progress"

            // Set viewport settings
            val dataPoints = exerciseSeriesMap.values.flatten()
            if (dataPoints.isNotEmpty()) {
                viewport.isScalable = true
                viewport.isScrollable = true
                viewport.isXAxisBoundsManual = true
                viewport.isYAxisBoundsManual = true

                // Adjust x-axis range with some padding
                val minX = dataPoints.minOf { it.x }
                val maxX = dataPoints.maxOf { it.x }
                val xRangePadding = (maxX - minX) * 0.1 // 10% padding on both sides
                val xMin = minX - xRangePadding
                val xMax = maxX + xRangePadding

                // Adjust y-axis range to start from zero
                val minY = dataPoints.minOf { it.y }
                val maxY = dataPoints.maxOf { it.y }
                val yRangePadding = (maxY - minY) * 0.1 // 10% padding on both sides
                val yMin = minY - yRangePadding
                val yMax = maxY + yRangePadding

                viewport.setMinX(xMin)
                viewport.setMaxX(xMax)
                viewport.setMinY(yMin)
                viewport.setMaxY(yMax)
            }
        }



        // Add a series for each exercise type
        for ((type, dataPoints) in exerciseSeriesMap) {
            // Sort the data points by date
            val sortedDataPoints = dataPoints.sortedBy { it.x }

            // Calculate the moving average
            val movingAverageDataPoints = sortedDataPoints.windowed(5, 1) { window ->
                val yValues = window.map { it.y }
                DataPoint(window.first().x, yValues.sum() / window.size)
            }

            // Log the moving average data points
            Log.d("PlotActivity", "Moving average data points for exercise type $type:")
            for (dataPoint in movingAverageDataPoints) {
                Log.d("PlotActivity", "Data Point: (${dataPoint.x}, ${dataPoint.y})")
            }

            // Create a LineGraphSeries and add it to the graph
            val series = LineGraphSeries(movingAverageDataPoints.toTypedArray())
            series.title = type
            graphView.addSeries(series)
        }
    }
}
