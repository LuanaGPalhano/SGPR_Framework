// Adiciona um "ouvinte" para o evento de submissão do formulário com id "formHistorico"
document.getElementById("formHistorico").addEventListener("submit", function(event) {
    // Impede o comportamento padrão do formulário, que é recarregar a página
    event.preventDefault();

    // 1. Coleta dos dados do formulário
    const pacienteId = document.getElementById("pacienteId").value;
    const estadoCivil = document.getElementById("estadoCivil").value;
    const ocupacao = document.getElementById("ocupacao").value;
    const alergias = document.getElementById("alergias").value;
    const medicamentos = document.getElementById("medicamentos").value;
    const suplementacao = document.getElementById("suplementacao").value;
    const historicoFamiliar = document.getElementById("historicoFamiliar").value;
    const outrasCondicoes = document.getElementById("outrasCondicoes").value;
    
    const bebeValue = document.querySelector('input[name="bebe"]:checked').value;
    const fumaValue = document.querySelector('input[name="fuma"]:checked').value;

   
    const historicoData = {
        estadoCivil,
        ocupacao,
        alergias,
        medicamentos,
        suplementacao,
        historicoFamiliar,
        outrasCondicoes,
        isBebe: bebeValue === 'true',
        isFuma: fumaValue === 'true'
    };

    fetch(`http://localhost:8080/api/pacientes/${pacienteId}/historico`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(historicoData)
    })
    .then(response => {
        if (response.ok) {
            alert("Histórico do paciente cadastrado com sucesso!");
            // Redireciona para a página de detalhes do paciente, por exemplo
            window.location.href = `paciente-detalhes.html?id=${pacienteId}`; 
        } else {
            // Se der erro, tenta extrair uma mensagem do corpo da resposta
            return response.text().then(msg => {
                throw new Error(msg || "Não foi possível cadastrar o histórico.");
            });
        }
    })
    .catch(error => {
        // Captura erros de rede ou os erros lançados no .then()
        alert("Erro: " + error.message);
        console.error("Detalhes do erro:", error);
    });
});
