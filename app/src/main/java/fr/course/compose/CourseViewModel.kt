package fr.course.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiCourseState(val data: List<Course> = listOf(), var loading: Boolean = false)

class CourseViewModel(private val courseRepository: CourseRepository = CourseRepository()): ViewModel() {

    private val _uiState = MutableStateFlow(UiCourseState(loading = true))
    val uiState: StateFlow<UiCourseState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            courseRepository.getListeOfCourse().collect { list ->
                _uiState.value = UiCourseState(data = list)
            }
        }
    }

    fun findCourse(name: String) {
        viewModelScope.launch {
            courseRepository.findCourses(name).collect { list ->
                _uiState.value = UiCourseState(data = list)
            }
        }
    }

    fun removeCourse(course: Course) {
        viewModelScope.launch {
            courseRepository.deleteItemOfCourse(course).collect { list ->
                _uiState.value = UiCourseState(data = list)
            }
        }
    }

}