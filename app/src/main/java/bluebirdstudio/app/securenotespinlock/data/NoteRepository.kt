package bluebirdstudio.app.securenotespinlock.data

import bluebirdstudio.app.securenotespinlock.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }

    suspend fun addNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}
