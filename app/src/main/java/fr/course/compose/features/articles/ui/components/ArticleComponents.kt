package fr.course.compose.features.articles.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.datasource.ArticleLocalDataSource
import fr.course.compose.common.ui.components.DismissBackground
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.features.courses.ui.UiCourseDetailState
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ArticleList(
    state: UiCourseDetailState,
    onQuantiteChange: (item: Articles) -> Unit,
    onDeleteArticle: (item: Articles) -> Unit,
    modifier: Modifier
) {
    if(state.loading)
    {
        Loading("", modifier)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
        ) {
            items(
                items = state.data?.articles!!,
                key = { article ->
                    article.id
                }
            ) { article -> ArticleItem(
                article = article,
                onQuantiteChange = onQuantiteChange,
                onRemove = onDeleteArticle)
            }
        }
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
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(list) { article ->
                ArticleItem(article, {}, {})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(
    article: Articles,
    onQuantiteChange: (item: Articles) -> Unit,
    onRemove: (item: Articles) -> Unit,
) {
    var show by remember { mutableStateOf(true) }
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
                ArticleItemCard(article, onQuantiteChange = onQuantiteChange)
            },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            onRemove(article)
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
    var isFinished by rememberSaveable { mutableStateOf(false) }
    Card(onClick = { isFinished = !isFinished }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp, 8.dp)
                .fillMaxWidth(1f)
        ) {
            Text(
                text = article.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                textAlign = TextAlign.Start,
                textDecoration = if(isFinished) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            IconButton(
                modifier = Modifier.size(16.dp),
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
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
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
    var quantite by remember { mutableStateOf(50) }
    var isFinished by remember { mutableStateOf(true) }
    Card(onClick = { isFinished = !isFinished }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp, 8.dp)
                .fillMaxWidth(1f)
        ) {
            Text(
                text = "Patate",
                textAlign = TextAlign.Start,
                textDecoration = if(isFinished) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            IconButton(
                modifier = Modifier.size(16.dp),
                enabled = quantite > 1,
                onClick = { quantite-- },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = quantite.toString(),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { quantite++ },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

@Composable
fun FormArticle(id: Long, onClickValidate: (articles: Articles) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    var quantite by rememberSaveable { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                enabled = quantite > 1,
                onClick = { quantite-- },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = quantite.toString(),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { quantite++ },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
        Button(
            onClick = {
                onClickValidate(Articles(courseId = id, name = text, quantite = quantite))
                text = ""
                quantite = 1 },
            enabled = text.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()) {
            Text(text= stringResource(R.string.bt_save).uppercase(), maxLines = 1)
        }
    }
}

@Preview
@Composable
fun FormArticle() {
    var text by rememberSaveable { mutableStateOf("") }
    var quantite by rememberSaveable { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { if(quantite > 1) quantite-- },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = quantite.toString(),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { quantite++ },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = text.isNotEmpty()) {
            Text(text=stringResource(R.string.bt_save).uppercase(), maxLines = 1)
        }
    }
}