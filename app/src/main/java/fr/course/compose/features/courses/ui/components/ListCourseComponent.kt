package fr.course.compose.features.courses.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.common.ui.components.DismissBackground
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.database.firstLetterUppercase
import fr.course.compose.features.courses.database.formatCourse
import fr.course.compose.features.courses.datasource.CourseLocaleDataSource.Companion.getListForTest
import fr.course.compose.features.courses.ui.UiCourseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun CourseList(
    state: UiCourseState,
    snackbarHostState: SnackbarHostState,
    onClickItem: (item: Courses) -> Unit,
    onRemove: (item: Courses) -> Unit,
    modifier: Modifier
) {
    if(state.loading)
    {
        Loading("", modifier)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
        ) {
            items(
                items = state.data,
                key = { course ->
                    course.id
                }
            ) { course -> CourseItem(
                    courseModel = course,
                    onClickItem = onClickItem,
                    snackbarHostState = snackbarHostState,
                    onRemove = onRemove
                )
            }
        }
    }

}

@Preview
@Composable
fun CourseList() {
    val state by remember {
        mutableStateOf(UiCourseState(data = getListForTest()))
    }

    if(state.loading)
    {
        Loading("", Modifier)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(getListForTest()) { course ->
                CourseItem(course, SnackbarHostState(), {}, { true })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItem(
    courseModel: Courses,
    snackbarHostState: SnackbarHostState,
    onClickItem: (item: Courses) -> Unit,
    onRemove: (item: Courses) -> Unit,
) {
    var show by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
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
                CourseItemCard(courseModel, onClickItem)
            },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            scope.launch {
                val result = snackbarHostState
                    .showSnackbar(
                        message = "Course ${courseModel.name} effacé",
                        actionLabel = "Annuler",
                        duration = SnackbarDuration.Short
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> { show = true; dismissState.reset() }
                    SnackbarResult.Dismissed -> { onRemove(courseModel) }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CourseItem() {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(Courses(0, "intermarche", Date().formatCourse(), R.drawable.icon_intermarche))
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
                CourseItemCard(currentItem) {
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
fun CourseItemCard(courseModel: Courses, onClickItem: (item: Courses) -> Unit) {
    Card(onClick = { onClickItem(courseModel) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp, 8.dp)
                .fillMaxWidth(1f)
        ) {
            Image(
                painter = painterResource(id = courseModel.icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = courseModel.name.firstLetterUppercase(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = courseModel.date,
                fontStyle = FontStyle.Italic,
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }
    }

}

@Preview
@Composable
fun CourseItemCard() {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp, 8.dp)
                .fillMaxWidth(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_carrefour),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "carrefour portet sur garonneeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = Date().formatCourse(),
                fontStyle = FontStyle.Italic,
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }
    }
}

