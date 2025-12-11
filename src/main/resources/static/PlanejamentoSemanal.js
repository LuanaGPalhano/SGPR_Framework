document.addEventListener("DOMContentLoaded", function() {

const btnVoltar = document.getElementById("btnSair");

btnVoltar.addEventListener("click", function(){
    globalThis.history.back();
})
    
    const selecaoSemana = document.querySelector("#semanaEscolha");

    const inicioPadrao = new Date().toISOString().slice(0, 10);

    fetch('http://localhost:8080/planejamento/semanas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ inicio: inicioPadrao })
    })
    .then(res => res.json())
    .then(semanas => {
        semanas.forEach(s => {
            const option = document.createElement("option");
            option.value = s.value;
            option.textContent = s.label;
            selecaoSemana.appendChild(option);
        });
        if(semanas.length) selecaoSemana.value = semanas[0].value;
    })
    .catch(err => console.error(err));

    
    //AREA DE PLANEJAMENTO
    const btnAdicionar = document.querySelectorAll(".adicaoRefeicao");

    btnAdicionar.forEach(function(btnAdicao) {
        btnAdicao.addEventListener("click", function() {
            //CRIA UMA AREA DE TEXTO
            let novaRefeicao = document.createElement("textarea");
            novaRefeicao.placeholder = "Adicione sua dieta";
            novaRefeicao.classList.add("novaRefeicao");

            //CRIA UM BOTÃO DE SALVAR E EXCLUIR
            let btnSalvar = document.createElement("button");
            btnSalvar.textContent = "Salvar";
            btnSalvar.classList.add("btnSalvar");

            let wrapper = document.createElement("div");
            wrapper.appendChild(novaRefeicao);
            wrapper.appendChild(btnSalvar);

            btnAdicao.parentNode.insertBefore(wrapper, btnAdicao);
            novaRefeicao.focus();

            btnSalvar.addEventListener("click", function() {
                let refeicaoText = novaRefeicao.value.trim();
                if(!refeicaoText)  return;

                const linhas = refeicaoText.split("\n").slice(1);

                const porcoes = linhas.map(linha => {
                    const[alimento, quantidade] = linha.split(" - ");
                    return{alimento : alimento?.trim(), quantidade: quantidade?.trim()};
                });

                    const planejamento = {
                        descricao: `Plano alimentar da semana ${selecaoSemana.value}`,
                        entradas: [
                            {
                                dia: btnAdicao.closest(".containerDia").querySelector("h4").textContent,
                                refeicao: refeicaoText.split("\n")[0],
                                porcoes: porcoes
                            }
                        ]
                    };

                    fetch("http://localhost:8080/planejamento", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify(planejamento)
                    })
                    .then(res => res.json())
                    .then(data => console.log("Planejamento salvo:", data))
                    .catch(err => console.error("Erro ao salvar:", err));

                    //CRIANDO CARD DE REFEICAO
                    let card = document.createElement("div");
                    card.classList.add("card");

                    let conteudo = document.createElement("div");
                    conteudo.innerHTML = refeicaoText.replace(/\n/g, "<br>");

                    //CRIA BOTÃO DE EXCLUIR O CARD
                    let btnExcluir = document.createElement("button");
                    btnExcluir.textContent = "x";
                    btnExcluir.classList.add("btnExcluir");

                    card.appendChild(btnExcluir);
                    card.appendChild(conteudo);
                    btnAdicao.parentNode.insertBefore(card, btnAdicao);

                    btnExcluir.addEventListener("click", function() {
                        card.remove();
                    }); 
                    wrapper.remove();
                }
            );
        });
    });
});