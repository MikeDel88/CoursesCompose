package fr.course.compose.features.courses.repository

import fr.course.compose.features.articles.database.CourseWithDetail
import fr.course.compose.features.courses.database.Courses
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getListeOfCourse(): Flow<List<Courses>>
    suspend fun suppCourse(courses: Courses): Int
    suspend fun addCourse(courses: Courses): Long
    suspend fun getCourseById(id: Long): Flow<Courses>
    suspend fun updateCourse(courses: Courses): Int
    suspend fun getCourseByIdWithArticle(id: Long): Flow<CourseWithDetail>
}