package fr.course.compose

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CourseRepositoryImpl @Inject constructor(private val courseLocaleDataSource: CourseLocaleDataSource): CourseRepository {
    override fun getListeOfCourse(): Flow<List<Courses>> = courseLocaleDataSource.getListe()
    override suspend fun suppCourse(courses: Courses): Int = courseLocaleDataSource.deleteCourse(courses)
    override suspend fun addCourse(courses: Courses): Long = courseLocaleDataSource.insertCourse(courses)
    override suspend fun updateCourse(courses: Courses): Int = courseLocaleDataSource.updateCourse(courses)
    override suspend fun getCourseById(id: Long): Flow<Courses> = courseLocaleDataSource.getCourseById(id)
    override suspend fun getCourseByIdWithArticle(id: Long): Flow<CourseWithDetail> = courseLocaleDataSource.getCourseByIdWithArticle(id)
}