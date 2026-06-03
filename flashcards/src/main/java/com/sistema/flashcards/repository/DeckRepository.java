package com.sistema.flashcards.repository;

import com.sistema.flashcards.model.Deck;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DeckRepository extends MongoRepository<Deck, String> {
    List<Deck> findByOwnerId(String ownerId);
}
