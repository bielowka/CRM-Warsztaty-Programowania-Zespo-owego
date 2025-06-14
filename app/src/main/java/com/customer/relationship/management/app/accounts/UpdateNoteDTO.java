package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.notes.NoteType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateNoteDTO {
    private String content;
    private NoteType noteType;
    private LocalDateTime noteDate;
} 