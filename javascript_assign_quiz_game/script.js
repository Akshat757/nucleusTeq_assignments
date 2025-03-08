const API_URL = 'https://opentdb.com/api.php?amount=15';
const categories = {
    'General Knowledge': 9,
    'Science & Nature': 17,
    'Computers': 18,
    'Mathematics': 19,
    'History': 23,
    'Sports': 21
};

// Game State
let playerName = '';
let selectedCategory = 'General Knowledge';
let selectedDifficulty = 'easy';
let currentQuestionIndex = 0;
let score = 0;
let questions = [];
let timer;
let currentCategory = '';
let currentDifficulty = '';

// Scoreboard structure: { "category-difficulty": [...] }
let scoreboard = JSON.parse(localStorage.getItem('scoreboard')) || {};

// Migrate old array-based scoreboard to object format
if (Array.isArray(scoreboard)) {
    const migratedScoreboard = {};
    scoreboard.forEach(entry => {
      const key = `${entry.category}-${entry.difficulty}`;
      if (!migratedScoreboard[key]) migratedScoreboard[key] = [];
      migratedScoreboard[key].push(entry);
    });
    scoreboard = migratedScoreboard;
    localStorage.setItem('scoreboard', JSON.stringify(scoreboard));
}

// DOM Elements
const nameScreen = document.querySelector('.name-screen');
const startScreen = document.querySelector('.start-screen');
const gameScreen = document.querySelector('.game-screen');
const endScreen = document.querySelector('.end-screen');
const nameInput = document.getElementById('player-name');
const submitNameBtn = document.querySelector('.submit-name');
const playerNameDisplay = document.querySelector('.player-name');
const categoryButtons = document.querySelector('.categories');
const difficultyButtons = document.querySelector('.difficulties');
const startBtn = document.querySelector('.start-btn');
const questionEl = document.querySelector('.question');
const optionsEl = document.querySelector('.options');
const timerProgress = document.querySelector('.timer-progress');
const scoreEl = document.querySelector('.score span');
const timeLeftDisplay = document.querySelector('.time-left');
const questionNumberEl = document.querySelector('.question-number');
const finalScoreValue = document.querySelector('.score-value');
const scoresContainer = document.querySelector('.scores');
const loadingBar = document.querySelector('.loading-bar');

// Initialize controls
function initializeControls() {
    Object.keys(categories).forEach(category => {
        const button = document.createElement('button');
        button.textContent = category;
        button.dataset.category = category;
        button.addEventListener('click', () => selectCategory(category));
        if (category === 'General Knowledge') button.classList.add('active');
        categoryButtons.appendChild(button);
    });

    ['easy', 'medium', 'hard'].forEach(difficulty => {
        const button = document.createElement('button');
        button.textContent = difficulty.charAt(0).toUpperCase() + difficulty.slice(1);
        button.dataset.difficulty = difficulty;
        button.addEventListener('click', () => selectDifficulty(difficulty));
        if (difficulty === 'easy') button.classList.add('active');
        difficultyButtons.appendChild(button);
    });
}

// Name input handling
submitNameBtn.addEventListener('click', () => {
    const name = nameInput.value.trim();
    if (name) {
        playerName = name;
        playerNameDisplay.textContent = name;
        nameScreen.classList.remove('active');
        startScreen.classList.add('active');
    }
});

function selectCategory(category) {
    selectedCategory = category;
    document.querySelectorAll('.categories button').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.category === category);
    });
}

function selectDifficulty(difficulty) {
    selectedDifficulty = difficulty;
    document.querySelectorAll('.difficulties button').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.difficulty === difficulty);
    });
}

async function startGame() {
    currentCategory = selectedCategory;
    currentDifficulty = selectedDifficulty;
    startScreen.classList.remove('active');
    gameScreen.classList.add('active');
    score = 0;
    currentQuestionIndex = 0;
    scoreEl.textContent = score;

    try {
        const response = await fetch(
            `${API_URL}&category=${categories[selectedCategory]}&difficulty=${selectedDifficulty}`
        );
        const data = await response.json();
        questions = data.results.map(q => ({
            ...q,
            options: shuffleArray([...q.incorrect_answers, q.correct_answer])
        }));
        showQuestion();
    } catch (error) {
        alert('Failed to fetch questions. Please try again.');
        resetGame();
    }
}

function showQuestion() {
    const question = questions[currentQuestionIndex];
    questionEl.textContent = decodeHTML(question.question);
    optionsEl.innerHTML = '';
    updateQuestionNumber();

    const letters = ['A', 'B', 'C', 'D'];
    question.options.forEach((option, index) => {
        const button = document.createElement('button');
        button.innerHTML = decodeHTML(option);
        button.dataset.option = letters[index];
        button.addEventListener('click', () => selectAnswer(button));
        optionsEl.appendChild(button);
    });

    startTimer();
}

function startTimer() {
    let timeLeft = 15;
    updateTimerDisplay(timeLeft);
    timerProgress.style.animation = 'none';
    timerProgress.offsetHeight;
    timerProgress.style.animation = 'countdown 15s linear forwards';

    timer = setInterval(() => {
        timeLeft--;
        updateTimerDisplay(timeLeft);
        if (timeLeft <= 0) {
            clearInterval(timer);
            handleTimeout();
        }
    }, 1000);
}

function updateTimerDisplay(time) {
    timeLeftDisplay.textContent = time;
}

function updateQuestionNumber() {
    questionNumberEl.textContent = `Question ${currentQuestionIndex + 1}/${questions.length}`;
}

function selectAnswer(selectedButton) {
    clearInterval(timer);
    const question = questions[currentQuestionIndex];
    const correct = selectedButton.textContent === question.correct_answer;

    if (correct) {
        score++;
        scoreEl.textContent = score;
    }

    showAnswerFeedback(selectedButton, question.correct_answer);
}

function handleTimeout() {
    const question = questions[currentQuestionIndex];
    const correctButton = [...document.querySelectorAll('.options button')].find(
        button => button.textContent === question.correct_answer
    );
    showAnswerFeedback(null, question.correct_answer);
}

function showAnswerFeedback(selectedButton, correctAnswer) {
    loadingBar.style.width = '100%';
    loadingBar.style.transition = 'width 2s linear';

    const options = document.querySelectorAll('.options button');
    options.forEach(button => {
        button.disabled = true;
        if (button.textContent === correctAnswer) {
            button.classList.add('correct');
        }
        if (button === selectedButton && button.textContent !== correctAnswer) {
            button.classList.add('incorrect');
        }
    });

    setTimeout(() => {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            resetQuestionState();
            showQuestion();
        } else {
            endGame(true);
        }
    }, 2000);
}

function resetQuestionState() {
    loadingBar.style.width = '0';
    loadingBar.style.transition = 'none';
    optionsEl.querySelectorAll('button').forEach(button => {
        button.classList.remove('correct', 'incorrect');
        button.disabled = false;
    });
}

function endGame(completed = true) {
    gameScreen.classList.remove('active');
    endScreen.classList.add('active');
    finalScoreValue.textContent = score;

    if (completed) {
        const key = `${currentCategory}-${currentDifficulty}`;
        
        // Initialize array if it doesn't exist
        if (!scoreboard[key]) {
            scoreboard[key] = [];
        }
        
        // Add new score
        scoreboard[key].push({
            name: playerName,
            score: score,
            category: currentCategory,
            difficulty: currentDifficulty,
            timestamp: new Date().getTime() // Add timestamp for tie-breaking
        });
        
        // Sort and keep top 5 scores
        scoreboard[key] = scoreboard[key]
            .sort((a, b) => {
                // First sort by score descending
                if (b.score !== a.score) return b.score - a.score;
                // Then sort by timestamp ascending (older entries first)
                return a.timestamp - b.timestamp;
            })
            .slice(0, 5);
        
        // Update localStorage
        localStorage.setItem('scoreboard', JSON.stringify(scoreboard));
    }

    displayScoreboard();
}

// Update scoreboard display
function displayScoreboard() {
    scoresContainer.innerHTML = ''; // Clear previous content

    const key = `${currentCategory}-${currentDifficulty}`; // Current quiz's key
    const entries = scoreboard[key] || []; // Get scores for this key

    // Create the scoreboard table
    const wrapper = document.createElement('div');
    wrapper.className = 'scoreboard-group';
    wrapper.innerHTML = `
        <h3>${currentCategory} (${currentDifficulty.toUpperCase()})</h3>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Score</th>
                    <th>Category</th>
                    <th>Difficulty</th>
                </tr>
            </thead>
            <tbody>
                ${entries.length > 0 ? 
                    entries.map((entry, index) => `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${entry.name}</td>
                            <td>${entry.score}</td>
                            <td>${entry.category}</td>
                            <td>${entry.difficulty.charAt(0).toUpperCase() + entry.difficulty.slice(1)}</td>
                        </tr>
                    `).join('') : 
                    `<tr><td colspan="5">No scores yet!</td></tr>`
                }
            </tbody>
        </table>
    `;

    scoresContainer.appendChild(wrapper);
}


function resetGame() {
    endScreen.classList.remove('active');
    startScreen.classList.add('active');
    timerProgress.style.animation = '';
    // Reload from localStorage
    scoreboard = JSON.parse(localStorage.getItem('scoreboard')) || {};
    // Reset game state
    questions = [];
    currentQuestionIndex = 0;
    score = 0;
}

// Exit button handler
function handleExitQuiz() {
    if (confirm('Are you sure you want to exit? Your progress will be lost!')) {
        location.reload();
    }
}

// Utility functions
function decodeHTML(html) {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

// Event listeners
startBtn.addEventListener('click', startGame);
document.querySelector('.play-again-btn').addEventListener('click', resetGame);
document.querySelector('.exit-btn').addEventListener('click', handleExitQuiz);
// Initialize the game
initializeControls();