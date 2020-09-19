package com.task.app.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.task.app.model.Course
import com.task.app.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*
Created by Aiman Qaid on 19,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/
class UserViewModel(application:Application): AndroidViewModel(application) {

    private val repository:UserRepository


    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun registerUser(user:User){
        viewModelScope.launch(Dispatchers.IO){
            repository.registerUser(user)
        }
    }
    fun addCourse(course: Course){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCourse(course)
        }
    }
    fun login(email:String,password:String): LiveData<User>? {
        return repository.login(email,password)
    }
    fun getAllCourses(userId:Long):LiveData<List<Course>>{
        return repository.getAllCourses(userId)
    }
}