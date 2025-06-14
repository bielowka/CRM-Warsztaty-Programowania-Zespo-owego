package com.customer.relationship.management.app.notes;

import com.customer.relationship.management.app.accounts.UpdateNoteDTO;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserRepository userRepository;

    public NoteController(NoteService noteService, UserRepository userRepository) {
        this.noteService = noteService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('SALESPERSON') or hasRole('MANAGER')")
    public ResponseEntity<NoteDTO> createNote(
            @RequestBody CreateNoteDTO createNoteDTO,
            Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note createdNote = noteService.createNote(createNoteDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NoteDTO(createdNote));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('SALESPERSON') or hasRole('MANAGER')")
    public ResponseEntity<List<NoteDTO>> getNotesByAccount(
            @PathVariable Long accountId,
            Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Note> notes = noteService.getNotesByAccount(accountId, currentUser);
        List<NoteDTO> noteDTOs = notes.stream()
                .map(NoteDTO::new)
                .toList();

        return ResponseEntity.ok(noteDTOs);
    }

    @PutMapping("/{noteId}")
    @PreAuthorize("hasRole('SALESPERSON') or hasRole('MANAGER')")
    public ResponseEntity<NoteDTO> updateNote(
            @PathVariable Long noteId,
            @RequestBody UpdateNoteDTO updateNoteDTO,
            Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note updatedNote = noteService.updateNote(noteId, updateNoteDTO, currentUser);
        return ResponseEntity.ok(new NoteDTO(updatedNote));
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasRole('SALESPERSON') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long noteId,
            Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        noteService.deleteNote(noteId, currentUser);
        return ResponseEntity.ok().build();
    }
}