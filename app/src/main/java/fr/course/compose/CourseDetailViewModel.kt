package fr.course.compose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiCourseDetailState(val data: Courses?, var loading: Boolean = false)
@HiltViewModel
class CourseDetailViewModel @Inject constructor(courseRepository: CourseRepositoryImpl, savedStateHandle: SavedStateHandle): ViewModel() {

    private val _uiState = MutableStateFlow(UiCourseDetailState(data = null, loading = true))
    val uiState: StateFlow<UiCourseDetailState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            courseRepository.getCourseDetails(savedStateHandle.get<Int>("id") ?: 0).collect {
                    course -> _uiState.value = UiCourseDetailState(data = course)
            }
        }

    }
}