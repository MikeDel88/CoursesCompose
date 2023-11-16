package fr.course.compose.features.courses.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.datasource.CourseLocaleDataSource
import fr.course.compose.features.courses.ui.UiCourseState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCourse(
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
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
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
                    IconButton(onClick = onRefreshList) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh datas"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = stringResource(R.string.bt_create_course)) },
            )
        },
        modifier = Modifier
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
                tonalElevation = 0.dp
            ) {
                CourseList(
                    state = uiCourseState,
                    onClickItem = onClickItem,
                    onRemove = onRemoveItem,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        FormCourse(Courses(name=""), modifier = Modifier.padding(16.dp)) { course ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(heightDp = 900)
@Composable
fun ScreenCourse() {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
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
                     IconButton(onClick = { /* do something */ }) {
                         Icon(
                             imageVector = Icons.Filled.Refresh,
                             contentDescription = "Refresh datas"
                         )
                     }
                 }
             )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = stringResource(id = R.string.bt_create_course)) },
            )
        },
        modifier = Modifier
    ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight())  {

                SearchBar(
                    query = "Intermarche",
                    onQueryChange = { },
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
                    tonalElevation = 0.dp
                ) {

                    CourseList(
                        UiCourseState(data = CourseLocaleDataSource.getListForTest()), {}, {},
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            sheetState = sheetState,
                        ) {
                            FormCourse(Courses(name = "Intermarche"), modifier = Modifier.padding(16.dp)) {
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
