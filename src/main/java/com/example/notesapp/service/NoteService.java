package com.example.notesapp.service;

import com.example.notesapp.model.Note;
import com.example.notesapp.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> findAll(String query) {
        String trimmedQuery = query == null ? "" : query.trim();
        if (trimmedQuery.isEmpty()) {
            return noteRepository.findAllByOrderByUpdatedAtDesc();
        }
        return noteRepository.findBySubjectContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByUpdatedAtDesc(
                trimmedQuery, trimmedQuery);
    }

    @Transactional(readOnly = true)
    public Note getById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));
    }

    public Note create(Note note) {
        Note newNote = new Note();
        newNote.setSubject(note.getSubject());
        newNote.setContent(note.getContent());
        return noteRepository.save(newNote);
    }

    public Note update(Long id, Note updatedNote) {
        Note existing = getById(id);
        existing.setSubject(updatedNote.getSubject());
        existing.setContent(updatedNote.getContent());
        return noteRepository.save(existing);
    }

    public void delete(Long id) {
        noteRepository.deleteById(id);
    }
}
