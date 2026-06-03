package com.sistema.flashcards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "flashcards")
@Getter @Setter
public class Flashcard {
    @Id
    private String id;

    private String front;

    private String back;

    @DocumentReference(lazy = true)
    private Deck deck;
}
