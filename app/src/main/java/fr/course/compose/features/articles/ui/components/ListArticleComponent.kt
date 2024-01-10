package fr.course.compose.features.articles.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.common.ui.components.DismissBackground
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.common.ui.theme.CoursesComposeTheme
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.ui.UiCourseDetailState
import fr.course.compose.features.courses.datasource.CourseLocaleDataSource
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    state: UiCourseDetailState,
    stateList: LazyListState,
    snackbarHostState: SnackbarHostState,
    onQuantiteChange: (item: Articles) -> Unit = {},
    onDeleteArticle: (item: Articles) -> Unit = {},
) {

    if(state.loading)
    {
        Loading("", modifier)
    } else {
        LazyColumn(
            state = stateList,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = if(state.data!!.second!!.size > 1) 120.dp else 80.dp),
            modifier = modifier
        ) {
            items(
                items = state.data.second!!,
                key = { article ->
                    article.id
                }
            ) { article ->
                ArticleItem(
                    article = article,
                    snackbarHostState = snackbarHostState,
                    onQuantiteChange = onQuantiteChange,
                    onRemove = onDeleteArticle
                )
            }
        }
    }
}

@Preview
@Composable
fun ArticleListPreview() {
     CoursesComposeTheme {
        ArticleList(
            state = UiCourseDetailState(data = Pair(CourseLocaleDataSource.getListForTest()[0], listOf())),
            stateList = LazyListState(),
            snackbarHostState = SnackbarHostState()
        )
     }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    article: Articles,
    snackbarHostState: SnackbarHostState,
    onQuantiteChange: (item: Articles) -> Unit = {},
    onRemove: (item: Articles) -> Unit = {},
) {
    var show by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                show = false
                true
            } else {
                show = true
                true
            }
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(
        show,exit = fadeOut(spring()), enter = fadeIn(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                ArticleItemCard(article, onQuantiteChange = onQuantiteChange)
            },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            scope.launch {
                val result = snackbarHostState
                    .showSnackbar(
                        message = "Article ${article.name} effacé",
                        actionLabel = "Annuler",
                        duration = SnackbarDuration.Short
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> { show = true; dismissState.reset() }
                    SnackbarResult.Dismissed -> { onRemove(article) }
                }
            }
        }
    }

}

@Preview
@Composable
fun ArticleItemPreview() {
    CoursesComposeTheme {
        ArticleItem(
            article = Articles(1, 1, "Choux"),
            snackbarHostState = SnackbarHostState()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItemCard(article: Articles, onQuantiteChange: (item: Articles) -> Unit) {
    //TODO: Voir pour mettre une propriete isDone et la stocker en base.
    // TODO: Voir comment on pourrait modifier l'article.
    var isFinished by rememberSaveable { mutableStateOf(false) }
    var isQuantityChange by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isQuantityChange) {
        if(isQuantityChange) {
            onQuantiteChange(article)
            isQuantityChange = false
        }
    }

    Card(onClick = { isFinished = !isFinished }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(1f)
        ) {
            Text(
                text = article.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                textAlign = TextAlign.Start,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textDecoration = if(isFinished) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 2,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            IconButton(
                modifier = Modifier.size(18.dp),
                enabled = article.quantite > 1,
                onClick = {
                    article.quantite--
                    isQuantityChange = true
                 },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = article.quantite.toString(),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(18.dp),
                onClick = {
                    article.quantite++
                    isQuantityChange = true
              },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
fun ArticleItemCardPreview() {
    ArticleItemCard(article = Articles(1, 1, "Intermarche"), onQuantiteChange = {})
}

