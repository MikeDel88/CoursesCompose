package fr.course.compose.features.courses.ui.components

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.common.ui.theme.CoursesComposeTheme
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.ui.UiCourseState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCourse(
    modifier: Modifier = Modifier,
    uiCourseState: UiCourseState,
    findList: (text: String) -> Unit,
    onClickItem: (item: Courses) -> Unit,
    onRemoveItem : (course: Courses) -> Unit,
    onAddItem : (course: Courses) -> Unit,
    onRefreshList: () -> Unit,
)
{
    var text by rememberSaveable { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                actions = {
                    val animateRotation = rememberInfiniteTransition(label = "")
                    val animation by animateRotation.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = InfiniteRepeatableSpec(
                            animation = tween(durationMillis = 1000, easing = EaseOut),
                            repeatMode = RepeatMode.Restart
                        ), label = ""
                    )
                    IconButton(onClick = {
                        onRefreshList()
                    }) {
                        Icon(
                            modifier = Modifier.rotate(0f),
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh datas"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = true,
                onClick = {
                    showBottomSheet = true
                },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = stringResource(R.string.bt_create_course)) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
        ) {
            SearchBar(
                query = text,
                onQueryChange = { text = it; findList(it) },
                onSearch = {},
                placeholder = {
                    Text(text = "")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                },
                trailingIcon = {},
                active = true,
                onActiveChange = {},
                tonalElevation = 0.dp,
            ) {
                CourseList(
                    state = uiCourseState,
                    onClickItem = onClickItem,
                    snackbarHostState = snackbarHostState,
                    onRemove = onRemoveItem,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
                if (showBottomSheet) {
                    ModalBottomSheet(
                        modifier = Modifier,
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        FormCourse(Courses(name=""), modifier = Modifier
                            .padding(16.dp)) { course ->
                            onAddItem(course)
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview()
@Composable
fun ScreenCourse() {
    CoursesComposeTheme {
        ScreenCourse(
            uiCourseState = UiCourseState(data = listOf(), loading = false),
            findList = {},
            onClickItem = {},
            onRemoveItem = {},
            onAddItem = {},
            onRefreshList = {}
        )
    }
}
