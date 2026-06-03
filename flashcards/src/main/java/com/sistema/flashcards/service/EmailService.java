package com.sistema.flashcards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async // Ejecuta la tarea en un hilo separado del pool de hilos de Spring
    public void sendReminderEmail(String to, String username, String deckName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("¡Es hora de repasar tu mazo: " + deckName + "!");
        message.setText("Hola " + username + ",\nTienes tarjetas pendientes de repasar hoy en tu mazo '" + deckName + "'. Recuerda mantener tu racha de estudio diaria.");
        
        mailSender.send(message); // Operación de red SMTP no bloqueante
    }
}
