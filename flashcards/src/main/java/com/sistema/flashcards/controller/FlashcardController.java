package com.sistema.flashcards.controller;

import com.sistema.flashcards.dto.FlashcardDTO;
import com.sistema.flashcards.dto.ReviewRequestDTO;
import com.sistema.flashcards.model.Flashcard;
import com.sistema.flashcards.model.UserProgress;
import com.sistema.flashcards.repository.UserProgressRepository;
import com.sistema.flashcards.repository.FlashcardRepository;
import com.sistema.flashcards.service.LeitnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

    private final UserProgressRepository progressRepository;
    private final FlashcardRepository flashcardRepository;
    private final LeitnerService leitnerService;

    // Obtener tarjetas pendientes de repasar para el usuario 1 y mazo 1
    @GetMapping("/pending")
    public ResponseEntity<List<FlashcardDTO>> getPendingCards() {
        String userId = "1"; // Hardcodeado para la demo
        String deckId = "1";
        
        List<String> flashcardIds = flashcardRepository.findByDeckId(deckId).stream()
                .map(Flashcard::getId)
                .collect(Collectors.toList());

        List<UserProgress> pendingProgress = progressRepository
                .findByUserIdAndNextReviewLessThanEqualAndFlashcardIdIn(userId, LocalDateTime.now(), flashcardIds);
        
        List<FlashcardDTO> dtos = pendingProgress.stream().map(up -> {
            Flashcard f = up.getFlashcard();
            FlashcardDTO dto = new FlashcardDTO();
            dto.setId(f.getId());
            dto.setCategory(f.getDeck() != null ? f.getDeck().getName() : "Sin Categoria");
            dto.setQuestion(f.getFront());
            dto.setAnswer(f.getBack());
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{cardId}/review")
    public ResponseEntity<Void> reviewCard(@PathVariable String cardId, @RequestBody ReviewRequestDTO request) {
        String userId = "1"; // Hardcodeado para la demo
        // Si la dificultad es 'hard', isSuccess es false (baja a caja 1). Si es good/easy es true (sube de caja)
        boolean isSuccess = !request.getDifficulty().equals("hard");
        
        leitnerService.reviewCard(userId, cardId, isSuccess);
        
        return ResponseEntity.ok().build();
    }
}
