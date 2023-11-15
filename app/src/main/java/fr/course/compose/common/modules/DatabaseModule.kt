package fr.course.compose.common.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.course.compose.common.database.CourseAppDatabase
import fr.course.compose.features.articles.database.ArticleDao
import fr.course.compose.features.courses.database.CourseDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideCourseDao(appDatabase: CourseAppDatabase): CourseDao {
        return appDatabase.courseDao()
    }

    @Provides
    fun provideArticleDao(appDatabase: CourseAppDatabase): ArticleDao {
        return appDatabase.articleDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CourseAppDatabase {
        return Room.databaseBuilder(
            appContext,
            CourseAppDatabase::class.java,
            "courses_database"
        ).build()
    }
}