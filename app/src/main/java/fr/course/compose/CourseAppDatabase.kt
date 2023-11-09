package fr.course.compose

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Courses::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class CourseAppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}




