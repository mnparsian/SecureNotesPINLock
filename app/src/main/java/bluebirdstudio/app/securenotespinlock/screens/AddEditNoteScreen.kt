package bluebirdstudio.app.securenotespinlock.screens

import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import bluebirdstudio.app.securenotespinlock.model.Note
import bluebirdstudio.app.securenotespinlock.model.NotesViewModel
import bluebirdstudio.app.securenotespinlock.ui.components.RichTextEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteId: Int?,
    viewModel: NotesViewModel
) {
    val note by viewModel.currentNote.collectAsState()

    var title by remember { mutableStateOf("") }
    var formattedContent by remember { mutableStateOf("") }

    var showExitDialog by remember { mutableStateOf(false) }

    var initialTitle by remember { mutableStateOf("") }
    var initialContent by remember { mutableStateOf("") }

    LaunchedEffect(noteId) {
        if (noteId != null) {
            viewModel.loadNoteById(noteId)
        } else {
            viewModel.resetCurrentNote()
        }
    }

    LaunchedEffect(note) {
        note?.let {
            title = it.title
            formattedContent = it.content
            initialTitle = it.title
            initialContent = it.content
        }
    }

    val isChanged = title != initialTitle || formattedContent != initialContent

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
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote(
                            Note(
                                id = note?.id ?: 0,
                                title = title,
                                content = formattedContent
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
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") },
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toolbar بالایی برای ابزارهای اضافی
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* TODO: OCR */ }) {
                    Icon(Icons.Default.Check, contentDescription = "OCR")
                }
                IconButton(onClick = { /* TODO: Voice to Text */ }) {
                    Icon(Icons.Default.Check, contentDescription = "Voice")
                }
                IconButton(onClick = { /* TODO: AI Suggestions */ }) {
                    Icon(Icons.Default.Check, contentDescription = "AI")
                }
                IconButton(onClick = { /* TODO: Insert Image */ }) {
                    Icon(Icons.Default.Check, contentDescription = "Image")
                }
            }

            // Rich Text Editor (Toolbar نوشتاری داخل HTML)
            RichTextEditor(
                initialContent = formattedContent,
                onContentChanged = { newContent -> formattedContent = newContent },
                modifier = Modifier.fillMaxSize(),
                onWebViewCreated = { webView ->
                    ViewCompat.setOnApplyWindowInsetsListener(webView) { _: View, insets ->
                        val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                        webView.evaluateJavascript("setKeyboardState($isKeyboardVisible)", null)
                        insets
                    }
                }
            )
        }
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
}
