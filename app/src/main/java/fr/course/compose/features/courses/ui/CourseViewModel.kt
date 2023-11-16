package fr.course.compose.features.courses.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.repository.CourseRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiCourseState(val data: List<Courses> = listOf(), var loading: Boolean = false)

@HiltViewModel
class CourseViewModel @Inject constructor(private val courseRepository: CourseRepositoryImpl): ViewModel() {

    private var listOfCourse: MutableList<Courses> = mutableListOf()
    private var listOfCourseFiltered: MutableList<Courses> = mutableListOf()
    private var search: String = ""

    private val _uiState = MutableStateFlow(UiCourseState(loading = true))
    val uiState: StateFlow<UiCourseState> = _uiState.asStateFlow()

    init {
        getListe()
    }

    private fun getListe() {
        viewModelScope.launch {
            delay(500) // Delay pour laisser le temps à l'écran de refresh.
            courseRepository.getListeOfCourse().collect { list ->
                listOfCourse = list.toMutableList()
                filterListOfCourse()
            }
        }
    }

    fun refreshList() {
        Log.d("CourseViewModel", "refreshList()")
        _uiState.value = UiCourseState(loading = true)
        getListe()
    }

    fun findCourse(name: String) {
        search = name
        filterListOfCourse()
    }

    private fun filterListOfCourse() {
        listOfCourseFiltered = if(search.isEmpty()) {
            mutableListOf()
        } else
            listOfCourse.filter { course -> course.name.contains(search) }.toMutableList()

        collectList()
    }

    fun removeCourse(courseModel: Courses) {
        viewModelScope.launch {
            val rowsAffected = courseRepository.suppCourse(courseModel)
            Log.d("CourseViewModel", "Deleted : $rowsAffected")
        }
    }

    fun addCourse(courses: Courses) {
        viewModelScope.launch {
            val id = courseRepository.addCourse(courses)
            Log.d("CourseViewModel", "Add : $id")
        }
    }

    private fun collectList() {
        Log.d("CourseViewModel", """
            Search : $search 
            List Filtered : $listOfCourseFiltered 
            List Not Filtered : $listOfCourse
        """.trimIndent())
        _uiState.value = UiCourseState(data = listOfCourseFiltered.ifEmpty { listOfCourse })
    }

}