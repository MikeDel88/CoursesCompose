package fr.course.compose

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
//Repositories will live same as the activity that requires them
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesCoursesRepository(impl: CourseRepositoryImpl): CourseRepository
    @Binds
    abstract fun providesArticlesRepository(impl: ArticleRepositoryImpl): ArticleRepository

}