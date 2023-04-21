package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

class DBHandler(context: Context) : SQLiteOpenHelper (
    context, DATABASE_NAME, null, DATABASE_VERSION
        ) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "crimeDB8.db"
        private const val TABLE_CRIME = "CrimeTable"
        private const val COLUMN_ID = "_ID"
        private const val COLUMN_UUID = "_UUID"
        private const val COLUMN_TITLE = "TITLE"
        private const val COLUMN_DETAILS = "DETAILS"
        private const val COLUMN_DATA_1 = "RECORD_1"
        private const val COLUMN_DATA_2 = "RECORD_2"
        private const val COLUMN_DATA_3 = "RECORD_3"
        private const val COLUMN_DATA_4 = "RECORD_4"
        private const val COLUMN_DATA_5 = "RECORD_5"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CRIME_TABLE = "CREATE TABLE $TABLE_CRIME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_UUID TEXT,$COLUMN_TITLE TEXT, $COLUMN_DETAILS TEXT, $COLUMN_DATA_1 TEXT, $COLUMN_DATA_2 TEXT, $COLUMN_DATA_3 TEXT, $COLUMN_DATA_4 TEXT, $COLUMN_DATA_5 TEXT)"

        if (db != null) {
            db.execSQL(CREATE_CRIME_TABLE)
//            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CRIME")

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CRIME")
        onCreate(db)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun getCrimes() : LinkedList<Crime>{
        val crimes : LinkedList<Crime> = LinkedList()
        val selectQuery = "SELECT * FROM $TABLE_CRIME"

        val db = this.readableDatabase

        val cursor : Cursor = db.rawQuery(selectQuery, null)

        var id : Int
        var uuid : String
        var title : String
        var details : String
        var data_1 : String
        var data_2 : String
        var data_3 : String
        var data_4 : String
        var data_5 : String

        Log.i("CHECK","CURSOR")
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex((COLUMN_ID)))
                uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UUID))
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                details = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))
                data_1 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_1))
                data_2 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_2))
                data_3 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_3))
                data_4 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_4))
                data_5 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_5))


                val crime : Crime = Crime()
                crime.id = id
                crime.uuid = uuid
                crime.title = title
                crime.details = details
                crime.record_1 = data_1
                crime.record_2 = data_2
                crime.record_3 = data_3
                crime.record_4 = data_4
                crime.record_5 = data_5

                crimes.add(crime)

            }while (cursor.moveToNext())
        }

        db.close()
        cursor.close()
        return crimes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCrime(crime : Crime){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_UUID,crime.uuid)
        contentValues.put(COLUMN_TITLE,crime.title)
        contentValues.put(COLUMN_DETAILS,crime.details)
        contentValues.put(COLUMN_DATA_1,crime.record_1)
        contentValues.put(COLUMN_DATA_2,crime.record_2)
        contentValues.put(COLUMN_DATA_3,crime.record_3)
        contentValues.put(COLUMN_DATA_4,crime.record_4)
        contentValues.put(COLUMN_DATA_5,crime.record_5)

        db.insert(TABLE_CRIME, null, contentValues)

        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCrimePosition(UUID : String): Int {
        val crimes : LinkedList<Crime> = getCrimes()

        var position : Int = 0;
        for (crime in crimes) {
            if (crime.uuid.equals(UUID)) {
                return position;
            }
            position++;
        }
        return 0;


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteCrime(crime : Crime){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID,crime.id)
        db.delete(TABLE_CRIME, COLUMN_UUID + "=\"" + crime.uuid + "\"", null)
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCrime(crime : Crime){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_UUID,crime.uuid)
        contentValues.put(COLUMN_TITLE,crime.title)
        contentValues.put(COLUMN_DETAILS,crime.details)

        db.update(TABLE_CRIME, contentValues, COLUMN_UUID + "=\"" + crime.uuid + "\"", null)

        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRecord(crime: Crime, record : String){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_DATA_1, record)
        contentValues.put(COLUMN_DATA_2,crime.record_1)
        contentValues.put(COLUMN_DATA_3,crime.record_2)
        contentValues.put(COLUMN_DATA_4,crime.record_3)
        contentValues.put(COLUMN_DATA_5,crime.record_4)

        db.update(TABLE_CRIME, contentValues, COLUMN_UUID + "=\"" + crime.uuid + "\"", null)

        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun searchName(query : String) : LinkedList<Crime>{
        val crimes : LinkedList<Crime> = LinkedList()
        val selectQuery = "SELECT * FROM $TABLE_CRIME WHERE $COLUMN_TITLE LIKE + \"$query%\" "

        val db = this.readableDatabase

        val cursor : Cursor = db.rawQuery(selectQuery, null)
        var id : Int
        var uuid : String
        var title : String
        var details : String
        var data_1 : String
        var data_2 : String
        var data_3 : String
        var data_4 : String
        var data_5 : String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex((COLUMN_ID)))
                uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UUID))
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                details = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS))
                data_1 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_1))
                data_2 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_2))
                data_3 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_3))
                data_4 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_4))
                data_5 = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_5))

                val crime : Crime = Crime()
                crime.id = id
                crime.uuid = uuid
                crime.title = title
                crime.details = details
                crime.record_1 = data_1
                crime.record_2 = data_2
                crime.record_3 = data_3
                crime.record_4 = data_4
                crime.record_5 = data_5

                crimes.add(crime)

            }while (cursor.moveToNext())
        }

        db.close()
        cursor.close()
        return crimes
    }
}