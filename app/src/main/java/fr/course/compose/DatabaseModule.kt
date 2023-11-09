package fr.course.compose

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideChannelDao(appDatabase: CourseAppDatabase): CourseDao {
        return appDatabase.courseDao()
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