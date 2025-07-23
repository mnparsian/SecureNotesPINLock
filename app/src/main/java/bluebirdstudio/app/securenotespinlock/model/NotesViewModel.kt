package bluebirdstudio.app.securenotespinlock.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bluebirdstudio.app.securenotespinlock.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    // StateFlow برای نگهداری نوت‌ها
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        loadNotes()
    }

    // گرفتن تمام نوت‌ها از دیتابیس
    fun loadNotes() {
        viewModelScope.launch {
            val allNotes = repository.getAllNotes()
            _notes.value = allNotes
        }
    }

    // افزودن نوت
    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
            loadNotes()
        }
    }

    // بروزرسانی نوت
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
            loadNotes()
        }
    }

    // حذف نوت
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            loadNotes()
        }
    }
}
