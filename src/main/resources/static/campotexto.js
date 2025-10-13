//ARQUIVO: CAMPO DE TEXTO

document.addEventListener("DOMContentLoaded", function() {
//ADICAO DE REFEIÇOES
const botaoAdiciona = document.getElementById('botAdiciona');
const refInput = document.getElementById('refeicaoInput');
const refLista = document.getElementById('listaRefeicoes');

let Refeicoes = [];

function adicionarRefeicao() {
    if(refInput.value.trim() !== ""){
        Refeicoes.push(refInput.value);
        const div = document.createElement("div");
        div.textContent = refInput.value;
        refLista.appendChild(div);
        refInput.value = "";
    };
};

    botaoAdiciona.addEventListener('click', function(e){
    e.preventDefault();
    adicionarRefeicao();
});

refInput.addEventListener("keydown", function(event){
    if(event.key == "Enter"){
        event.preventDefault();
        adicionarRefeicao();
    }
});

//ADIÇÃO DE IMAGENS
const inputImg = document.getElementById('adicaoImagem');
const botaoImg = document.getElementById('btImagem');
const imgPadrao = document.getElementById('iconePadrao');

let imgURL = null;

function abrirInput(){
    inputImg.click();
}

botaoImg.addEventListener('click', abrirInput);
//TROCA A IMAGEM PADRAO DO SITE POR UMA ESCOLHIDA PELO USUÁRIO
inputImg.addEventListener('change', function(){
    arqEscolhido = inputImg.files[0];
    if(arqEscolhido){
        const reader = new FileReader();
        reader.onload = function(e){
        imgPadrao.src = e.target.result;
        botaoImg.style.display = 'none';
        imgURL = e.target.result;
        };

    reader.readAsDataURL(arqEscolhido);
    }
});

//SALVANDO CONTEÚDO 

const areaTexto = document.querySelector("textarea");
const botaoSave = document.getElementById("salvarConteudo");

botaoSave.addEventListener("click", function(){
    const texto = areaTexto.value.trim();
    if(!texto && Refeicoes.length == 0 && !imgURL){
        alert("Você não pode salvar uma página vazia");
        return;
    }

    const payload = {
        texto: texto,
        refeicoes: Refeicoes.map(r => ({conjuntoRefeicoes: r})),
        imgURL: imgURL
    };

    

    fetch('http://localhost:8080/diario', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
        .then(res => {
            if(res.ok) {
                alert("Anotação salva com sucesso!");
                window.close(); // fecha a janela
            } else {
                alert("Erro ao salvar a anotação!");
            }
        })
        .catch(err => console.error(err)); 
    });
});