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

    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    suspend fun upsertNote(note: Note) {
        if (note.id == 0) {
            noteDao.insertNote(note)
        } else {
            noteDao.updateNote(note)
        }
    }




}
