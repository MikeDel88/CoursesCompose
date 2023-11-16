package fr.course.compose.features.articles.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.common.ui.components.SimpleDatePickerInDatePickerDialog
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.datasource.ArticleLocalDataSource
import fr.course.compose.features.articles.ui.UiCourseDetailState
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.database.firstLetterUppercase
import fr.course.compose.features.courses.database.formatCourse
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCourseDetail(
    uiCourseDetailState: UiCourseDetailState,
    onUpdateItem: (course: Courses) -> Unit,
    onAddArticleItem: (article: Articles) -> Unit,
    onDeleteArticleItem: (article: Articles) -> Unit,
    onChangeQuantiteArticleItem: (article: Articles) -> Unit,
    onRefreshList: (id: Long) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }


    if(uiCourseDetailState.loading ) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(uiCourseDetailState.data?.courses == null) {
        Text(text= stringResource(R.string.load_data_not_found))
    } else {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                    text = { Text(text = stringResource(R.string.bt_create_article)) },
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
            ) {
                Header(
                    courses = uiCourseDetailState.data.courses!!,
                    onUpdateItem = onUpdateItem
                )
                if(uiCourseDetailState.data.articles.isNullOrEmpty()) {
                    Text(text = stringResource(R.string.article_not_found), modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), textAlign = TextAlign.Center)
                } else {
                    ArticleList(
                        state = uiCourseDetailState,
                        onQuantiteChange = onChangeQuantiteArticleItem,
                        onDeleteArticle = { article ->
                            scope.launch {
                                val result = snackbarHostState
                                    .showSnackbar(
                                        message = "Article ${article.name} effacé",
                                        actionLabel = "Annuler",
                                        duration = SnackbarDuration.Long
                                    )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> { onRefreshList(uiCourseDetailState.data.courses!!.id) }
                                    SnackbarResult.Dismissed -> { onDeleteArticleItem(article) }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    )
                }
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        FormArticle(
                            id = uiCourseDetailState.data.courses!!.id,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) { article ->
                            onAddArticleItem(article)
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

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if(loading) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(course == null) {
        Text(text= stringResource(R.string.load_data_not_found), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    } else {

        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        //showBottomSheet = true
                    },
                    icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                    text = { Text(text = stringResource(R.string.bt_create_article)) },
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Header(course) {}
                ArticleList()
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        FormArticle(course.id, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            //onAddItem(course)
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
@Composable
fun Header(courses: Courses, onUpdateItem: (course: Courses) -> Unit) {

    var isNameEditing by rememberSaveable { mutableStateOf(false) }
    var isDateEditing by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf(courses.name) }
    var dateSelected by rememberSaveable { mutableStateOf(courses.date) }
    var openDatePicker by rememberSaveable { mutableStateOf(false) }

    Box(
        propagateMinConstraints = false,
        modifier = Modifier
            .heightIn(min = 100.dp)
            .fillMaxHeight(0.2f)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            if(isNameEditing) {
                Row(verticalAlignment = Alignment.Bottom) {
                    OutlinedTextField(value = text.firstLetterUppercase(), onValueChange = { text = it })
                    Spacer(Modifier.size(6.dp))
                    IconButton(onClick = {
                        isNameEditing = false
                        if(text != courses.name) {
                            courses.name = text
                            onUpdateItem(courses)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "")
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text= text.firstLetterUppercase(),
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.size(6.dp))
                    IconButton(onClick = { isNameEditing = true }, modifier = Modifier.size(15.dp)) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }

                }

            }
            Spacer(Modifier.size(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = dateSelected,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    textAlign = TextAlign.End,
                    fontStyle = FontStyle.Italic
                )
                if(!isDateEditing) {
                    Spacer(Modifier.size(6.dp))
                    IconButton(onClick = { openDatePicker = true; isDateEditing = true }, modifier = Modifier.size(15.dp)) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SimpleDatePickerInDatePickerDialog(openDatePicker, { openDatePicker = false; isDateEditing = false }) { date ->
                        dateSelected = if(date != null) Date(date).formatCourse() else ""
                        if(dateSelected != courses.date) {
                            courses.date = dateSelected
                            onUpdateItem(courses)
                        }
                    }
                }
            }
        }
    }
    Divider()
}
