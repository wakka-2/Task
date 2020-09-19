package com.task.app.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.app.model.Course
import com.task.app.model.User


/*
Created by Aiman Qaid on 19,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/

@Dao
interface UserDao {

    //To create user in Database
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user:User)

    //To create course in Database
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCourse(course: Course)

    //To get Logged in user data
    @Query("SELECT * FROM `users table` WHERE `User Email`=(:userEmail) AND `User Password`=(:userPassword) ")
    fun login(userEmail:String,userPassword:String):LiveData<User>

    @Query("SELECT * FROM `courses table` WHERE `User ID`=(:userId)")
    fun getAllCourses(userId:Long):LiveData<List<Course>>
}