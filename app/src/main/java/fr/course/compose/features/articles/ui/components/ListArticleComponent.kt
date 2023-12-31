package fr.course.compose.features.articles.ui.components

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.common.ui.components.DismissBackground
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.datasource.ArticleLocalDataSource
import fr.course.compose.features.articles.ui.UiCourseDetailState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ArticleList(
    state: UiCourseDetailState,
    snackbarHostState: SnackbarHostState,
    onScrollDetected: (state: LazyListState) -> Unit,
    onQuantiteChange: (item: Articles) -> Unit,
    onDeleteArticle: (item: Articles) -> Unit,
    modifier: Modifier
) {
    val scroll = rememberLazyListState()
    Log.d("Article", "List compose")
    if(state.loading)
    {
        Loading("", modifier)
    } else {
        LazyColumn(
            state = scroll,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = if(state.data!!.articles!!.size > 1) 120.dp else 80.dp),
            modifier = modifier
        ) {
            items(
                items = state.data?.articles!!,
                key = { article ->
                    article.id
                }
            ) { article -> ArticleItem(
                article = article,
                snackbarHostState = snackbarHostState,
                onQuantiteChange = onQuantiteChange,
                onRemove = onDeleteArticle)
            }
        }

        onScrollDetected(scroll)
    }
}

@Preview
@Composable
fun ArticleList() {
    val list by remember {
        mutableStateOf(ArticleLocalDataSource.getListForTest())
    }
    val loading by remember {
        mutableStateOf(false)
    }

    if(loading)
    {
        Loading("", Modifier)
    } else {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        ) {
            items(list) { article ->
                ArticleItem(article, SnackbarHostState(), {}, {})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(
    article: Articles,
    snackbarHostState: SnackbarHostState,
    onQuantiteChange: (item: Articles) -> Unit,
    onRemove: (item: Articles) -> Unit,
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
            modifier = Modifier,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ArticleItem() {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(Articles(courseId = 1, name = "Patates", quantite = 1))
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                show = false
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(
        show,exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                ArticleItemCard(currentItem) {
                    Toast.makeText(context, "Click on $currentItem", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            Toast.makeText(context, "Item ${currentItem.name} has been removed", Toast.LENGTH_SHORT).show()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItemCard(article: Articles, onQuantiteChange: (item: Articles) -> Unit) {
    //TODO: Voir pour mettre une propriete isDone et la stocker en base.
    // TODO: Voir comment on pourrait modifier l'article.
    var isFinished by rememberSaveable { mutableStateOf(false) }
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
                    onQuantiteChange(article)
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
                onClick = { article.quantite++; onQuantiteChange(article) },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ArticleItemCard() {
    var quantite by remember { mutableIntStateOf(50) }
    var isFinished by remember { mutableStateOf(true) }
    Card(onClick = { isFinished = !isFinished }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(1f)
        ) {
            Text(
                text = "Patate",
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
                enabled = quantite > 1,
                onClick = { quantite-- },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = quantite.toString(),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(18.dp),
                onClick = { quantite++ },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

