document.getElementById("formNutricionista").addEventListener("submit", function(event) {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const crnUf = document.getElementById("crnUf").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const confirmarSenha = document.getElementById("confirmarSenha").value;

    if (senha !== confirmarSenha) {
        alert("As senhas não conferem!");
        return;
    }

    const nutricionista = { nome, crnUf, email, senha };

    // A URL está correta: /api/nutricionistas/cadastro
    fetch("http://localhost:8080/api/nutricionistas/cadastro", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nutricionista)
    })
        .then(response => {
            // Verifica se a resposta NÃO foi bem-sucedida (status não é 2xx)
            if (!response.ok) {
                const status = response.status;

                // Tenta ler o corpo da resposta como JSON (esperando ErrorResponse)
                return response.json()
                    .then(errorData => {
                        let detailedMessage = errorData.message || `Erro ${status}`;

                        // Mensagem específica para conflito (CRN duplicado)
                        if (status === 409) {
                            detailedMessage = errorData.message || "CRN já cadastrado. Verifique os dados.";
                        }
                        // Adicione outros 'else if' para status específicos se necessário (ex: 400 para validação)

                        throw new Error(detailedMessage); // Lança erro com msg detalhada
                    })
                    .catch(() => {
                        // Fallback se não for JSON
                        if (status === 409) {
                            throw new Error("CRN já cadastrado.");
                        } else if (status === 400) {
                            throw new Error("Requisição inválida. Verifique os dados enviados.");
                        } else if (status === 403) {
                            throw new Error("Acesso negado. Verifique suas permissões.");
                        } else if (status === 500) {
                            throw new Error("Erro interno no servidor. Tente novamente mais tarde.");
                        } else {
                            throw new Error(`Erro ${status}. Não foi possível completar o cadastro.`);
                        }
                    });
            }
            // Se a resposta FOI bem-sucedida (status 2xx, como 201 Created)
            // O backend retorna a entidade Nutricionista, podemos tentar lê-la
            return response.json();
        })
        .then(nutriCriado => {
            // Este .then() só executa se o cadastro foi bem-sucedido
            alert("Cadastro de nutricionista realizado com sucesso!");
            window.location.href = "login.html"; // Redireciona para login
        })
        .catch(error => {
            // Pega *qualquer* erro lançado nos blocos .then() anteriores
            // Exibe a mensagem de erro específica que foi construída
            alert("Erro no cadastro: " + error.message);
            console.error("Detalhes do erro de cadastro:", error);
        });
});

// Preview da foto (mantido como antes)
document.getElementById("foto").addEventListener("change", function(event) {
    const file = event.target.files[0];
    const preview = document.getElementById("fotoPreview");

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.style.backgroundImage = `url(${e.target.result})`;
            preview.style.backgroundSize = "cover";
            preview.style.color = "transparent";
        };
        reader.readAsDataURL(file);
    }
});