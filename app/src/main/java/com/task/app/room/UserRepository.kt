package com.task.app.room

import androidx.lifecycle.LiveData
import com.task.app.model.Course
import com.task.app.model.User


/*
Created by Aiman Qaid on 19,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/
class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User){
        userDao.registerUser(user)
    }
    suspend fun addCourse(course: Course){
        userDao.addCourse(course)
    }
    fun login(email:String,password:String):LiveData<User>{
        return userDao.login(email,password)
    }
    fun getAllCourses(userId:Long):LiveData<List<Course>>{
        return userDao.getAllCourses(userId)
    }
}