document.addEventListener('DOMContentLoaded', () => {

    const loadingMessage = document.getElementById('loading-message');
    const favoritosContainer = document.getElementById('favoritos-container');
    const PACIENTE_ID = 7; // Mantenha como está para testes, mas lembre-se de tornar dinâmico
    const API_BASE_URL = 'http://localhost:8080';

    // Flag para evitar múltiplas chamadas de carregamento concorrentes
    let isLoading = false;

    function formatarData(dataIso) {
        if (!dataIso) return '';
        try {
            const data = new Date(dataIso);
            const dia = String(data.getDate()).padStart(2, '0');
            const mes = String(data.getMonth() + 1).padStart(2, '0'); // Meses começam do 0
            const ano = data.getFullYear();
            const hora = String(data.getHours()).padStart(2, '0');
            const minuto = String(data.getMinutes()).padStart(2, '0');
            return `${dia}/${mes}/${ano} ${hora}:${minuto}`;
        } catch (e) {
            console.error("Erro ao formatar data:", e);
            return dataIso;
        }
    }

    async function carregarFavoritos() {
        // Verifica se já está carregando para evitar chamadas duplicadas
        if (isLoading) {
            console.log("Carregamento já em andamento.");
            return;
        }
        isLoading = true; // Marca que o carregamento iniciou

        loadingMessage.style.display = 'block';
        favoritosContainer.innerHTML = ''; // Limpa o container *antes* de buscar os dados

        try {
            const url = `${API_BASE_URL}/api/favoritos/${PACIENTE_ID}`;
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Erro ao buscar favoritos: ${response.statusText || response.status} (${response.status})`);
            }

            const favoritos = await response.json();
            loadingMessage.style.display = 'none';

            if (favoritos.length === 0) {
                favoritosContainer.innerHTML = '<p class="no-favorites">Você ainda não salvou nenhum cardápio favorito.</p>';
            } else {
                // Renderiza todos os favoritos
                favoritos.forEach(favorito => {
                    const itemElement = document.createElement('div');
                    itemElement.classList.add('favorito-item');
                    itemElement.id = `favorito-${favorito.id}`;
                    // Inclui o botão de excluir
                    itemElement.innerHTML = `
                        <button class="delete-button" data-id="${favorito.id}" title="Excluir favorito">X</button>
                        <h4>${favorito.refeicaoNome}</h4>
                        <p>${favorito.sugestaoTexto}</p>
                        <small>Salvo em: ${formatarData(favorito.dataCriacao)}</small>
                    `;
                    favoritosContainer.appendChild(itemElement);
                });
            }

        } catch (error) {
            console.error('Falha ao carregar favoritos:', error);
            loadingMessage.style.display = 'none';
            favoritosContainer.innerHTML = `<p class="no-favorites" style="color: red;">Erro ao carregar favoritos: ${error.message}. Tente novamente mais tarde.</p>`;
        } finally {
            isLoading = false; // Marca que o carregamento terminou (mesmo se falhou)
        }
    }

    async function excluirFavoritoBackend(favoritoId) {
        try {
            const url = `${API_BASE_URL}/api/favoritos/${favoritoId}/paciente/${PACIENTE_ID}`;
            const response = await fetch(url, { method: 'DELETE' });

            if (response.ok) {
                const itemParaRemover = document.getElementById(`favorito-${favoritoId}`);
                if (itemParaRemover) {
                    itemParaRemover.remove();
                }
                if (favoritosContainer.children.length === 0) {
                    favoritosContainer.innerHTML = '<p class="no-favorites">Você não tem mais cardápios favoritos.</p>';
                }
                console.log('Favorito excluído com sucesso!');
            } else {
                console.error(`Erro ao excluir: ${response.statusText || response.status}`);
                alert('Erro ao excluir o favorito.'); // Mantém um alerta simples para o erro
            }
        } catch (error) {
            console.error('Falha ao excluir favorito:', error);
            alert('Ocorreu um erro de rede ao tentar excluir o favorito.');
        }
    }

    favoritosContainer.addEventListener('click', (event) => {
        // Verifica se o elemento clicado TEM a classe 'delete-button'
        if (event.target.classList.contains('delete-button')) {
            const favoritoId = event.target.getAttribute('data-id');
            // A confirmação agora está DENTRO do listener único
            if (confirm('Tem certeza que deseja excluir este favorito?')) {
                excluirFavoritoBackend(favoritoId);
            }
        }
    });

    // --- Execução Inicial ---
    carregarFavoritos();

});