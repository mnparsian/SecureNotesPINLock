package bluebirdstudio.app.securenotespinlock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bluebirdstudio.app.securenotespinlock.data.NoteRepository
import bluebirdstudio.app.securenotespinlock.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote

    fun loadNoteById(id: Int) {
        viewModelScope.launch {
            val note = repository.getNoteById(id)
            _currentNote.value = note
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.upsertNote(note)
            _notes.value = repository.getAllNotes()  // آپدیت لیست بعد از ذخیره
        }
    }


    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _notes.value = repository.getAllNotes()  // بلافاصله لیست آپدیت می‌شه
        }
    }


    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes
    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repository.getAllNotes() // متد getAllNotes رو توی repository داری
        }
    }

    fun resetCurrentNote() {
        _currentNote.value = null
    }


}
