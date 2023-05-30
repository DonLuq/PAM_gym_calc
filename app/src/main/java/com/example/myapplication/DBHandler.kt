package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

class DBHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "exerciseDBtest.db"

        // Table and column names
        private const val TABLE_EXERCISE = "ExerciseTable"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_UUID = "_UUID"
        private const val COLUMN_EXERCISE = "Name"
        private const val COLUMN_DATE = "Date"
        private const val COLUMN_WEIGHT = "Weight"
        private const val COLUMN_REPETITIONS = "Repetitions"

        // List of possible exercises
        val POSSIBLE_EXERCISES = listOf(
            "Squats", "Deadlifts", "Bench Press", "Overhead Press", "Rows", "Lunges", "Push-ups",
            "Pull-ups", "Dips", "Chin-ups", "Calf Raises", "Planks", "Sit-ups", "Crunches",
            "Leg Press", "Leg Curls", "Leg Extensions", "Bicep Curls", "Tricep Extensions", "Lateral Raises"
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {
        // Define the SQL query for creating the exercise table
        val CREATE_EXERCISE_TABLE =
            "CREATE TABLE $TABLE_EXERCISE($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_UUID TEXT, $COLUMN_EXERCISE TEXT, $COLUMN_DATE TEXT, $COLUMN_WEIGHT TEXT, $COLUMN_REPETITIONS TEXT)"



        // Execute the SQL query to create the table
        db?.execSQL(CREATE_EXERCISE_TABLE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // If the old version and new version don't match, drop the current table and create a new one
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE")
        onCreate(db)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addExercise(name: String?, date: String?, weight: String?, repetitions: String?) {
        // Check that the input values are valid
        require(!name.isNullOrEmpty()) { "Exercise name must not be null or empty" }
        requireNotNull(date) { "Date must not be null" }
//        require(weight != null && weight > 0) { "Weight must be a positive integer" }
//        require(repetitions != null && repetitions > 0) { "Repetitions must be a positive integer" }

        // Generate a random UUID
        val uuid = UUID.randomUUID().toString()

        // Create a new ContentValues object and add values to it
        val values = ContentValues().apply {
            put(COLUMN_UUID, uuid)
            put(COLUMN_EXERCISE, name)
            put(COLUMN_DATE, date)
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_REPETITIONS, repetitions)
        }

        // Get a writable database and insert the values into the table
        writableDatabase.use { db ->
            db.insert(TABLE_EXERCISE, null, values)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExercises(): List<Exercise> {
        val exerciseList = mutableListOf<Exercise>()

        // Get a readable database and query all rows from the exercise table
        readableDatabase.use { db ->
            val cursor: Cursor? = db.query(
                TABLE_EXERCISE, null, null, null, null, null, "$COLUMN_DATE DESC"
            )

            // Iterate over the cursor and create Exercise objects from the rows
            cursor?.use {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID))
                    val exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                    val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
                    val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

                    // Create an Exercise object and add it to the list
                    val exerciseObj = Exercise(id, uuid, exercise, date, weight, repetitions)
                    exerciseList.add(exerciseObj)
                }
            }
        }

        return exerciseList
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteExercise(uuid: String?) {
        // Check that the UUID is not null or empty
        require(!uuid.isNullOrEmpty()) { "Exercise UUID must not be null or empty" }

        // Get a writable database and delete the exercise record with the given UUID
        writableDatabase.use { db ->
            db.delete(TABLE_EXERCISE, "$COLUMN_UUID = ?", arrayOf(uuid))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExercise(
        uuid: String?,
        name: String?,
        date: String?,
        weight: String?,
        repetitions: String?
    ) {
        // Check that the UUID and input values are valid
        require(!uuid.isNullOrEmpty()) { "Exercise UUID must not be null or empty" }
        require(!name.isNullOrEmpty()) { "Exercise name must not be null or empty" }
        requireNotNull(date) { "Date must not be null" }
        require(weight != null && weight > 0.toString()) { "Weight must be a positive integer" }
        require(repetitions != null && repetitions > 0.toString()) { "Repetitions must be a positive integer" }

        // Create a new ContentValues object and add updated values to it
        val values = ContentValues().apply {
            put(COLUMN_EXERCISE, name)
            put(COLUMN_DATE, date)
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_REPETITIONS, repetitions)
        }

        // Get a writable database and update the exercise record with the given UUID
        writableDatabase.use { db ->
            db.update(TABLE_EXERCISE, values, "$COLUMN_UUID = ?", arrayOf(uuid))
        }
    }

//    fun getExercisePosition(id: Int): Int {
//        // First, we retrieve a list of all exercises using the `getExercises()` function.
//        val exercises: LinkedList<Exercise> = getExercises()
//
//        // We initialize a variable `position` to 0, which will be incremented as we iterate through the list of exercises.
//        var position: Int = 0
//
//        // We loop through the list of exercises using a `for` loop.
//        // For each exercise in the list, we check if its ID matches the `id` parameter we passed into the function.
//        // If we find a match, we return the current value of `position`, which represents the position of the exercise in the list.
//        // If we don't find a match, we increment the `position` variable and continue to the next exercise.
//        for (exercise in exercises) {
//            if (exercise.id == id) {
//                return position
//            }
//            position++
//        }
//
//        // If we reach the end of the loop without finding a match, we return 0, which represents an invalid position.
//        return 0
//    }

//    fun updateExercise(exerciseRecord: Exercise) {
//        // Open a writable database connection.
//        val db = this.writableDatabase
//
//        // Create a new ContentValues object to store the updated column values.
//        val contentValues = ContentValues()
//        contentValues.put(COLUMN_EXERCISE, exerciseRecord.name)
//        contentValues.put(COLUMN_DATE, exerciseRecord.date)
//        contentValues.put(COLUMN_WEIGHT, exerciseRecord.weight)
//        contentValues.put(COLUMN_REPETITIONS, exerciseRecord.repetitions)
//
//        // Use the update() method of SQLiteDatabase to update the row in the database with the
//        // specified id.
//        db.update(
//            TABLE_EXERCISE, // The name of the table to update.
//            contentValues, // The ContentValues object containing the updated column values.
//            "$COLUMN_ID=?", // The selection criteria for the rows to update.
//            arrayOf(exerciseRecord.id.toString()) // The selection criteria parameters.
//        )
//
//        // Close the database connection.
//        db.close()
//    }

//    fun updateExerciseRecord(
//        id: Int,
//        uuid: UUID,
//        exercise: String,
//        date: String,
//        weight: String,
//        repetitions: String )
//    {
//        // Open a writable database connection.
//        val db = this.writableDatabase
//
//        // Create a new ContentValues object to hold the updated values.
//        val contentValues = ContentValues()
//
//        // Put the updated values into the ContentValues object.
//        contentValues.put(COLUMN_EXERCISE, exercise)
//        contentValues.put(COLUMN_DATE, date)
//        contentValues.put(COLUMN_WEIGHT, weight)
//        contentValues.put(COLUMN_REPETITIONS, repetitions)
//
//        // Update the exercise record in the database.
//        // The COLUMN_ID=? and arrayOf(id.toString()) arguments specify the row to update based on the id.
//        db.update(TABLE_EXERCISE, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))
//
//        // Close the database connection.
//        db.close()
//    }


//    fun searchExercise(query: String): LinkedList<Exercise> {
//        // Initialize a linked list to store the search results.
//        val exercises: LinkedList<Exercise> = LinkedList()
//
//        // Construct the SQL select query to retrieve the rows whose exercise name starts with the query string.
//        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_EXERCISE LIKE \"$query%\""
//
//        // Open a readable database connection.
//        val db = this.readableDatabase
//
//        // Execute the select query.
//        val cursor: Cursor = db.rawQuery(selectQuery, null)
//
//        // Initialize variables to store the column values of the retrieved rows.
//        var id: Int
//        var uuid: String
//        var exercise: String
//        var date: String
//        var weight: String
//        var repetitions: String
//
//        // Iterate through the rows returned by the query and extract their column values.
//        if (cursor.moveToFirst()) {
//            do {
//                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
//                uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UUID))
//                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
//                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
//                weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
//                repetitions = cursor.getString(cursor.getColumnIndex(COLUMN_REPETITIONS))
//
//                // Create a new Exercise object with the extracted column values and add it to the results list.
//                val exerciseRecord = Exercise(id, uuid, exercise, date, weight, repetitions)
//                exercises.add(exerciseRecord)
//
//            } while (cursor.moveToNext())
//        }
//
//        // Close the database connection and the cursor.
//        db.close()
//        cursor.close()
//
//        // Return the list of search results.
//        return exercises
//    }

    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    /**
     * Retrieves a list of exercises from the SQLite database ordered by date,
     * and returns only the latest exercise for each unique date.
     * If there are no exercises for a particular date, a default exercise with empty properties is added to the list.
     */

    fun getListOfElementsByDate(date: String): List<Exercise> {
        // Create a map to store the exercises sorted by date
        val exercisesByDate: MutableMap<String, Exercise> = mutableMapOf()

        // SQL query to retrieve all exercises from the database ordered by date in descending order
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_DATE = ? ORDER BY $COLUMN_ID DESC"

        // Open a readable database connection and execute the query
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Loop through each row returned by the query
        while (cursor.moveToNext()) {
            // Extract the values of each column for the current row
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID)) ?: UUID.randomUUID().toString()
            val exerciseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

            // Create an Exercise object with the extracted values
            val exerciseRecord = Exercise(id, uuid, exerciseName, date, weight, repetitions)

            // Add the Exercise object to the map, using the UUID or exercise name as the key
            if (uuid != null && uuid.isNotBlank()) {
                exercisesByDate[uuid] = exerciseRecord
            } else {
                if (!exercisesByDate.containsKey(exerciseName)) {
                    exercisesByDate[exerciseName] = exerciseRecord
                }
            }
        }

        // Filter out duplicate exercise names, keeping the latest one
        val latestExercises = exercisesByDate.values.groupBy { it.name }
            .mapValues { (_, v) -> v.maxByOrNull { it.date }!! }

        Log.d("TAG", "Latest exercises: $latestExercises")

        // Create a default exercise with empty properties
        val defaultExercise = Exercise((0..10).random(), "empty", "empty", "empty", "empty", "empty")

        // Create a new list to store the latest exercises
        val result = mutableListOf<Exercise>()

        // Add the latest exercise for each unique date to the list
        var counter = 0
        for (exerciseName in POSSIBLE_EXERCISES) {
            val selectQuery2 = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_EXERCISE   = '$exerciseName' AND $COLUMN_DATE = '$date' ORDER BY $COLUMN_ID DESC"
            val cursor2: Cursor = db.rawQuery(selectQuery2, null)
            val exercise = if (cursor2.moveToFirst()) {
                val id = cursor2.getInt(cursor2.getColumnIndexOrThrow(COLUMN_ID))
                val uuid = cursor2.getString(cursor2.getColumnIndexOrThrow(COLUMN_UUID)) ?: UUID.randomUUID().toString()
                val exerciseName = cursor2.getString(cursor2.getColumnIndexOrThrow(COLUMN_EXERCISE))
                val date = cursor2.getString(cursor2.getColumnIndexOrThrow(COLUMN_DATE))
                val weight = cursor2.getString(cursor2.getColumnIndexOrThrow(COLUMN_WEIGHT))
                val repetitions = cursor2.getString(cursor2.getColumnIndexOrThrow(COLUMN_REPETITIONS))
                Exercise(id, uuid, exerciseName, date, weight, repetitions)
            } else {
                counter++
                Exercise(id = counter, name = exerciseName)
            }
            result.add(exercise)
            cursor2.close()
            Log.d("TAG", "Exercise added to result: $exercise")
        }

        // Close the database connection and the cursor
        cursor.close()
        db.close()

        // Return the list of latest exercises
        return result
    }

    fun getRecordsByName(exerciseName: String, howMany: Int): List<Exercise> {
        // Create a list to store the exercises
        val exercises = mutableListOf<Exercise>()

        // SQL query to retrieve the latest records for the given exercise name
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_EXERCISE = ? ORDER BY $COLUMN_ID DESC LIMIT ?"

        // Open a readable database connection and execute the query
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(exerciseName, howMany.toString()))

        // Loop through each row returned by the query
        while (cursor.moveToNext()) {
            // Extract the values of each column for the current row
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID))
            val exerciseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

            // Create an Exercise object with the extracted values
            val exerciseRecord = Exercise(id, uuid, exerciseName, date, weight, repetitions)

            // Add the Exercise object to the list
            exercises.add(exerciseRecord)
        }

        // Close the database connection and the cursor
        cursor.close()
        db.close()

        // Return the list of exercises
        return exercises
    }

    fun getExercisesByNameAndDateRange(exerciseName: String, startDate: String, endDate: String): List<Exercise> {
        // Create a list to store the exercises
        val exercises = mutableListOf<Exercise>()

        // SQL query to retrieve exercises for the given exercise name and date range
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_EXERCISE = ? AND $COLUMN_DATE BETWEEN ? AND ? ORDER BY $COLUMN_DATE DESC"

        // Open a readable database connection and execute the query
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(exerciseName, startDate, endDate))

        // Loop through each row returned by the query
        while (cursor.moveToNext()) {
            // Extract the values of each column for the current row
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID))
            val exerciseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

            // Create an Exercise object with the extracted values
            val exerciseRecord = Exercise(id, uuid, exerciseName, date, weight, repetitions)

            // Add the Exercise object to the list
            exercises.add(exerciseRecord)
        }

        // Close the database connection and the cursor
        cursor.close()
        db.close()

        // Return the list of exercises
        return exercises
    }


    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    fun getRowById(id: Int): Exercise? {
        // Construct the SQL select query to retrieve the row based on the id.
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_ID = $id"

        // Open a readable database connection.
        val db = this.readableDatabase

        // Execute the select query.
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Initialize variables to store the column values of the retrieved row.
        var id: Int? = null
        var uuid: String? = null
        var exercise: String? = null
        var date: String? = null
        var weight: String? = null
        var repetitions: String? = null

        // If the cursor has a result, extract the column values and store them in the variables.
        if (cursor.moveToFirst()) {
            uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
        }

        // Close the database connection and the cursor.
        db.close()
        cursor.close()

        // Return a new Exercise object if all column values are not null, otherwise return null.
        return if (id != null && exercise != null && date != null && weight != null && repetitions != null) {
            Exercise(id, exercise, date, weight, repetitions)
        } else {
            null
        }
    }

        //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
//    fun getRowByUUID(uuid: UUID): Exercise {
//
//         // Define the SELECT query with placeholders for arguments
//        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_UUID = ?"
//
//        // Define the argument(s) for the query as an array
//        val args = arrayOf(uuid.toString())
//
//        // Get a readable database reference
//        val db = this.readableDatabase
//
//        // Execute the query and get a cursor that points to the result set
//        val cursor = db.rawQuery(selectQuery, args)
//
//        // Declare a variable to hold the row data as an Exercise object
//        var row: Exercise? = null
//
//        // Check if the cursor has any data
//        if (cursor.moveToFirst()) {
//        // If the cursor points to the first row of data, create a new Exercise object with the values from the cursor
//        row = Exercise(
//        uuid  = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID)),
//        exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE)),
//        date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
//        weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
//        repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
//        )
//        }
//
//        // Check if row is still null (i.e. no row was found with the given UUID)
//        if (row == null) {
//        // Throw an exception indicating that no row was found with the given UUID
//        throw Exception("No row found with UUID $uuid")
//        }
//
//        // Close the database and cursor resources
//        db.close()
//        cursor.close()
//
//        // Return the row data as an Exercise object
//        return row
//    }


    /**
     * Retrieves an Exercise object from the database with the specified name and date
     * @param exerciseName the name of the exercise to retrieve
     * @param date the date of the exercise to retrieve
     * @return an Exercise object with the specified name and date, or null if not found
     */
    fun getExerciseByNameAndDate(exerciseName: String, date: String): Exercise? {
        val db = readableDatabase
        var exercise: Exercise? = null

        // Define the columns to retrieve from the database
        val columns = arrayOf(COLUMN_ID, COLUMN_UUID, COLUMN_EXERCISE, COLUMN_DATE, COLUMN_WEIGHT, COLUMN_REPETITIONS)

        // Define the selection criteria
        val selection = "$COLUMN_EXERCISE = ? AND $COLUMN_DATE = ?"

        // Define the selection arguments
        val selectionArgs = arrayOf(exerciseName, date)

        // Execute the query
        val cursor = db.query(TABLE_EXERCISE, columns, selection, selectionArgs, null, null, null)

        // Check if a row was returned
        if (cursor.moveToFirst()) {
            // Map the values from the row to an Exercise object
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
            exercise = Exercise(id, uuid, name, date, weight, repetitions)
        }

        // Close the cursor and database connection
        cursor.close()
        db.close()

        // Return the Exercise object, or null if not found
        return exercise
    }

    fun updateRow(exercise: Exercise) {
        // Get a writable database reference
        val db = this.writableDatabase

        // Define the values to update as a ContentValues object
        val values = ContentValues().apply {
        put(COLUMN_EXERCISE, exercise.name)
        put(COLUMN_DATE, exercise.date)
        put(COLUMN_WEIGHT, exercise.weight)
        put(COLUMN_REPETITIONS, exercise.repetitions)
        }

        // Define the WHERE clause and arguments for the UPDATE query
        val whereClause = "$COLUMN_UUID = ?"
        val whereArgs = arrayOf(exercise.uuid)

        // Execute the UPDATE query
        db.update(TABLE_EXERCISE, values, whereClause, whereArgs)

        // Close the database resource
        db.close()
    }

    fun updateColumn(uuid: UUID, columnName: String, newValue: String) {
        // Get a writable database reference
        val db = this.writableDatabase

        // Define the values to update as a ContentValues object
        val values = ContentValues().apply {
        put(columnName, newValue)
        }

        // Define the WHERE clause and arguments for the UPDATE query
        val whereClause = "$COLUMN_UUID = ?"
        val whereArgs = arrayOf(uuid.toString())

        // Execute the UPDATE query
        db.update(TABLE_EXERCISE, values, whereClause, whereArgs)

        // Close the database resource
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExerciseByUUID(uuid: String): Exercise? {
        // Get a readable database and query the exercise with the given UUID
        readableDatabase.use { db ->
            val cursor: Cursor? = db.query(
                TABLE_EXERCISE, null, "$COLUMN_UUID = ?", arrayOf(uuid), null, null, null
            )

            // Check if a row was found with the given UUID
            if (cursor?.moveToFirst() == true) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
                val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

                // Create and return the Exercise object
                return Exercise(id, uuid, exercise, date, weight, repetitions)
            }
        }

        return null // Return null if no exercise was found with the given UUID
    }


}