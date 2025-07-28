package bluebirdstudio.app.securenotespinlock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import androidx.navigation.compose.rememberNavController
import bluebirdstudio.app.securenotespinlock.data.NoteDatabase
import bluebirdstudio.app.securenotespinlock.data.NoteRepository
import bluebirdstudio.app.securenotespinlock.model.AuthViewModel
import bluebirdstudio.app.securenotespinlock.screens.LoginScreen
import bluebirdstudio.app.securenotespinlock.screens.RegistrationScreen
import bluebirdstudio.app.securenotespinlock.screens.ResetPinScreen

import bluebirdstudio.app.securenotespinlock.ui.theme.SecureNotesPINLockTheme
import bluebirdstudio.app.securenotespinlock.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // دیتابیس و ریپازیتوری
        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "secure_notes.db"
        ).fallbackToDestructiveMigration().build()

        val repository = NoteRepository(db.noteDao())

        // فکتوری ViewModel
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return NotesViewModel(repository) as T
            }
        }

        // گرفتن ViewModel
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setContent {
            SecureNotesPINLockTheme {
                val navController = rememberNavController()

                // دیتابیس و ViewModel ها
                val notesViewModel = ViewModelProvider(
                    this,
                    factory
                )[NotesViewModel::class.java]

                NavigationComponent(
                    navController = navController,
                    notesViewModel = notesViewModel
                )
            }
        }


    }
}
