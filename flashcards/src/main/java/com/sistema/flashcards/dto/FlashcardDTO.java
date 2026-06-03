package com.sistema.flashcards.dto;

import lombok.Data;

@Data
public class FlashcardDTO {
    private String id;
    private String category;
    private String question;
    private String answer;
}
