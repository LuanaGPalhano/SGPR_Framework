document.addEventListener("DOMContentLoaded", function() {

//ARQUIVO: DIARIO ALIMENTAR

const btnVoltar = document.getElementById("btnSair");

btnVoltar.addEventListener("click", function(){
    globalThis.history.back();
})

//MOSTRA A DATA NA TELA PRINCIPAL
const dataHoje = new Date();
document.getElementById("data").textContent = dataHoje.toLocaleDateString("pt-BR", {day: "numeric", month: "long", year: "numeric"});

//ABRIR JANELA PARA CAMPO DE TEXTO
const addNota = document.getElementById('novaAnotacao');

function abrirDiario(){

    const windowFeature = 'width=950, height=750, resizable=yes, scrollbars=yes';

    window.open('campotexto.html', 'MeuDiario', windowFeature);
}

addNota.addEventListener('click', abrirDiario);
});

//BLOCO ADICIONADO APÓS DIÁRIO SER SALVO
function adicionarBloco(anotacao){
    const blocoContainer = document.getElementById("containerDiario");

    const bloco = document.createElement("div");
    bloco.className = "blocoDiario";

    const dataRegistro = anotacao.registroHorario || new Date().toISOString();
    const data = new Date(dataRegistro);
    bloco.textContent = data.toLocaleDateString("pt-BR", {day: "2-digit", month: "long", year:"numeric", hour:"2-digit", minute:"2-digit"});

    bloco.addEventListener("click", function(){
        sessionStorage.setItem("anotacaoVisualizacao", JSON.stringify(anotacao));
        window.open(
            'anotacaoSalva.html', '_blank', 'width=750, height=750, resizable=yes, scrollbars=yes'
        );
    });
    blocoContainer.appendChild(bloco);
}
