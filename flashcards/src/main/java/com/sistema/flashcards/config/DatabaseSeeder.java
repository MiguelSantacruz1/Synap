package com.sistema.flashcards.config;

import com.sistema.flashcards.model.Deck;
import com.sistema.flashcards.model.Flashcard;
import com.sistema.flashcards.model.User;
import com.sistema.flashcards.model.UserProgress;
import com.sistema.flashcards.repository.DeckRepository;
import com.sistema.flashcards.repository.FlashcardRepository;
import com.sistema.flashcards.repository.UserProgressRepository;
import com.sistema.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final UserProgressRepository userProgressRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setId("1");
            user.setUsername("albeiro");
            user.setEmail("albeiro@test.com");
            user.setPassword("1234");
            userRepository.save(user);

            Deck deck = new Deck();
            deck.setId("1");
            deck.setName("Arquitectura y POO");
            deck.setDescription("Conceptos básicos");
            deck.setColorHex("#6366f1");
            deck.setIsPublic(true);
            deck.setOwner(user);
            deckRepository.save(deck);

            String[][] cards = {
                    {"¿Qué es el Principio de Responsabilidad Única (SRP)?", "Es un principio de diseño (SOLID) que establece que una clase debe tener una sola razón para cambiar, es decir, una única responsabilidad."},
                    {"¿Qué es la Cohesión en ingeniería de software?", "Mide qué tan relacionadas y enfocadas están las responsabilidades dentro de un mismo módulo o clase. Buscamos siempre lograr Alta Cohesión."},
                    {"¿Qué es el Acoplamiento?", "Es el grado de dependencia funcional entre diferentes módulos del software. El objetivo arquitectónico es mantener un Bajo Acoplamiento."},
                    {"¿Qué es el Polimorfismo en POO?", "Es la capacidad de objetos de distintas clases de responder al mismo mensaje o método de diferentes maneras, a través de una interfaz común."},
                    {"¿En qué consiste el patrón Singleton?", "Es un patrón de diseño creacional que garantiza que una clase tenga una única instancia en toda la aplicación y provee un punto global de acceso a ella."}
            };

            for (int i = 0; i < cards.length; i++) {
                Flashcard flashcard = new Flashcard();
                flashcard.setId(String.valueOf(i + 1));
                flashcard.setFront(cards[i][0]);
                flashcard.setBack(cards[i][1]);
                flashcard.setDeck(deck);
                flashcardRepository.save(flashcard);

                UserProgress progress = new UserProgress();
                progress.setUser(user);
                progress.setFlashcard(flashcard);
                progress.setBox(1);
                progress.setNextReview(LocalDateTime.now());
                userProgressRepository.save(progress);
            }

            System.out.println("Base de datos MongoDB inicializada con datos de prueba.");
        }
    }
}
