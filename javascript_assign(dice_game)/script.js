const players = document.querySelectorAll('.player');
const rollBtn = document.querySelector('.btn-roll');
const saveBtn = document.querySelector('.btn-save');
const resetBtn = document.querySelector('.btn-reset');
const diceImg = document.querySelector('.dice img');
const winnerOverlay = document.querySelector('.winner-overlay');
const playAgainBtn = document.querySelector('.btn-play-again');

let scores = [0, 0];
let currentScore = 0;
let activePlayer = 0;
let gameActive = true;

// Dice images - replace with your own paths
const diceImages = [
    'images/dice-1-b.svg',
    'images/dice-1-b.svg',
    'images/dice-1-b.svg',
    'images/dice-4-b.svg',
    'images/dice-5-b.svg',
    'images/dice-6-b.svg'
];

// Disable player name input fields
function disablePlayerNameInputs() {
    document.querySelectorAll('.player-name-input').forEach(input => {
        input.disabled = true; // Disable the input field
    });
}

// Enable player name input fields
function enablePlayerNameInputs() {
    document.querySelectorAll('.player-name-input').forEach(input => {
        input.disabled = false; // Enable the input field
    });
}

// Game Initialization
function init() {
    scores = [0, 0];
    currentScore = 0;
    activePlayer = 0;
    gameActive = true;
    
    document.querySelectorAll('.saved-score').forEach(score => score.textContent = '0');
    document.querySelectorAll('.current-score').forEach(score => score.textContent = '0');
    players.forEach(player => player.classList.remove('active'));
    players[0].classList.add('active');
    winnerOverlay.style.display = 'none';
    enablePlayerNameInputs(); // Enable inputs for editing
}

// Switch Player
function switchPlayer() {
    currentScore = 0;
    document.querySelector(`.player[data-player="${activePlayer}"] .current-score`).textContent = '0';
    activePlayer = activePlayer === 0 ? 1 : 0;
    players.forEach(player => player.classList.toggle('active'));
}

// Roll Dice
rollBtn.addEventListener('click', () => {
    if (!gameActive) return;
    
    const dice = Math.floor(Math.random() * 6) + 1;
    diceImg.src = diceImages[dice - 1];
    diceImg.parentElement.style.animation = 'diceShake 0.5s ease';

    setTimeout(() => {
        diceImg.parentElement.style.animation = '';
    }, 500);

    if (dice !== 1) {
        currentScore += dice;
        document.querySelector(`.player[data-player="${activePlayer}"] .current-score`).textContent = currentScore;
    } else {
        switchPlayer();
    }

    disablePlayerNameInputs(); // Disable inputs after the first roll
});

// Save Score
saveBtn.addEventListener('click', () => {
    if (!gameActive || currentScore === 0) return;
    
    scores[activePlayer] += currentScore;
    document.querySelector(`.player[data-player="${activePlayer}"] .saved-score`).textContent = scores[activePlayer];
    
    if (scores[activePlayer] >= 100) {
        gameActive = false;
        winnerOverlay.style.display = 'flex';
        document.querySelector('.winner-text').textContent = 
            `${document.querySelector(`.player[data-player="${activePlayer}"] .player-name-input`).value} Wins!`;
    } else {
        switchPlayer();
    }
});

// Reset Game
resetBtn.addEventListener('click', init);

// Play Again
playAgainBtn.addEventListener('click', () => {
    init();
    winnerOverlay.style.display = 'none';
});

// Name Editing
document.querySelectorAll('.player-name-input').forEach(input => {
    input.addEventListener('input', function() {
        this.parentElement.querySelector('.player-name').textContent = this.value;
    });
});

// Initialize game
init();