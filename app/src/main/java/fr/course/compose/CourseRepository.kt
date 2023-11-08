package fr.course.compose

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CourseRepository {
    init {
        saveListOfCourse( getListForTest( ))
    }

    fun getListeOfCourse(): Flow<List<Course>> = flow {
        emit(getListe())
    }

    fun deleteItemOfCourse(course: Course): Flow<List<Course>> = flow {
        val list: MutableList<Course> = getListe().toMutableList()
        list.remove(course)
        saveListOfCourse(list)
        emit(list)
    }

    fun findCourses(name: String): Flow<List<Course>> = flow {
        if(name.isEmpty())
            emit(getListe())
        else
            emit(getListe().filter { course -> course.name.lowercase().contains(name.lowercase()) })
    }


}