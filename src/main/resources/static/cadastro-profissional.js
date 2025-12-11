document.addEventListener('DOMContentLoaded', () => {

    // --- 1. Lógica Visual: Trocar CRN por CREF ou CRP ---
    const selectTipo = document.getElementById('tipoProfissional');
    const labelRegistro = document.getElementById('labelRegistro');
    const inputRegistro = document.getElementById('registroInput');

    // Função que atualiza o texto do label
    function atualizarLabel() {
        const tipo = selectTipo.value;
        if (tipo === 'NUTRICIONISTA') {
            labelRegistro.innerText = 'CRN/UF';
            inputRegistro.placeholder = 'Ex: 15984/SP';
        } else if (tipo === 'EDUCADOR_FISICO') {
            labelRegistro.innerText = 'CREF/UF';
            inputRegistro.placeholder = 'Ex: 75698-G/SP';
        } else if (tipo === 'PSICOLOGO') {
            labelRegistro.innerText = 'CRP/UF';
            inputRegistro.placeholder = 'Ex: 06/65325';
        }
    }

    // Escuta a mudança no Select
    if (selectTipo) {
        selectTipo.addEventListener('change', atualizarLabel);
        atualizarLabel(); // Chama uma vez no início
    } else {
        console.error("ERRO CRÍTICO: Não encontrei o elemento <select id='tipoProfissional'> no HTML.");
    }

    // --- 2. Lógica Visual: Preview da Foto ---
    const inputFoto = document.getElementById('foto');
    const fotoPreview = document.getElementById('fotoPreview');

    if (inputFoto) {
        inputFoto.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(event) {
                    fotoPreview.style.backgroundImage = `url(${event.target.result})`;
                    fotoPreview.innerText = '';
                }
                reader.readAsDataURL(file);
            }
        });
    }

    // --- 3. Lógica de Envio ---
    const form = document.getElementById('formProfissional');

    if (!form) {
        console.error("ERRO CRÍTICO: Não encontrei o <form id='formProfissional'>. Verifique o HTML.");
        return;
    }

    form.addEventListener('submit', async function(event) {
        event.preventDefault();

        const senhaElement = document.getElementById('senha');
        const confirmarElement = document.getElementById('confirmarSenha');
        const nomeElement = document.getElementById('nome');
        const emailElement = document.getElementById('email');
        const registroElement = document.getElementById('registroInput');
        const tipoElement = document.getElementById('tipoProfissional');

        // Verificação de segurança para elementos nulos
        if (!senhaElement || !nomeElement || !emailElement) {
            alert("Erro no HTML: Alguns campos não foram encontrados pelo ID.");
            return;
        }

        const senha = senhaElement.value;
        const confirmarSenha = confirmarElement.value;

        if (senha !== confirmarSenha) {
            alert("As senhas não coincidem!");
            return;
        }

        // Montagem do Objeto (DTO)
        const dadosCadastro = {
            nome: nomeElement.value,
            email: emailElement.value,
            senha: senha,
            registroProfissional: registroElement.value,
            tipoProfissional: tipoElement.value
        };

        // === DEBUG: MOSTRA O QUE ESTÁ SENDO ENVIADO ===
        console.log("--- TENTANDO CADASTRAR ---");
        console.log("JSON Gerado:", JSON.stringify(dadosCadastro));

        if (!dadosCadastro.nome || !dadosCadastro.senha || !dadosCadastro.registroProfissional) {
            alert("Atenção: Alguns campos parecem estar vazios. Verifique o console.");
            return; // Não envia se estiver vazio
        }

        try {
            const response = await fetch('http://localhost:8080/api/profissionais/cadastro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosCadastro)
            });

            if (response.ok) {
                const profissionalSalvo = await response.json();
                console.log("Sucesso no Backend:", profissionalSalvo);
                alert(`Cadastro realizado com sucesso!\nBem-vindo(a), ${profissionalSalvo.nome}.`);
                window.location.href = 'login.html';
            } else {
                const errorText = await response.text();
                console.error("Erro do Backend:", errorText);
                let msg = "Erro ao cadastrar.";
                try {
                    const jsonErr = JSON.parse(errorText);
                    msg = jsonErr.message || msg;
                } catch (e) {}
                alert(`Erro: ${msg}`);
            }

        } catch (error) {
            console.error('Erro na requisição fetch:', error);
            alert('Ocorreu um erro de conexão.');
        }
    });
});