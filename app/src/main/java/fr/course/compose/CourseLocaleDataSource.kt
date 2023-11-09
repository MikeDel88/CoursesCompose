package fr.course.compose

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CourseLocaleDataSource @Inject constructor(private val courseDao: CourseDao) {
    companion object {
        fun getListForTest(): List<Courses> {
            return mutableListOf<Courses>().apply {
                add(Courses(0, "Intermarche", Date().formatCourse(),R.drawable.icon_intermarche))
                add(Courses(1, "Leclerc", Date().formatCourse(),R.drawable.icon_leclerc))
                add(Courses(2, "Super U", Date().formatCourse(),R.drawable.icon_superu))
                add(Courses(3, "Carrefour", Date().formatCourse(),R.drawable.icon_carrefour))
                add(Courses(4, "Casino", Date().formatCourse(),R.drawable.icon_inconnu))
            }
        }

    }
    fun getListe(): Flow<List<Courses>> =courseDao.getAll()

    suspend fun deleteCourse(courses: Courses) = withContext(Dispatchers.Default) {
        courseDao.delete(courses)
        getListe()
    }

    suspend fun insertCourse(courses: Courses) = withContext(Dispatchers.Default) {
        courseDao.insert(courses)
        getListe()
    }

    suspend fun getListeByNames(name: String) = withContext(Dispatchers.Default) {
        if(name.isEmpty())
            getListe()
        else
            courseDao.getCourseByName("%$name%")
    }

    suspend fun updateCourse(courses: Courses) = withContext(Dispatchers.Default) {
        courseDao.update(courses)
        getListe()
    }
}