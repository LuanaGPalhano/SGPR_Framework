document.addEventListener('DOMContentLoaded', () => {

    const loginForm = document.getElementById('formLogin');

    // ADICIONADO 'async' AQUI PARA CORRIGIR O ERRO
    loginForm.addEventListener('submit', async function(event) {

        event.preventDefault(); // Impede que a página recarregue sozinha

        const loginInput = document.getElementById('login').value;
        const senhaInput = document.getElementById('senha').value;
        const botao = document.querySelector('button[type="submit"]'); // Seleciona o botão de enviar

        // Feedback visual para o usuário
        const textoOriginal = botao.innerText;
        botao.innerText = 'Entrando...';
        botao.disabled = true;

        const dadosLogin = {
            login: loginInput,
            senha: senhaInput
        };

        try {
            // Faz a requisição ao Backend
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosLogin)
            });

            if (response.ok) {
                const data = await response.json();
                console.log("LOGIN SUCESSO! Resposta do Backend:", data);

                // Salva os dados no navegador para usar nas próximas páginas
                localStorage.setItem('usuario', JSON.stringify(data));

                // === LÓGICA DE REDIRECIONAMENTO (Framework) ===
                // Verifica o tipo retornado pelo backend e manda para a página certa

                if (data.tipo === 'PACIENTE') {
                    window.location.href = 'TelaPrincipalPac.html';
                }
                else if (data.tipo === 'NUTRICIONISTA') {
                    window.location.href = 'TelaPrincipalNutri.html';
                }
                else if (data.tipo === 'EDUCADOR_FISICO') {
                    window.location.href = 'painel-educador.html';
                }
                else if (data.tipo === 'PSICOLOGO') {
                    window.location.href = 'painel-psicologo.html';
                }
                else {
                    alert("Erro: Tipo de usuário desconhecido recebido do servidor: " + data.tipo);
                    console.error("Tipo desconhecido:", data);
                    botao.innerText = textoOriginal;
                    botao.disabled = false;
                }

            } else {
                // Se errou a senha ou usuário (401 Unauthorized)
                botao.innerText = textoOriginal;
                botao.disabled = false;
                alert("Login ou senha incorretos. Tente novamente.");
            }

        } catch (error) {
            console.error('Erro de rede:', error);
            botao.innerText = textoOriginal;
            botao.disabled = false;
            alert("Erro ao conectar com o servidor. Verifique se o backend está rodando.");
        }
    });
});