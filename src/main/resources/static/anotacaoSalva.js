document.addEventListener("DOMContentLoaded", function() {
    // Recebe o objeto "anotacao" que foi passado pela janela pai
    const anotacaoJSON = sessionStorage.getItem("anotacaoVisualizacao");
    if(!anotacaoJSON) return;
    const anotacao = JSON.parse(anotacaoJSON);

    // Preenche os campos
    const dataElem = document.getElementById("data");
    dataElem.textContent = anotacao.registroHorario
        ? new Date(anotacao.registroHorario).toLocaleDateString("pt-BR") 
        : "Data desconhecida";

    document.getElementById("texto").value = anotacao.texto || "";

    const imgElem = document.getElementById("imagem");
    if(anotacao.imgURL){
        imgElem.src = anotacao.imgURL;
        imgElem.style.display = "block";
    }

    const refeicoesElem = document.getElementById("refeicoes");
    if(anotacao.entradas && anotacao.entradas.length > 0){
        refeicoesElem.innerHTML = "<h3>Refeições do dia:</h3>" + 
            anotacao.entradas.map(e => `<div>${e.descricao}</div>`).join("");
    }
});