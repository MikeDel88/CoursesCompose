package fr.course.compose.features.articles.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.database.ArticlesFilter
import fr.course.compose.features.articles.repository.ArticleRepositoryImpl
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.database.getDrawable
import fr.course.compose.features.courses.repository.CourseRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiCourseDetailState(val data: Pair<Courses?, List<Articles>?>?, var loading: Boolean = false)
@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val courseRepository: CourseRepositoryImpl,
    private val articleRepositoryImpl: ArticleRepositoryImpl,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(UiCourseDetailState(data = null, loading = true))
    val uiState: StateFlow<UiCourseDetailState> = _uiState.asStateFlow()

    init {
       getCourseAndList(savedStateHandle.get<Long>("id") ?: 0)
    }

    private fun getCourseAndList(id: Long) {
        viewModelScope.launch {
            delay(500)
            courseRepository.getCourseByIdWithArticle(id).collect {
                    data -> _uiState.update { state ->
                        state.copy(data = Pair(data.courses, data.articles), loading = false)
                    }
            }
        }
    }

    fun refreshList(id: Long) {
        _uiState.update { state ->
            state.copy(loading = true)
        }
        getCourseAndList(id)
    }

    fun majCourse(courses: Courses) {
        viewModelScope.launch {
            courses.icon = getDrawable(courses.name)
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

    fun filterListe(filter: ArticlesFilter) {
        val courses = _uiState.value.data?.first
        val liste = _uiState.value.data?.second!!
        val listeFiltered = when(filter) {
            ArticlesFilter.DONE -> liste
            ArticlesFilter.TODO -> liste
            ArticlesFilter.NAME_ASC -> liste.sortedBy { it.name }
            ArticlesFilter.NAME_DESC -> liste.sortedByDescending { it.name }
            ArticlesFilter.QUANTITY_ASC -> liste.sortedBy { it.quantite }
            ArticlesFilter.QUANTITY_DESC -> liste.sortedByDescending { it.quantite }
        }
        _uiState.update { state ->
            state.copy(data = Pair(courses, listeFiltered), loading = false)
        }
    }
}