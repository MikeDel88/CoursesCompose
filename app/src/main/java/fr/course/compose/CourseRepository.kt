package fr.course.compose

import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getListeOfCourse(): Flow<List<Courses>>
    suspend fun suppCourse(courses: Courses): Int
    suspend fun addCourse(courses: Courses): Long
    suspend fun getCourseById(id: Long): Flow<Courses>
    suspend fun updateCourse(courses: Courses): Int
    suspend fun getCourseByIdWithArticle(id: Long): Flow<CourseWithDetail>
}