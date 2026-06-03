// Flashcards array loaded from Spring Boot Backend
const API_BASE_URL = 'https://synap-b-production.up.railway.app'; // URL del backend en producción
let flashcardsData = [];

let currentIndex = 0;
let isFlipped = false;
let pendientesCount = flashcardsData.length;

// DOM Elements
const flashcard = document.getElementById('flashcard');
const flashcardScene = document.getElementById('flashcardScene');
const actionButtons = document.getElementById('actionButtons');
const cardQuestion = document.getElementById('cardQuestion');
const cardAnswer = document.getElementById('cardAnswer');
const currentCardNum = document.getElementById('currentCardNum');
const totalCardsNum = document.getElementById('totalCardsNum');
const progressFill = document.getElementById('progressFill');
const completionMessage = document.getElementById('completionMessage');

// Initialize App
async function initApp() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/flashcards/pending`);
        flashcardsData = await response.json();
        
        pendientesCount = flashcardsData.length;
        totalCardsNum.textContent = flashcardsData.length;
        updatePendientesBadge();
        
        loadCard(currentIndex);
        updateProgress();
    } catch (error) {
        console.error("Error cargando tarjetas:", error);
        cardQuestion.textContent = "Error al conectar con el servidor.";
    }
}

// Load a specific card
function loadCard(index) {
    if (index >= flashcardsData.length) {
        showCompletion();
        return;
    }

    const card = flashcardsData[index];
    
    // Animate transition
    flashcardScene.style.opacity = 0;
    
    setTimeout(() => {
        // Reset state
        isFlipped = false;
        flashcard.classList.remove('is-flipped');
        actionButtons.classList.remove('visible');
        
        // Update content
        cardQuestion.textContent = card.question;
        cardAnswer.innerHTML = `<p>${card.answer}</p>`;
        
        // Update UI
        currentCardNum.textContent = index + 1;
        document.querySelectorAll('.card-category').forEach(el => el.textContent = card.category);
        
        // Fade in
        flashcardScene.style.opacity = 1;
    }, 300);
}

// Handle Flip
flashcardScene.addEventListener('click', () => {
    if (!isFlipped) {
        isFlipped = true;
        flashcard.classList.add('is-flipped');
        
        // Show action buttons after flipping
        setTimeout(() => {
            actionButtons.classList.add('visible');
        }, 300); // Wait a bit for the flip animation to progress
    }
});

// Handle Review (Leitner Logic Simulation)
async function handleReview(difficulty) {
    const cardId = flashcardsData[currentIndex].id;
    
    // Call the Spring Boot Backend
    try {
        await fetch(`${API_BASE_URL}/api/flashcards/${cardId}/review`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ difficulty: difficulty })
        });
        console.log(`Tarjeta ${cardId} enviada al servidor como: ${difficulty}`);
    } catch (error) {
        console.error("Error enviando progreso:", error);
    }
    
    // Actualizar el contador de pendientes al repasar la tarjeta
    if (pendientesCount > 0) pendientesCount--;
    updatePendientesBadge();
    
    // Move to next card
    currentIndex++;
    updateProgress();
    loadCard(currentIndex);
}

function updatePendientesBadge() {
    const badge = document.querySelector('.stat-badge.pendientes');
    if (pendientesCount === 0) {
        badge.textContent = '✅ 0 Pendientes';
        badge.style.background = 'rgba(16, 185, 129, 0.15)';
        badge.style.color = '#6ee7b7';
        badge.style.borderColor = 'rgba(16, 185, 129, 0.3)';
    } else {
        badge.textContent = `📚 ${pendientesCount} Pendientes Hoy`;
        badge.style.background = 'rgba(239, 68, 68, 0.15)';
        badge.style.color = '#fca5a5';
        badge.style.borderColor = 'rgba(239, 68, 68, 0.3)';
    }
}

// Update Progress Bar
function updateProgress() {
    const percentage = (currentIndex / flashcardsData.length) * 100;
    progressFill.style.width = `${percentage}%`;
}

// Show Completion State
function showCompletion() {
    flashcardScene.style.display = 'none';
    actionButtons.style.display = 'none';
    document.querySelector('.progress-container').style.display = 'none';
    completionMessage.style.display = 'block';
}

// Reset Session (for demo)
function resetStudy() {
    currentIndex = 0;
    pendientesCount = flashcardsData.length;
    
    flashcardScene.style.display = 'block';
    actionButtons.style.display = ''; // Limpiamos el 'none' para que funcione la clase .visible de CSS
    document.querySelector('.progress-container').style.display = 'block';
    completionMessage.style.display = 'none';
    
    updatePendientesBadge();
    
    initApp();
}

// Start
initApp();
