package com.sistema.flashcards.service;

import com.sistema.flashcards.model.Flashcard;
import com.sistema.flashcards.model.User;
import com.sistema.flashcards.model.UserProgress;
import com.sistema.flashcards.repository.FlashcardRepository;
import com.sistema.flashcards.repository.UserRepository;
import com.sistema.flashcards.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeitnerService {

    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;

    @Transactional
    public UserProgress reviewCard(String userId, String cardId, boolean isSuccess) {
        // Buscar o inicializar el progreso de este usuario para esta tarjeta
        UserProgress progress = progressRepository.findByUserIdAndFlashcardId(userId, cardId)
            .orElseGet(() -> {
                UserProgress newProgress = new UserProgress();
                User user = userRepository.findById(userId).orElseThrow();
                Flashcard card = flashcardRepository.findById(cardId).orElseThrow();
                newProgress.setUser(user);
                newProgress.setFlashcard(card);
                return newProgress;
            });

        if (isSuccess) {
            // Sube de caja (máximo caja 3) y calcula fecha de próximo repaso
            int nextBox = Math.min(progress.getBox() + 1, 3);
            progress.setBox(nextBox);
            
            // Intervalo en días: Caja 1 -> +1 día, Caja 2 -> +3 días, Caja 3 -> +7 días
            int daysToAdd = (nextBox == 2) ? 3 : (nextBox == 3) ? 7 : 1;
            progress.setNextReview(LocalDateTime.now().plusDays(daysToAdd));
        } else {
            // Fallo: regresa el progreso a caja 1 y se repasa inmediatamente
            progress.setBox(1);
            progress.setNextReview(LocalDateTime.now());
        }

        return progressRepository.save(progress);
    }
}
