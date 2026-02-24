import { setupCadastroEvents } from "./cadastroEvents";

export function renderCadastro(container){
    container.innerHTML = `
       
    <form action="" method="post">

        <h1>Cadastro</h1>

        <div class="campo">
            <label for="name"> Nome:</label>
            <input type="text" name="name" id="name"><br>
            <ul id="text-rules">
                <li id="rule-length-nome">O nome deve conter pelo menos 3 caracteres</li>
            </ul>
        </div>

        <div class="campo">
            <label for="dataNascimento"> Data de Nascimento:</label>
            <input type="date" name="dataNascimento" id="dataNascimento"><br>
            <ul id="date-rules">
                <li id="rule-date-required">A data de nascimento é obrigatória</li>
            </ul>
        </div>

        <div class="campo">
            <label for="email"> Email:</label>
            <input type="email" name="email" id="email"><br>
            <ul id="email-rules">
                <li id="rule-email-required">O email é obrigatório</li>
                <li id="rule-email-invalid">O email deve ser válido</li>
            </ul>
        </div>

        <div class="campo">
            <label for="senhaInput">Senha: </label>
            <input type="password" name="senhaInput" id="senhaInput"><br>
            <ul id="password-rules"></ul>
                <li id="rule-password-required">A senha é obrigatória</li>
                <li id="rule-password-length">A senha deve conter pelo menos 8 caracteres</li>
                <li id="rule-password-uppercase">A senha deve conter pelo menos uma letra maiúscula</li>
                <li id="rule-password-lowercase">A senha deve conter pelo menos uma letra minúscula</li>
                <li id="rule-password-number">A senha deve conter pelo menos um número</li>
                <li id="rule-password-special">A senha deve conter pelo menos um caractere especial</li>
            </ul>
        </div>

        <div class="campo">
            <label for="confirmarSenhaInput">Confirmar Senha: </label>
            <input type="password" name="confirmarSenhaInput" id="confirmarSenhaInput"><br>
            <ul id="confirm-password-rules"></ul>
                <li id="rule-confirm-password-match">As senhas devem coincidir</li>
            </ul>

        </div>

        <div class="campo">
            <input type="checkbox" name="mostrarSenha" id="mostrarSenha">
            <label for="mostrarSenha">Mostrar Senha</label>
            <br>
        </div>

        <div class="campo">
            <button type="button" onclick="window.location.href='index.html'">tela de login</button>
             <button type="submit"> Cadastrar </button>
        </div>


    </form>
        <p>Já possui cadastro? <button id="login-button" data-link data-path="/login">Login</button></p>
    `;
}   