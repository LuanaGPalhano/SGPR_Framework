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

    fetch("http://localhost:8080/api/cadastrar/paciente", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(paciente)
    })
        .then(response => {
            if (response.ok) {
                alert("Cadastro realizado com sucesso!");
                window.location.href = "login.html"; // volta para login
            } else {
                return response.text().then(msg => {
                    throw new Error(msg || "Erro ao cadastrar paciente!");
                });
            }
        })
        .catch(error => {
            alert("Erro: " + error.message);
            console.error(error);
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
