package com.task.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.task.app.model.Course
import com.task.app.model.User


/*
Created by Aiman Qaid on 19,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/

@Database(entities = [User::class,Course::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java,
                "user_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}