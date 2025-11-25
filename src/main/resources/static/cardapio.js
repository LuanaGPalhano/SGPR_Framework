document.addEventListener('DOMContentLoaded', () => {

    // --- 1. Seleção dos Elementos ---
    const chatMessages = document.getElementById('chat-messages');
    const chatForm = document.getElementById('chat-form');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');
    const loadingIndicator = document.getElementById('loading-indicator');

    // --- 2. Configurações ---
    // IMPORTANTE: Em um aplicativo real, o ID do paciente viria de um token (JWT) após o login.
    const PACIENTE_ID = 7;
    const API_BASE_URL = 'http://localhost:8080';

    // --- 3. Definição das Funções ---

    function displayMessage(text, sender) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', sender);
        messageElement.innerHTML = text;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight; // Rola para o final
    }

    function toggleLoading(show) {
        loadingIndicator.style.display = show ? 'flex' : 'none';
    }

    async function sendMessageToBackend(message) {
        messageInput.disabled = true;
        sendButton.disabled = true;
        toggleLoading(true);

        try {
            const backendUrl = `${API_BASE_URL}/api/chat/${PACIENTE_ID}`;

            const response = await fetch(backendUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: message })
            });

            if (!response.ok) {
                throw new Error(`Erro na comunicação com o servidor: ${response.statusText || response.status}`);
            }

            const data = await response.json();
            displayMessage(data.response, 'assistant');

        } catch (error) {
            console.error('Falha ao enviar mensagem:', error);
            displayMessage('Desculpe, ocorreu um erro. Tente novamente mais tarde.', 'assistant');
        } finally {
            toggleLoading(false);
            messageInput.disabled = false;
            sendButton.disabled = false;
            messageInput.focus();
        }
    }

    async function initializeChat() {
        // Envia uma "mensagem inicial" especial para o backend saber que é o começo da conversa.
        await sendMessageToBackend("__INITIAL_MESSAGE__");
    }

    // --- 4. Execução Inicial ---

    // Adiciona o listener para o envio do formulário (quando o usuário clica em Enviar ou aperta Enter)
    chatForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const userMessage = messageInput.value.trim();

        if (userMessage) {
            displayMessage(userMessage, 'user');
            sendMessageToBackend(userMessage);
            messageInput.value = '';
        }
    });

    initializeChat();
});