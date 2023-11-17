package fr.course.compose.features.articles.ui.components

import android.os.Build
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.common.ui.components.SimpleDatePickerInDatePickerDialog
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.database.ArticlesFilter
import fr.course.compose.features.articles.datasource.ArticleLocalDataSource
import fr.course.compose.features.articles.ui.UiCourseDetailState
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.database.firstLetterUppercase
import fr.course.compose.features.courses.database.formatCourse
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun ScreenCourseDetail(
    uiCourseDetailState: UiCourseDetailState,
    onUpdateItem: (course: Courses) -> Unit,
    onAddArticleItem: (article: Articles) -> Unit,
    onDeleteArticleItem: (article: Articles) -> Unit,
    onChangeQuantiteArticleItem: (article: Articles) -> Unit,
    onFilterChange: (filter: ArticlesFilter) -> Unit
) {

    Log.d("Article", "Screen compose")

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(true) }
    var openAlertDialog by remember { mutableStateOf(false) }


    // Animation with Header when user scroll List Article.
    val transition = updateTransition(targetState = expanded, label = "transition")
    val animatedSizeDp: Dp by transition.animateDp(transitionSpec = {
        tween(500)
    }, "") { isExpanded ->
        if (isExpanded) 200.dp else 100.dp
    }

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
                Column(horizontalAlignment = Alignment.End) {
                    if(uiCourseDetailState.data.articles!!.size > 1) {
                        SmallFloatingActionButton(
                            onClick = { openAlertDialog = true }
                        ) {
                            Icon(painter = painterResource(R.drawable.icon_filter_list), contentDescription = "order")
                        }
                    }
                    ExtendedFloatingActionButton(
                        onClick = {
                            showBottomSheet = true
                        },
                        icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                        text = { Text(text = stringResource(R.string.bt_create_article)) },
                    )
                }
            },
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
            ) {
                Header(
                    courses = uiCourseDetailState.data.courses!!,
                    modifier = Modifier
                        .height(animatedSizeDp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.inversePrimary),
                    onUpdateItem = onUpdateItem
                )
                if(uiCourseDetailState.data.articles.isNullOrEmpty()) {
                    Text(text = stringResource(R.string.article_not_found), modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), textAlign = TextAlign.Center)
                } else {
                    ArticleList(
                        state = uiCourseDetailState,
                        onScrollDetected = { state ->
                            expanded = state.firstVisibleItemIndex <= 0
                        },
                        onQuantiteChange = onChangeQuantiteArticleItem,
                        snackbarHostState = snackbarHostState,
                        onDeleteArticle = onDeleteArticleItem,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    )
                }
                if (showBottomSheet) {
                    ModalBottomSheet(
                        modifier = Modifier.fillMaxWidth(),
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        FormArticle(
                            id = uiCourseDetailState.data.courses!!.id,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
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

            if(openAlertDialog) {
                AlertDialog(
                    icon = {
                        Icon(painterResource(id = R.drawable.icon_filter_list), contentDescription = "Filter Icon")
                    },
                    title = {
                        Text(text = "Filtre")
                    },
                    text = {
                        Column {
                            val filters = ArticlesFilter.values()
                            filters.forEach {
                                Log.d("Articles", "Filter ${it.name} (${it.ordinal}) : ${it.label}")
                                Text(text=it.label, modifier= Modifier
                                    .selectable(
                                    selected = false,
                                    onClick= { openAlertDialog = false; onFilterChange(it) }
                                    )
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                )
                            }
                        }
                    },
                    onDismissRequest = { openAlertDialog = false },
                    confirmButton = {},
                    dismissButton = {}
                )
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
    val expanded by remember { mutableStateOf(true) }
    var openAlertDialog by remember { mutableStateOf(false) }

    if(loading) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(course == null) {
        Text(text= stringResource(R.string.load_data_not_found), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    } else {

        Scaffold(
            floatingActionButton = {
                Column(horizontalAlignment = Alignment.End) {
                    if(data.size > 1) {
                        SmallFloatingActionButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(R.drawable.icon_filter_list), contentDescription = "order")
                        }
                    }
                    ExtendedFloatingActionButton(
                        onClick = {
                            showBottomSheet = true
                        },
                        icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                        text = { Text(text = stringResource(R.string.bt_create_article)) },
                    )
                }
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Header(course, Modifier
                    .animateContentSize()
                    .height(if (expanded) 150.dp else 80.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inversePrimary)) {}
                ArticleList()
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                    ) {
                        FormArticle(course.id, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
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
fun Header(courses: Courses, modifier : Modifier, onUpdateItem: (course: Courses) -> Unit) {

    var isNameEditing by rememberSaveable { mutableStateOf(false) }
    var isDateEditing by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf(courses.name) }
    var dateSelected by rememberSaveable { mutableStateOf(courses.date) }
    var openDatePicker by rememberSaveable { mutableStateOf(false) }

    Box(
        propagateMinConstraints = false,
        modifier = modifier,
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
