package com.sonpxp.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sonpxp.todoapp.data.local.entity.TaskEntity
import com.sonpxp.todoapp.data.util.Converters

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
