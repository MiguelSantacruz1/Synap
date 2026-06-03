package com.sistema.flashcards.repository;

import com.sistema.flashcards.model.UserProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends MongoRepository<UserProgress, String> {
    
    Optional<UserProgress> findByUserIdAndFlashcardId(String userId, String flashcardId);

    List<UserProgress> findByUserIdAndNextReviewLessThanEqualAndFlashcardIdIn(
        String userId, 
        LocalDateTime now, 
        List<String> flashcardIds
    );
}
