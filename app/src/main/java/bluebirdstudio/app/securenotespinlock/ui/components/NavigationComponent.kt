package bluebirdstudio.app.securenotespinlock

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bluebirdstudio.app.securenotespinlock.screens.*
import bluebirdstudio.app.securenotespinlock.AppScreen
import bluebirdstudio.app.securenotespinlock.model.AuthViewModel
import bluebirdstudio.app.securenotespinlock.model.NotesViewModel


import androidx.navigation.NavController



@Composable
fun NavigationComponent(
    navController: NavHostController,
    notesViewModel: NotesViewModel
) {
    val authViewModel: AuthViewModel = viewModel()

    // تعیین نقطه شروع بر اساس داشتن PIN
    val startDestination = if (authViewModel.pin.value.isNotEmpty()) {
        AppScreen.Login.route  // اگر PIN هست، صفحه لاگین (برای وارد کردن PIN)
    } else {
        AppScreen.Registration.route  // اگر PIN نیست، باید ثبت نام کنه
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // صفحه نوت‌ها
        composable(AppScreen.Notes.route) {
            NoteListScreen(
                viewModel = notesViewModel,
                onAddNote = { navController.navigate(AppScreen.AddEditNote().route) },
                onEditNote = { note -> navController.navigate(AppScreen.AddEditNote(note.id).route) },
                onDeleteNote = { note -> notesViewModel.deleteNote(note) },
                onLogout = {
                    navController.navigate(AppScreen.Login.route) {
                        popUpTo(AppScreen.Notes.route) { inclusive = true }
                    }
                }
            )
        }

        // صفحه Add/Edit
        composable("add_edit_note/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            AddEditNoteScreen(
                navController = navController,
                viewModel = notesViewModel,
                noteId = if (noteId == -1) null else noteId
            )
        }


        // صفحه Login
        composable(AppScreen.Login.route) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }

        // صفحه Registration
        composable(AppScreen.Registration.route) {
            RegistrationScreen(navController = navController, viewModel = authViewModel)
        }

        // صفحه Reset Pin
        composable(AppScreen.Reset.route) {
            ResetPinScreen(navController = navController, viewModel = authViewModel)
        }
    }
}
