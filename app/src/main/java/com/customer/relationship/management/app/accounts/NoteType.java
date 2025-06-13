package com.customer.relationship.management.app.accounts;

import lombok.Getter;

@Getter
public enum NoteType {
    MEETING("Notatka ze spotkania"),
    PHONE_CALL("Rozmowa telefoniczna"),
    EMAIL("Korespondencja email"),
    FOLLOW_UP("Dalsze działania"),
    GENERAL("Notatka ogólna"),
    OTHER("Inna");

    private final String displayName;

    NoteType(String displayName) {
        this.displayName = displayName;
    }

}