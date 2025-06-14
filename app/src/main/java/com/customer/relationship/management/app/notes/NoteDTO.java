package com.customer.relationship.management.app.notes;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteDTO {
    private final Long id;
    private final String content;
    private final NoteType noteType;
    private final LocalDateTime noteDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public NoteDTO(Note note) {
        this.id = note.getId();
        this.content = note.getContent();
        this.noteType = note.getNoteType();
        this.noteDate = note.getNoteDate();
        this.createdAt = note.getCreatedAt();
        this.updatedAt = note.getUpdatedAt();
    }
} 