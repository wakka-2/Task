package com.task.app.model

import androidx.room.*


/*
Created by Aiman Qaid on 18,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/

@Entity(tableName = "Courses Table", indices = [
        Index(value = ["Course ID"], unique = true),
    Index(value = ["User ID"], unique = false)],
    foreignKeys = [ForeignKey(entity = User::class,
    parentColumns = arrayOf("User ID"),
    childColumns = arrayOf("User ID"),
    onDelete = ForeignKey.CASCADE)]
)
data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Course ID")
    val id: Int,
    //Foreign Key
    @ColumnInfo(name = "User ID")
    val idNum:Long,
    @ColumnInfo(name = "Course Title")
    val title: String,
    @ColumnInfo(name = "Course City")
    val city: String,
    @ColumnInfo(name = "Course Date")
    val date: String,
    @ColumnInfo(name = "Course Duration")
    val duration: String,
    @ColumnInfo(name = "Course Rating")
    val rating: String
)
