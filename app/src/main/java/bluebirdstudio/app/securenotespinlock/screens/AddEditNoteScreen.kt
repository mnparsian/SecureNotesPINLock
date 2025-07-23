package bluebirdstudio.app.securenotespinlock.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.filled.*


import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import bluebirdstudio.app.securenotespinlock.model.Note
import bluebirdstudio.app.securenotespinlock.R
import bluebirdstudio.app.securenotespinlock.data.NoteRepository
import bluebirdstudio.app.securenotespinlock.model.NotesViewModel


@Composable
fun AddEditNoteScreen(
    viewModel: NotesViewModel,
    note: Note? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var content by remember {
        mutableStateOf(TextFieldValue(note?.content ?: ""))
    }

    var fontSize by remember { mutableStateOf(note?.fontSize ?: 16) }
    var textColor by remember {
        mutableStateOf(
            note?.textColor?.let { Color(note.textColor.toULong())
            } ?: Color.Black
        )
    }



    var alignment by remember { mutableStateOf(note?.textAlign ?: "Start") }
    var isBold by remember { mutableStateOf(note?.isBold ?: false) }

    // Dropdown menus state
    var showFontSizeMenu by remember { mutableStateOf(false) }
    var showColorMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
//            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {

        // ----- Toolbar -----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Font Size Picker
            Box {
                OutlinedButton(onClick = { showFontSizeMenu = true }) {
//                    Text("Size: $fontSize")
                    Text(stringResource(id = R.string.size) + ": $fontSize")
                }
                DropdownMenu(
                    expanded = showFontSizeMenu,
                    onDismissRequest = { showFontSizeMenu = false }
                ) {
                    listOf(12, 14, 16, 18, 20, 24, 28, 32).forEach {
                        DropdownMenuItem(
                            text = { Text(it.toString()) },
                            onClick = {
                                fontSize = it
                                showFontSizeMenu = false
                            }
                        )
                    }
                }
            }

            // Color Picker
            val colors = listOf(
                Color.Black to "Black",
                Color.Red to "Red",
                Color.Blue to "Blue",
                Color.Green to "Green",
                Color.Gray to "Gray"
            )

            Box {
                OutlinedButton(onClick = { showColorMenu = true }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(textColor, shape = CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(id = R.string.color))
                    }
                }
                DropdownMenu(
                    expanded = showColorMenu,
                    onDismissRequest = { showColorMenu = false }
                ) {
                    colors.forEach { (color, name) ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        Modifier
                                            .size(20.dp)
                                            .background(color, CircleShape)
                                            .border(1.dp, Color.DarkGray, CircleShape)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(name)
                                }
                            },
                            onClick = {
                                textColor = color
                                showColorMenu = false
                            }
                        )
                    }
                }
            }


            // Bold Toggle
            IconToggleButton(
                checked = isBold,
                onCheckedChange = { isBold = it }
            ) {
                Text("B", fontWeight = FontWeight.Bold)
            }

            // Alignment Buttons
//            IconButton(onClick = { alignment = TextAlign.Start }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
//                    contentDescription = "Align Start"
//                )
//            }
//            IconButton(onClick = { alignment = TextAlign.Center }) {
//                Icon(
//                    imageVector = Icons.Default.FormatAlignCenter,
//                    contentDescription = "Align Center"
//                )
//            }
//            IconButton(onClick = { alignment = TextAlign.End }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.FormatAlignRight,
//                    contentDescription = "Align End"
//                )
//            }
            AlignmentDropdown(
                selectedAlignment = alignment,
                onAlignmentSelected = { alignment = it }
            )







        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Editor Area -----
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .background(Color.White)
                .padding(12.dp)
        ) {
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = fontSize.sp,
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                    textAlign = when (alignment) {
                        "Center" -> TextAlign.Center
                        "End" -> TextAlign.End
                        "Right" -> TextAlign.End
                        "Left" -> TextAlign.Start
                        else -> TextAlign.Start
                    }
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Save / Cancel Buttons -----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val trimmedContent = content.text.trim()
                val generatedTitle = if (trimmedContent.isNotBlank()) {
                    trimmedContent.lineSequence().firstOrNull()?.take(30) ?: ""
                } else {
                    ""
                }

                val newNote = note?.copy(
                    title = generatedTitle,
                    content = trimmedContent,
                    textColor = textColor.value.toLong(),
                    fontSize = fontSize,
                    textAlign = alignment,
                    isBold = isBold
                ) ?: Note(
                    title = generatedTitle,
                    content = trimmedContent,
                    category = "",
                    textColor = textColor.value.toLong(),
                    backgroundColor = 0xFFFFFFFF,
                    fontSize = fontSize,
                    textAlign = alignment,
                    isBold = false
                )

                if (note == null) viewModel.addNote(newNote)
                else viewModel.updateNote(newNote)

                onSave()
            }) { Text(stringResource(id = R.string.save)) }

            Button(onClick = onCancel) { Text(stringResource(id = R.string.cancel)) }
        }
    }
}
@Composable
fun AlignmentDropdown(
    selectedAlignment: String,
    onAlignmentSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Helper function to get the right icon
    fun alignmentIcon(alignment: String): ImageVector {
        return when (alignment) {
            "Start" -> Icons.AutoMirrored.Filled.FormatAlignLeft
            "Center" -> Icons.Default.FormatAlignCenter
            "End" -> Icons.AutoMirrored.Filled.FormatAlignRight
            else -> Icons.Default.FormatAlignCenter
        }
    }


    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = alignmentIcon(selectedAlignment),
                contentDescription = "Alignment"
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                        contentDescription = "Align Left"
                    )
                },
                onClick = {
                    onAlignmentSelected("Left")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Icon(
                        imageVector = Icons.Default.FormatAlignCenter,
                        contentDescription = "Align Center"
                    )
                },
                onClick = {
                    onAlignmentSelected("Center")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatAlignRight,
                        contentDescription = "Align Right"
                    )
                },
                onClick = {
                    onAlignmentSelected("Right")
                    expanded = false
                }
            )
        }
    }
}
