package bluebirdstudio.app.securenotespinlock.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bluebirdstudio.app.securenotespinlock.model.Note
import bluebirdstudio.app.securenotespinlock.viewmodel.NotesViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteId: Int?,
    viewModel: NotesViewModel
) {
    val note by viewModel.currentNote.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var isBold by remember { mutableStateOf(false) }
    var isItalic by remember { mutableStateOf(false) }


    var showExitDialog by remember { mutableStateOf(false) }

// مقادیر اولیه
    var initialTitle by remember { mutableStateOf("") }
    var initialContent by remember { mutableStateOf("") }

    // Load Note if editing
    LaunchedEffect(noteId) {
        if (noteId != null) {
            viewModel.loadNoteById(noteId)
        } else {
            viewModel.resetCurrentNote()
        }
    }


    // Populate fields when note is loaded
    LaunchedEffect(note) {
        note?.let {
            title = it.title
            content = it.content
            initialTitle = it.title
            initialContent = it.content
        }
    }

    val isChanged = title != initialTitle || content != initialContent

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == null) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isChanged) {
                            showExitDialog = true
                        } else {
                            viewModel.resetCurrentNote()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    if (showExitDialog) {
                        AlertDialog(
                            onDismissRequest = { showExitDialog = false },
                            title = { Text("Discard changes?") },
                            text = { Text("You have unsaved changes. Are you sure you want to go back?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.resetCurrentNote()
                                    navController.popBackStack()
                                }) {
                                    Text("Discard")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showExitDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }


                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote(
                            Note(
                                id = note?.id ?: 0,
                                title = title,
                                content = content
                            )
                        )
                        viewModel.resetCurrentNote()
                        navController.popBackStack()

                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") },
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Toolbar for text styles
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(onClick = { isBold = !isBold }) {
                    Text("B", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                IconButton(onClick = { isItalic = !isItalic }) {
                    Text("I", fontStyle = FontStyle.Italic, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content Field with selection + style
            SelectionContainer {
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                        color = Color.Black
                    )
                )
            }
        }
    }
}
