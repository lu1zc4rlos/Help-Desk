export function renderLogin(container) {
    container.innerHTML = `
        <h1>Login</h1>
        <form action="">
        <div class="campo">
            <label for="email">Email:</label>
            <input type="email" name="email" id="email"><br>
        </div>

        <div class="campo">
            <label for="senhaInput">Senha: </label>
            <input type="password" name="senhaInput" id="senhaInput"><br>
        </div>

        <div class="campo">
            <input type="checkbox" name="mostrarSenha" id="mostrarSenha">
            <label for="mostrarSenha">Mostrar Senha</label>
            <br>
        </div>

        <div class="campo">
            <button type="submit">Entrar</button>
            <button type="button" onclick="window.location.href='g.html'">Cadastrar</button>
            <button type="button" onclick="window.location.href='rec.html'">Recuperar Senha</button>
        </div>

    </form>

        <p>Não possui cadastro? <button id="cadastro-button" data-link data-path="/cadastro">Cadastrar-se</button></p>
    `;


}