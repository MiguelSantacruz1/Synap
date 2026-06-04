package com.sistema.flashcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa una tarjeta de estudio (flashcard)")
public class FlashcardDTO {
    @Schema(description = "Identificador único de la tarjeta", example = "60c72b2f9b1d8b2a1c8b4567")
    private String id;

    @Schema(description = "Categoría o mazo al que pertenece la tarjeta", example = "Inglés")
    private String category;

    @Schema(description = "Pregunta o anverso de la tarjeta", example = "What is the capital of France?")
    private String question;

    @Schema(description = "Respuesta o reverso de la tarjeta", example = "Paris")
    private String answer;
}
