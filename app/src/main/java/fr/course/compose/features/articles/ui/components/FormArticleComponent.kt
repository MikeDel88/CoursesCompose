package fr.course.compose.features.articles.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.R
import fr.course.compose.common.ui.theme.CoursesComposeTheme

@Composable
fun FormArticle(id: Long, modifier: Modifier = Modifier, onClickValidate: (articles: Articles) -> Unit = {}) {
    var text by rememberSaveable { mutableStateOf("") }
    var quantite by rememberSaveable { mutableIntStateOf(1) }
    var quantiteDialog by rememberSaveable { mutableIntStateOf(1) }
    var openAlertDialog by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        BasicTextField(
            value = text,
            modifier = Modifier.weight(1f),
            onValueChange = { newText ->
                text = newText
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    openAlertDialog = true
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .weight(1f)
                        .padding(all = 8.dp), // inner padding
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    innerTextField()
                    Row {
                        IconButton(
                            onClick = { openAlertDialog = true },
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Row {
                                Text(
                                    text = quantite.toString(),
                                    fontSize = 16.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Quantity",
                                    tint = Color.DarkGray
                                )
                            }
                        }
                        Button(
                            onClick = {
                                onClickValidate(
                                    Articles(
                                        courseId = id,
                                        name = text,
                                        quantite = quantite
                                    )
                                )
                                text = ""
                                quantite = 1
                            },
                            enabled = text.isNotEmpty()
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "")
                        }
                    }
                }
            }
        )


        if(openAlertDialog) {
            AlertDialog(
                icon = {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping Icon")
                },
                title = {
                    Text(text = "Quantité")
                },
                text = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        IconButton(
                            modifier = Modifier.size(32.dp),
                            enabled = quantiteDialog > 1,
                            onClick = { quantiteDialog-- },
                            colors =  IconButtonDefaults.filledIconButtonColors()
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = quantiteDialog.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        IconButton(
                            modifier = Modifier.size(32.dp),
                            onClick = { quantiteDialog++ },
                            colors =  IconButtonDefaults.filledIconButtonColors()
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                        }
                    }
                },
                onDismissRequest = { openAlertDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { openAlertDialog = false; quantite = quantiteDialog }
                    ) {
                        Text(stringResource(id = R.string.bt_ok).uppercase())
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { openAlertDialog = false; quantiteDialog = quantite}
                    ) {
                        Text(stringResource(R.string.bt_annuler).uppercase())
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun FormArticlePreview() {
    CoursesComposeTheme {
        FormArticle(id = 1)
    }
}
