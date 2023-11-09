package fr.course.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiCourseState(val data: List<Courses> = listOf(), var loading: Boolean = false)

@HiltViewModel
class CourseViewModel @Inject constructor(private val courseRepository: CourseRepositoryImpl): ViewModel() {

    private val _uiState = MutableStateFlow(UiCourseState(loading = true))
    val uiState: StateFlow<UiCourseState> = _uiState.asStateFlow()

    init {
        getListe()
    }

    private fun getListe() {
        viewModelScope.launch {
            courseRepository.getListeOfCourse().collect(::collectList)
        }
    }

    fun findCourse(name: String) {
        viewModelScope.launch {
            courseRepository.findCoursesByName(name).collect(::collectList)
        }
    }

    fun removeCourse(courseModel: Courses) {
        viewModelScope.launch {
            courseRepository.suppCourse(courseModel).collect(::collectList)
        }
    }

    fun majCourse(courses: Courses) {
        viewModelScope.launch {
            courseRepository.updateCourse(courses).collect(::collectList)
        }
    }

    fun addCourse(courses: Courses) {
        viewModelScope.launch {
            courseRepository.addCourse(courses).collect(::collectList)
        }
    }

    private fun collectList(list: List<Courses>) {
        _uiState.value = UiCourseState(data = list)
    }

}