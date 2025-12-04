document.addEventListener('DOMContentLoaded', () => {

    // ============================================================
    // 1. VERIFICAÇÃO DE SEGURANÇA E IDENTIDADE
    // ============================================================

    const usuarioLogadoJson = localStorage.getItem('usuario');

    // Se não tiver usuário salvo, redireciona para o login
    if (!usuarioLogadoJson) {
        alert("Sessão não iniciada. Por favor, faça login.");
        window.location.href = "login.html";
        return; // Para a execução do script
    }

    const usuarioLogado = JSON.parse(usuarioLogadoJson);

    // PEGA O ID DINAMICAMENTE (Fundamental para o Chat funcionar)
    const PACIENTE_ID = usuarioLogado.id;
    const API_BASE_URL = 'http://localhost:8080';

    // Validação opcional de tipo
    if (usuarioLogado.tipo !== 'PACIENTE') {
        console.warn("Aviso: O usuário logado não é do tipo PACIENTE.");
        // Opcional: window.location.href = "painel-profissional.html";
    }

    console.log(`Chat iniciado. Usuário: ${usuarioLogado.login} (ID: ${PACIENTE_ID})`);


    // ============================================================
    // 2. SELEÇÃO DOS ELEMENTOS DO DOM
    // ============================================================

    const chatMessages = document.getElementById('chat-messages');
    const chatForm = document.getElementById('chat-form');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');
    const loadingIndicator = document.getElementById('loading-indicator'); // Se existir no HTML


    // ============================================================
    // 3. FUNÇÕES DE INTERFACE (UI)
    // ============================================================

    /**
     * Exibe uma mensagem na tela de chat.
     * @param {string} text - O conteúdo da mensagem (pode conter HTML).
     * @param {string} sender - 'user' ou 'assistant' (para classes CSS).
     */
    function displayMessage(text, sender) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', sender);

        // IMPORTANTE: Usamos innerHTML para que <br> e <strong> do Java funcionem
        messageElement.innerHTML = text;

        chatMessages.appendChild(messageElement);

        // Rola automaticamente para a última mensagem
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    /**
     * Controla o estado de carregamento (botão desabilitado/spinner).
     * @param {boolean} show - True para mostrar carregando, False para normal.
     */
    function toggleLoading(show) {
        if (loadingIndicator) {
            loadingIndicator.style.display = show ? 'flex' : 'none';
        }

        if (show) {
            messageInput.disabled = true;
            sendButton.disabled = true;
            sendButton.innerText = "..."; // Feedback simples no botão
        } else {
            messageInput.disabled = false;
            sendButton.disabled = false;
            sendButton.innerText = "Enviar";
            messageInput.focus();
        }
    }


    // ============================================================
    // 4. LÓGICA DE COMUNICAÇÃO COM O BACKEND
    // ============================================================

    /**
     * Envia a mensagem para a API e processa a resposta.
     * @param {string} message - Texto digitado pelo usuário.
     * @param {boolean} isInitial - Se true, não exibe mensagem de erro na tela (silencioso).
     */
    async function sendMessageToBackend(message, isInitial = false) {
        toggleLoading(true);

        try {
            // URL dinâmica usando o ID do paciente logado
            const backendUrl = `${API_BASE_URL}/api/chat/${PACIENTE_ID}`;

            const response = await fetch(backendUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message: message }) // DTO esperado pelo Java
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Erro ${response.status}: ${errorText || response.statusText}`);
            }

            const data = await response.json();

            // Exibe a resposta do assistente (Strategy)
            displayMessage(data.response, 'assistant');

        } catch (error) {
            console.error('Falha ao enviar mensagem:', error);
            if (!isInitial) {
                displayMessage('Desculpe, ocorreu um erro de conexão com o servidor. Tente novamente.', 'assistant error');
            }
        } finally {
            toggleLoading(false);
        }
    }

    /**
     * Inicializa o chat enviando um comando oculto para o backend resetar o estado.
     */
    async function initializeChat() {
        // Envia mensagem especial. O backend reseta o estado e manda as boas-vindas.
        // Não exibimos "__INITIAL_MESSAGE__" na tela do usuário (por isso não chamamos displayMessage aqui).
        await sendMessageToBackend("__INITIAL_MESSAGE__", true);
    }


    // ============================================================
    // 5. EVENT LISTENERS E INICIALIZAÇÃO
    // ============================================================

    if (chatForm) {
        chatForm.addEventListener('submit', (event) => {
            event.preventDefault(); // Evita recarregar a página

            const userMessage = messageInput.value.trim();

            if (userMessage) {
                // 1. Mostra o que o usuário digitou
                displayMessage(userMessage, 'user');

                // 2. Limpa o campo
                messageInput.value = '';

                // 3. Envia para o servidor
                sendMessageToBackend(userMessage);
            }
        });
    }

    // Inicia a conversa ao carregar a página
    initializeChat();
});