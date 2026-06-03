package com.sistema.flashcards.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private String difficulty; // "hard", "good", "easy"
}
