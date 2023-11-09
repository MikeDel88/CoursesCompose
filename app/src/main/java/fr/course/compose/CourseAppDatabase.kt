package fr.course.compose

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Courses::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class CourseAppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}




