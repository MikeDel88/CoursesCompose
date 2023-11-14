package fr.course.compose

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiCourseDetailState(val data: CourseWithDetail?, var loading: Boolean = false)
@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val courseRepository: CourseRepositoryImpl,
    private val articleRepositoryImpl: ArticleRepositoryImpl,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(UiCourseDetailState(data = null, loading = true))
    val uiState: StateFlow<UiCourseDetailState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            courseRepository.getCourseByIdWithArticle(savedStateHandle.get<Long>("id") ?: 0).collect {
                    course -> _uiState.value = UiCourseDetailState(data = course)
            }
        }

    }

    fun majCourse(courses: Courses) {
        viewModelScope.launch {
            val rowsAffected = courseRepository.updateCourse(courses)
            Log.d("CourseDetailViewModel", "Update : $rowsAffected")
        }
    }

    fun deleteCourse(courses: Courses) {
        viewModelScope.launch {
            val rowsAffected = courseRepository.suppCourse(courses)
            Log.d("CourseDetailViewModel", "Delete : $rowsAffected")
        }
    }

    fun addArticle(articles: Articles) {
        if(articles.quantite <= 0)
            return

        viewModelScope.launch {
            val rowsAffected = articleRepositoryImpl.addArticle(articles)
            Log.d("CourseDetailViewModel", "Add Article : $rowsAffected")
        }
    }

    fun deleteArticle(articles: Articles) {
        viewModelScope.launch {
            val rowsAffected = articleRepositoryImpl.suppArticle(articles)
            Log.d("CourseDetailViewModel", "Delete Article : $rowsAffected")
        }
    }

    fun updateArticle(articles: Articles) {
        if(articles.quantite <= 0)
            return

        viewModelScope.launch {
            val rowsAffected = articleRepositoryImpl.updateArticle(articles)
            Log.d("CourseDetailViewModel", "Update Article : $rowsAffected")
        }
    }
}