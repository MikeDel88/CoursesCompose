package fr.course.compose

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Courses::class, Articles::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
    ]
)
abstract class CourseAppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun articleDao(): ArticleDao
}




