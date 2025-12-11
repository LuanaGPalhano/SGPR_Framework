document.getElementById("formPaciente").addEventListener("submit", function(event) {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const cpf = document.getElementById("cpf").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const confirmarSenha = document.getElementById("confirmarSenha").value;

    if (senha !== confirmarSenha) {
        alert("As senhas não conferem!");
        return;
    }

    const paciente = { nome, cpf, email, senha };

    fetch("http://localhost:8080/api/pacientes/cadastro", { // URL CORRIGIDA
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(paciente)
    })
        .then(response => {
            if (!response.ok) {
                const status = response.status;

                return response.json()
                    .then(errorData => {
                        let detailedMessage = errorData.message || `Erro ${status}`; // Pega a msg do ErrorResponse
                        if (status === 409) {
                            detailedMessage = errorData.message || "CPF já cadastrado. Verifique os dados."; // Usa a msg do backend ou um fallback claro
                        }
                        throw new Error(detailedMessage);
                    })
                    .catch(() => {
                        if (status === 409) {
                            throw new Error("CPF já cadastrado.");
                        } else if (status === 400) {
                            throw new Error("Requisição inválida. Verifique os dados enviados.");
                        } else if (status === 500) {
                            throw new Error("Erro interno no servidor. Tente novamente mais tarde.");
                        } else {
                            throw new Error(`Erro ${status}. Não foi possível completar o cadastro.`);
                        }
                    });
            }
            return response.json();
        })
        .then(pacienteCriado => {
            alert("Cadastro realizado com sucesso!");
            window.location.href = "login.html";
        })
        .catch(error => {
            alert("Erro no cadastro: " + error.message);
            console.error("Detalhes do erro de cadastro:", error);
        });
});

// Preview da foto
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
