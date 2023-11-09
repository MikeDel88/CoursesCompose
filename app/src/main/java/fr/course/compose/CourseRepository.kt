package fr.course.compose

import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getListeOfCourse(): Flow<List<Courses>>
    suspend fun suppCourse(courses: Courses): Flow<List<Courses>>
    suspend fun addCourse(courses: Courses): Flow<List<Courses>>
    suspend fun findCoursesByName(name: String): Flow<List<Courses>>
    suspend fun updateCourse(courses: Courses): Flow<List<Courses>>
}