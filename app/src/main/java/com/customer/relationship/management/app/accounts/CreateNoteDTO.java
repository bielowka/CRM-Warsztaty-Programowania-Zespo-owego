package com.customer.relationship.management.app.accounts;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateNoteDTO {
    private String content;
    private NoteType noteType;
    private LocalDateTime noteDate;
    private Long accountId;
} 