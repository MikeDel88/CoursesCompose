package fr.course.compose

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Date


@Composable
fun ScreenCourse(
    uiCourseState: UiCourseState,
    findList: (text: String) -> Unit,
    onClickItem: (item: Course) -> Unit,
    onRemoveItem : (course: Course) -> Unit,
)
{
    var text by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(horizontal = 8.dp))  {
        if(uiCourseState.loading)
        {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )

        }
        else
        {
            Spacer(modifier = Modifier.size(16.dp))
            Search(
                text = text,
                onValueChange = { text = it; findList(it) }
            )
            CourseList(
                list = uiCourseState.data,
                onClickItem = onClickItem,
                onRemove = onRemoveItem,
            )
        }

    }
}

@Preview
@Composable
fun ScreenCourse() {
    val isLoading = false
    Column(modifier = Modifier.padding(horizontal = 8.dp))  {
        if(isLoading)
        {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        }
        else
        {
            Spacer(modifier = Modifier.size(16.dp))
            Search("Intermarche") {}
            CourseList(getListForTest(), {}, {})
        }

    }
}


@Composable
fun Search(text: String, onValueChange: (newValue: String) -> Unit) {
    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@Preview
@Composable
fun Search() {
    var text by rememberSaveable { mutableStateOf("") }

    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@Composable
fun CourseList(
    list: List<Course>,
    onClickItem: (item: Course) -> Unit,
    onRemove: (item: Course) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = list,
            key = { course ->
                course.id
            }
        ) { course ->
            CourseItem(
                course = course,
                onClickItem = onClickItem,
                onRemove = onRemove
            )
        }
    }
}

@Preview
@Composable
fun CourseList() {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(getListForTest()) { course ->
            CourseItem(course, {}, {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.EndToStart -> Color(0xFFFF1744)
        else -> {Color.Transparent}
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItem(
    course: Course,
    onClickItem: (item: Course) -> Unit,
    onRemove: (item: Course) -> Unit,
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
                CourseItemCard(course, onClickItem)
            },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            onRemove(course)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CourseItem() {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(Course(0, "intermarche", Date().formatCourse(), R.drawable.icon_intermarche))
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
fun CourseItemCard(course: Course, onClickItem: (item: Course) -> Unit) {
    Card(onClick = { onClickItem(course) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(1f)
        ) {
            Image(
                painter = painterResource(id = course.icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = course.name,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = course.date,
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
                .padding(8.dp, 4.dp)
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