package bluebirdstudio.app.securenotespinlock.screens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.icons.Icons
//import androidx.compose.material3.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import bluebirdstudio.app.securenotespinlock.R
import bluebirdstudio.app.securenotespinlock.model.Note
import bluebirdstudio.app.securenotespinlock.model.NoteItem
import bluebirdstudio.app.securenotespinlock.model.NotesViewModel
import bluebirdstudio.app.securenotespinlock.model.NoteItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NotesViewModel = viewModel(),
    onAddNote: () -> Unit,
    onLogout: () -> Unit,
    onEditNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.your_notes)) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.no_notes_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
            ) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onClick = { onEditNote(note) },
                        onDelete = { onDeleteNote(note) }
                    )
                }
            }
        }

    }
}
