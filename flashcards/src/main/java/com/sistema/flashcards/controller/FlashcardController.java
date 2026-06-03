package com.sistema.flashcards.controller;

import com.sistema.flashcards.dto.FlashcardDTO;
import com.sistema.flashcards.dto.ReviewRequestDTO;
import com.sistema.flashcards.model.Flashcard;
import com.sistema.flashcards.model.UserProgress;
import com.sistema.flashcards.repository.UserProgressRepository;
import com.sistema.flashcards.repository.FlashcardRepository;
import com.sistema.flashcards.service.LeitnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
@Tag(name = "Flashcard Controller", description = "Endpoints para la gestión y repaso de flashcards")
public class FlashcardController {

    private final UserProgressRepository progressRepository;
    private final FlashcardRepository flashcardRepository;
    private final LeitnerService leitnerService;

    // Obtener tarjetas pendientes de repasar para el usuario 1 y mazo 1
    @GetMapping("/pending")
    @Operation(summary = "Obtener tarjetas pendientes", description = "Obtiene las tarjetas de estudio que están pendientes de repasar para el usuario y mazo configurado por defecto (demo)")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlashcardDTO.class)))
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
    @Operation(summary = "Registrar repaso de tarjeta", description = "Registra el resultado del repaso de una tarjeta (dificultad: hard, good, easy) actualizando su estado según el algoritmo Leitner")
    @ApiResponse(responseCode = "200", description = "Repaso registrado correctamente")
    @ApiResponse(responseCode = "404", description = "Tarjeta o progreso no encontrado")
    public ResponseEntity<Void> reviewCard(
            @PathVariable @Parameter(description = "ID de la tarjeta a repasar", example = "60c72b2f9b1d8b2a1c8b4567") String cardId, 
            @RequestBody ReviewRequestDTO request) {
        String userId = "1"; // Hardcodeado para la demo
        // Si la dificultad es 'hard', isSuccess es false (baja a caja 1). Si es good/easy es true (sube de caja)
        boolean isSuccess = !request.getDifficulty().equals("hard");
        
        leitnerService.reviewCard(userId, cardId, isSuccess);
        
        return ResponseEntity.ok().build();
    }
}
