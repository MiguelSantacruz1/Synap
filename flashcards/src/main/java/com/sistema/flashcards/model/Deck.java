package com.sistema.flashcards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "decks")
@Getter @Setter
public class Deck {
    @Id
    private String id;

    private String name;

    private String description;
    private String colorHex;
    private Boolean isPublic = false;

    @DocumentReference(lazy = true)
    private User owner;
}
