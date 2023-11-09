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
        viewModelScope.launch {
            courseRepository.getListeOfCourse().collect { list ->
                _uiState.value = UiCourseState(data = list)
            }
        }
    }

    fun findCourse(name: String) {
        viewModelScope.launch {
            courseRepository.findCoursesByName(name).collect { list ->
                _uiState.value = UiCourseState(data = list)
            }
        }
    }

    fun removeCourse(courseModel: Courses) {
        viewModelScope.launch {
            courseRepository.suppCourse(courseModel).collect { list ->
                _uiState.value = UiCourseState(data = list)
            }
        }
    }

}