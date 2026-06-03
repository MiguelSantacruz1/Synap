package com.sistema.flashcards.repository;

import com.sistema.flashcards.model.Flashcard;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FlashcardRepository extends MongoRepository<Flashcard, String> {
    List<Flashcard> findByDeckId(String deckId);
}
