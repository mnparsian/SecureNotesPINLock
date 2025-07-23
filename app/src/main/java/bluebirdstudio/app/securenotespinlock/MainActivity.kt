package bluebirdstudio.app.securenotespinlock

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import bluebirdstudio.app.securenotespinlock.data.NoteDatabase
import bluebirdstudio.app.securenotespinlock.data.NoteRepository
import bluebirdstudio.app.securenotespinlock.model.NotesViewModel
import bluebirdstudio.app.securenotespinlock.screens.*
import bluebirdstudio.app.securenotespinlock.ui.theme.LanguageSelector
import bluebirdstudio.app.securenotespinlock.ui.theme.SecureNotesPINLockTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ساخت دیتابیس و Repository
        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "secure_notes.db"
        ).fallbackToDestructiveMigration().build()

        val repository = NoteRepository(db.noteDao())

        // Factory برای ساخت ViewModel با Repository
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotesViewModel(repository) as T
            }
        }

        setContent {
            var languageChanged by remember { mutableStateOf(false) }
            SecureNotesPINLockTheme {
                key(languageChanged) {
                    LanguageSelector(onLanguageChanged = { languageChanged = !languageChanged })

                    val prefs = getSharedPreferences("SecureNotesPrefs", Context.MODE_PRIVATE)
                    var currentScreen by remember {
                        mutableStateOf(
                            if (prefs.getString("user_pin", null).isNullOrEmpty())
                                AppScreen.Registration else AppScreen.Login
                        )
                    }

                    // ViewModel رو اینجا با Factory بساز
                    val notesViewModel: NotesViewModel = viewModel(factory = factory)

                    when (currentScreen) {
                        AppScreen.Registration -> RegistrationScreen(
                            context = this,
                            onRegistered = { currentScreen = AppScreen.Login }
                        )

                        AppScreen.Login -> LoginScreen(
                            context = this,
                            onLoginSuccess = { currentScreen = AppScreen.Notes },
                            onForgotPin = { currentScreen = AppScreen.Reset }
                        )

                        AppScreen.Notes -> NoteListScreen(
                            viewModel = notesViewModel,
                            onEditNote = { note -> currentScreen = AppScreen.AddNote(note) },
                            onLogout = { currentScreen = AppScreen.Login },
                            onAddNote = { currentScreen = AppScreen.AddNote() },
                            onDeleteNote = { note -> notesViewModel.deleteNote(note) }
                        )

                        is AppScreen.AddNote -> {
                            val screen = currentScreen as AppScreen.AddNote
                            AddEditNoteScreen(
                                viewModel = notesViewModel,
                                note = screen.note,
                                onSave = { currentScreen = AppScreen.Notes },
                                onCancel = { currentScreen = AppScreen.Notes }
                            )
                        }

                        AppScreen.Reset -> ResetPinScreen(
                            context = this,
                            onResetSuccess = { currentScreen = AppScreen.Login }
                        )
                    }
                }
            }
        }
    }
}
