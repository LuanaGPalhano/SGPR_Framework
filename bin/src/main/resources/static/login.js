document.getElementById("formLogin").addEventListener("submit", async function(e) {
    e.preventDefault();

    const login = document.getElementById("login").value;
    const senha = document.getElementById("senha").value;

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ login, senha })
        });

        if (response.ok) {
            const data = await response.json();

            // Redireciona baseado no tipo de usuário
            if (data.tipo === "PACIENTE") {
                window.location.href = "TelaPrincipalPac.html";
            } else if (data.tipo === "NUTRICIONISTA") {
                window.location.href = "TelaPrincipalNutri.html";
            } else {
                alert("Usuário sem tipo definido.");
            }
        } else {
            alert("Login (CPF/CRN) ou senha inválidos!");
        }
    } catch (error) {
        console.error("Erro ao conectar com backend:", error);
        alert("Erro de conexão com o servidor. Verifique se o backend está rodando.");
    }
});