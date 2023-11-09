package fr.course.compose

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CourseRepositoryImpl @Inject constructor(private val courseLocaleDataSource: CourseLocaleDataSource): CourseRepository {
    override fun getListeOfCourse(): Flow<List<Courses>> = courseLocaleDataSource.getListe()
    override suspend fun suppCourse(courses: Courses): Flow<List<Courses>> = courseLocaleDataSource.deleteCourse(courses)
    override suspend fun addCourse(courses: Courses): Flow<List<Courses>> = courseLocaleDataSource.insertCourse(courses)
    override suspend fun findCoursesByName(name: String): Flow<List<Courses>> = courseLocaleDataSource.getListeByNames(name)
    override suspend fun updateCourse(courses: Courses): Flow<List<Courses>> = courseLocaleDataSource.updateCourse(courses)

}