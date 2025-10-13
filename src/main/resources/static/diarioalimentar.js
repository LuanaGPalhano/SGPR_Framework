document.addEventListener("DOMContentLoaded", function() {

//ARQUIVO: DIARIO ALIMENTAR

//MOSTRA A DATA NA TELA PRINCIPAL
const dataHoje = new Date();
document.getElementById("data").textContent = dataHoje.toLocaleDateString("pt-BR", {day: "numeric", month: "long", year: "numeric"});

//ABRIR JANELA PARA CAMPO DE TEXTO
const addNota = document.getElementById('novaAnotacao');

function abrirDiario(){
    window.open(
        'campotexto.html', 'Meu Diário', 'width=450, height=450, resizable=yes, scrollbars=yes'
    );
}

addNota.addEventListener('click', abrirDiario);

});