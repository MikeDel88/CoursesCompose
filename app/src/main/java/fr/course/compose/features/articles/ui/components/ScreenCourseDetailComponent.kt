package fr.course.compose.features.articles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.datasource.ArticleLocalDataSource
import fr.course.compose.features.articles.ui.UiCourseDetailState
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.ui.components.FormCourse

@Composable
fun ScreenCourseDetail(
    uiCourseDetailState: UiCourseDetailState,
    onUpdateItem: (course: Courses) -> Unit,
    onAddArticleItem: (article: Articles) -> Unit,
    onDeleteArticleItem: (article: Articles) -> Unit,
    onChangeQuantiteArticleItem: (article: Articles) -> Unit
) {
    if(uiCourseDetailState.loading ) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(uiCourseDetailState.data?.courses == null) {
        Text(text= stringResource(R.string.load_data_not_found))
    } else {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()) {
            FormCourse(
                courses = uiCourseDetailState.data.courses!!,
                onClickValidate = onUpdateItem
            )
            Divider()
            if(uiCourseDetailState.data.articles.isNullOrEmpty()) {
                Text(text = stringResource(R.string.article_not_found), modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                ArticleList(
                    state = uiCourseDetailState,
                    onQuantiteChange = onChangeQuantiteArticleItem,
                    onDeleteArticle = onDeleteArticleItem,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            FormArticle(
                id = uiCourseDetailState.data.courses!!.id,
                onClickValidate = onAddArticleItem)
        }
    }

}

@Preview(heightDp = 900)
@Composable
fun ScreenCourseDetail() {
    val loading by remember {
        mutableStateOf(false)
    }
    val course by remember {
        mutableStateOf(Courses(name="Intermarche"))
    }
    val data by remember {
        mutableStateOf(ArticleLocalDataSource.getListForTest())
    }
    if(loading) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(course == null) {
        Text(text= stringResource(R.string.load_data_not_found), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    } else {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()) {
            FormCourse(
                courses = course,
                onClickValidate = {}
            )
            Divider()
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(data) { article ->
                    ArticleItem(article, {}, {})
                }
            }
            FormArticle(
                id = course.id,
                onClickValidate = {})
        }
    }

}