package com.sistema.flashcards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Document(collection = "user_progress")
@CompoundIndex(name = "user_flashcard_idx", def = "{'user': 1, 'flashcard': 1}", unique = true)
@Getter @Setter
public class UserProgress {
    @Id
    private String id;

    @DocumentReference(lazy = true)
    private User user;

    @DocumentReference(lazy = true)
    private Flashcard flashcard;

    private Integer box = 1; // Cajas Leitner: 1, 2, o 3

    private LocalDateTime nextReview = LocalDateTime.now();
}
