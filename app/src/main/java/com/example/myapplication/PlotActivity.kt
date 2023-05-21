package com.example.myapplication

import android.graphics.BitmapFactory
    import android.os.Build
    import android.os.Bundle
    import android.widget.ImageView
    import androidx.annotation.RequiresApi
    import androidx.appcompat.app.AppCompatActivity
    import jetbrains.letsPlot.export.ggsave
    import jetbrains.letsPlot.geom.geom_point
    import jetbrains.letsPlot.lets_plot
    import java.io.File
    import com.example.myapplication.DBHandler

    class PlotActivity : AppCompatActivity() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_plot)

            // Create an instance of your DBHandler class
            val dbHandler = DBHandler(this)

            // Retrieve data from the database
            val exercises = dbHandler.getExercises()

            // Extract the data you want to plot
            val xData = exercises.map { it.date }
            val yData = exercises.map { it.weight }

            // Format the data as a Map object
            val data = mapOf<String, Any>(
                "x" to xData,
                "y" to yData
            )

            // Create a plot using the Lets-Plot API
            val plot = lets_plot(data) { x = "x"; y = "y" } + geom_point()

            // Save the plot as a PNG image to a temporary file
            val file = File.createTempFile("plot", ".png")
            ggsave(plot, file.absolutePath)

            // Load the image from the temporary file into a Bitmap object
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)

            // Create an ImageView component and set the Bitmap as its content
            val imageView = ImageView(this)
            imageView.setImageBitmap(bitmap)

            // Set the ImageView as the content view of the activity
            setContentView(imageView)
        }
    }
