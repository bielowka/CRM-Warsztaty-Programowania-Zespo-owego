package com.customer.relationship.management.app.accounts;

public enum LeadStatus {
    NEW("Nowy"),
    QUALIFICATION("Kwalifikacja"),
    NEEDS_ANALYSIS("Analiza potrzeb"),
    VALUE_PROPOSITION("Propozycja wartości"),
    ID_DECISION_MAKERS("Identyfikacja decydentów"),
    PERCEPTION_ANALYSIS("Analiza percepcji"),
    PROPOSAL("Propozycja"),
    NEGOTIATION("Negocjacje"),
    CLOSED_WON("Zamknięty - wygrany"),
    CLOSED_LOST("Zamknięty - przegrany");

    private final String displayName;

    LeadStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}