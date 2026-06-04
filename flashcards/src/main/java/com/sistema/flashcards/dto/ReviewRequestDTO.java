package com.sistema.flashcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para enviar el resultado de dificultad tras repasar una tarjeta")
public class ReviewRequestDTO {
    @Schema(description = "Nivel de dificultad reportado por el usuario", allowableValues = {"hard", "good", "easy"}, example = "good")
    private String difficulty; // "hard", "good", "easy"
}
