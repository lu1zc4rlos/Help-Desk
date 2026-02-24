export function setupCadastroEvents(container) {
    const campoData = container.getElementById('dataNascimento');

    if (campoData) {
        const dataAtual = new Date().toISOString().split('T')[0];
        campoData.max = dataAtual;
    }

    const mostrarSenhaCheckbox = container.getElementById('mostrarSenha');
    const senhaInput = container.getElementById('senhaInput');
    const confirmarSenhaInput = container.getElementById('confirmarSenhaInput');

    mostrarSenhaCheckbox.addEventListener('change', () => {
        const tipo = mostrarSenhaCheckbox.checked ? 'text' : 'password';
        senhaInput.type = tipo;

        if (confirmarSenhaInput) {
            confirmarSenhaInput.type = tipo;
        }
    })

}

