package fr.course.compose.common.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import fr.course.compose.features.articles.database.ArticleDao
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.courses.database.CourseDao
import fr.course.compose.features.courses.database.Courses

@Database(entities = [Courses::class, Articles::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
    ]
)
abstract class CourseAppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun articleDao(): ArticleDao
}




