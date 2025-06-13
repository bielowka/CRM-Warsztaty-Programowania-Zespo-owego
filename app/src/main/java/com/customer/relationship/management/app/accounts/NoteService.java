package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;
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

        if (!canAccessAccount(account, currentUser)) {
            throw new SecurityException("You can only add notes to accounts you have access to");
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

        if (!canAccessAccount(account, currentUser)) {
            throw new SecurityException("You can only view notes for accounts you have access to");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "noteDate");
        return noteRepository.findByAccountId(accountId, sort);
    }

    @Transactional
    public Note updateNote(Long noteId, UpdateNoteDTO updateNoteDTO, User currentUser) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!canAccessAccount(note.getAccount(), currentUser)) {
            throw new SecurityException("You can only edit notes from accounts you have access to");
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

        if (!canAccessAccount(note.getAccount(), currentUser)) {
            throw new SecurityException("You can only delete notes from accounts you have access to");
        }

        noteRepository.delete(note);
    }

    private boolean canAccessAccount(Account account, User currentUser) {
        if (currentUser.getRole() == UserRole.MANAGER) {
            return account.getUser().getTeam() != null && 
                   account.getUser().getTeam().equals(currentUser.getTeam());
        }
        return account.getUser().getId().equals(currentUser.getId());
    }
} 