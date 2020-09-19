package com.task.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/*
Created by Aiman Qaid on 19,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/
@Entity(tableName = "Users Table", indices = [
    Index(value = ["User ID"], unique = true)
])
data class User(
    @PrimaryKey
    @ColumnInfo(name = "User ID")
    val idNum:Long,
    @ColumnInfo(name = "User Name")
    val name:String,
    @ColumnInfo(name = "User Phone")
    val phoneNum:String,
    @ColumnInfo(name = "User Email")
    val email:String,
    @ColumnInfo(name = "User Password")
    val password:String)