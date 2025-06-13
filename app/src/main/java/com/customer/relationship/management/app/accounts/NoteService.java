package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final AccountRepository accountRepository;

    public NoteService(NoteRepository noteRepository, AccountRepository accountRepository) {
        this.noteRepository = noteRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Note createNote(CreateNoteDTO createNoteDTO, User currentUser) {
        Account account = accountRepository.findById(createNoteDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only add notes to your own accounts");
        }

        Note note = new Note();
        note.setContent(createNoteDTO.getContent());
        note.setNoteType(createNoteDTO.getNoteType() != null ? createNoteDTO.getNoteType() : NoteType.OTHER);
        note.setNoteDate(createNoteDTO.getNoteDate() != null ? createNoteDTO.getNoteDate() : LocalDateTime.now());
        note.setAccount(account);

        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<Note> getNotesByAccount(Long accountId, User currentUser) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only view notes for your own accounts");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "noteDate");
        return noteRepository.findByAccountId(accountId, sort);
    }

    @Transactional
    public Note updateNote(Long noteId, UpdateNoteDTO updateNoteDTO, User currentUser) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getAccount().getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only edit notes from your own accounts");
        }

        if (updateNoteDTO.getContent() != null) {
            note.setContent(updateNoteDTO.getContent());
        }
        if (updateNoteDTO.getNoteType() != null) {
            note.setNoteType(updateNoteDTO.getNoteType());
        }
        if (updateNoteDTO.getNoteDate() != null) {
            note.setNoteDate(updateNoteDTO.getNoteDate());
        }

        return noteRepository.save(note);
    }

    @Transactional
    public void deleteNote(Long noteId, User currentUser) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getAccount().getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only delete notes from your own accounts");
        }

        noteRepository.delete(note);
    }
} 