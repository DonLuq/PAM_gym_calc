package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

data class Exercise(
    var id: Int,
    var uuid: String = UUID.randomUUID().toString(),
    var exercise: String,
    var date: String = "2000-12-12 13:20:00",
    var weight: String = "10 10 10",
    var repetitions: String = "15 15 15",

) {
    constructor(exercise: Exercise) : this(
        exercise.id,
        exercise.uuid ?: UUID.randomUUID().toString(),
        exercise.exercise,
        exercise.date,
        exercise.weight,
        exercise.repetitions
    )

    fun setId(Id: Int) {
        id = Id
    }

    fun getId(): Int {
        return id
    }

    fun setUUID(UUID: String?) {
        if (UUID != null) {
            uuid = UUID
        }
    }

    fun getUUID(): String? {
        return uuid
    }

    fun setExercise(e: String?) {
        if (e != null) {
            exercise = e
        }
    }
    fun getExercise(): String? {
        return exercise
    }

    fun setDate(s: String?) {
        if (s != null) {
            date = s
        }
    }
    fun getDate(): String? {
        return date
    }

    fun setWeight(w: String?) {
        if (w != null) {
            weight = w
        }
    }
    fun getWeight(): String? {
        return weight
    }

    fun setRepetitions(r: String?) {
        if (r != null) {
            repetitions = r
        }
    }
    fun getRepetitions(): String? {
        return repetitions
    }
}