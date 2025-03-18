package it.unimib.mywave.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unimib.mywave.R
import it.unimib.mywave.data.MarkerData

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String, color: Color) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = color,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotSearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {

    SearchBar(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        query = query,
        onQueryChange = {
            onQueryChange(it)
            onSearch(it.trim())
        },
        onSearch = {
            onSearch(it.trim())
        },
        active = false,
        onActiveChange = {
            //active = it
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                textDecoration = TextDecoration.Underline
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable {
                        if (query.isNotEmpty()) {
                            onQueryChange("")
                            onSearch("")
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }

        }) {}
//    OutlinedTextField(
//        value = searchQuery,
//        onValueChange = {
//            searchQuery = it
//            onSearch(it.trim())
//        },
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//        maxLines = 1,
//        label = { Text(stringResource(R.string.search)) },
//        textStyle = TextStyle(fontWeight = FontWeight.Bold),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = Secondary
//        )
//    )
}

@Composable
fun MyTextField(
    labelValue: String, labelIco: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false
) {
    val textValue = rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = labelIco, contentDescription = "")
        },
        singleLine = true,
        maxLines = 1,
        isError = !errorStatus
    )
}

@Composable
fun MyTextFieldPass(
    labelValue: String, labelIco: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    imeAction: ImeAction
) {
    val localFocusManager = LocalFocusManager.current
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = labelIco, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible.value) {
                stringResource(id = R.string.auth_show_password)
            } else {
                stringResource(id = R.string.auth_show_password)
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}

@Composable
fun ClickableTextComponentProfile(
    labelValue: String,
    isBold: Boolean,
    onButtonClicked: () -> Unit
) {
    val textValue = buildAnnotatedString {
        pushStringAnnotation(
            tag = labelValue,
            annotation = labelValue
        )
        append(labelValue)
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        ),
        text = textValue,
        onClick = {
            onButtonClicked.invoke()
        }
    )
}

@Composable
fun UnderLinedTextComponent(value: String, onTextSelected: (String) -> Unit) {
    /*Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = MaterialTheme.colorScheme.secondary,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )*/

    val textValue = buildAnnotatedString {
        pushStringAnnotation(tag = value, annotation = value)
        append(value)
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline
        ),
        text = textValue,
        onClick = { offset ->
            textValue.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == value) {
                        onTextSelected(span.item)
                    }
                }
        })
}

@Composable
fun ClickableLoginTextComponent(tryLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText =
        if (tryLogin) stringResource(id = R.string.auth_go_to_login_check) else stringResource(id = R.string.auth_go_to_register_check)
    val textClick =
        if (tryLogin) stringResource(id = R.string.auth_login) else stringResource(id = R.string.auth_register)

    val textValue = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            pushStringAnnotation(tag = textClick, annotation = textClick)
            append(textClick)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = textValue, onClick = { offset ->
            textValue.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == textClick) {
                        onTextSelected(span.item)
                    }
                }
        })
}

@Composable
fun CheckBoxComponent(
    labelValue: String,
    checked: Boolean,
    onTextSelected: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(36.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Checkbox(checked = checked,
            onCheckedChange = {
                onCheckedChange.invoke(it)
            })
        val textValue = buildAnnotatedString {
            append(labelValue)
        }
        ClickableText(
            text = textValue,
            onClick = {
                onTextSelected.invoke(labelValue)
            },
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }

}

@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.primary
                        )
                    ),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DividerComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.auth_or),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray
        )
    }
}

@Composable
fun SpotDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onUpdateClick: (newName: String) -> Unit,
    onDeleteClick: () -> Unit,
    oldName: String
) {
    var newName by remember { mutableStateOf(oldName) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.edit_spot), fontWeight = FontWeight.Bold)
                    IconButton(
                        modifier = Modifier
                            .height(40.dp),
                        onClick = {
                            // Elimina il marker
                            onDismiss()
                            onDeleteClick()
                        }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "SpotDialog.kt",
                        )
                    }
                }

            },
            text = {
                // Contenuto del dialogo con campi modificabili
                Column {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text(stringResource(R.string.name)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Aggiorna il nome del marker quando l'utente conferma
                        onDismiss()
                        onUpdateClick(newName.trim())
                    }
                ) {
                    Text(
                        stringResource(R.string.confirm),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotCard(
    marker: MarkerData,
    onDeleteClick: () -> Unit, // Funzione chiamata per eliminare spot
    onUpdateClick: (newName: String) -> Unit, // Funzione chiamata per aggiornare nome spot
    onSpotCardClick: (marker: MarkerData) -> Unit // Funzione chiamata per aprire la schermata di dettaglio
) {
    var showDialog by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 6.dp),
        onClick = {
            // Spot Details Screen
            onSpotCardClick(marker)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .width(300.dp),
                        text = marker.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .width(300.dp),
                        text = "Lat: ${marker.latitude}\nLong: ${marker.longitude}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = stringResource(R.string.edit),
                    )
                }
            }
        }
        // Dialogo per la modifica del marker
        SpotDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onUpdateClick = onUpdateClick,
            onDeleteClick = onDeleteClick,
            oldName = marker.name
        )
    }
}
