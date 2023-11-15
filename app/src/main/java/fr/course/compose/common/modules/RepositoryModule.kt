package fr.course.compose.common.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import fr.course.compose.features.articles.repository.ArticleRepository
import fr.course.compose.features.articles.repository.ArticleRepositoryImpl
import fr.course.compose.features.courses.repository.CourseRepository
import fr.course.compose.features.courses.repository.CourseRepositoryImpl

@Module
//Repositories will live same as the activity that requires them
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesCoursesRepository(impl: CourseRepositoryImpl): CourseRepository
    @Binds
    abstract fun providesArticlesRepository(impl: ArticleRepositoryImpl): ArticleRepository

}