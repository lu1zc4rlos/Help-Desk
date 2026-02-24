export function validateCadastroForm() {

    const erros = {};

    const name = document.getElementById('name').value;
    const dataNascimento = document.getElementById('dataNascimento').value;
    const email = document.getElementById('email').value;
    const senhaInput = document.getElementById('senhaInput').value;
    const confirmarSenhaInput = document.getElementById('confirmarSenhaInput').value;

    if(!name){
        erros.name = "Nome é obrigatório";
    }else if(name.length < 3){
        erros.name = "Nome deve conter pelo menos 3 caracteres";
    }

    if(!dataNascimento){
        erros.dataNascimento = "Data de nascimento é obrigatória";
    }

    if(!email){
        erros.email = "Email é obrigatório";
    }else if(!/\S+@\S+\.\S+/.test(email)){
        erros.email = "Email inválido";
    }

    if(!senhaInput){
        erros.senhaInput = "Senha é obrigatória";
    }else if(senhaInput.length < 8){
        erros.senhaInput = "Senha deve conter pelo menos 8 caracteres";
    }else if(!/[A-Z]/.test(senhaInput)){
        erros.senhaInput = "Senha deve conter pelo menos uma letra maiúscula";
    }else if(!/[a-z]/.test(senhaInput)){
        erros.senhaInput = "Senha deve conter pelo menos uma letra minúscula";
    }else if(!/[0-9]/.test(senhaInput)){
        erros.senhaInput = "Senha deve conter pelo menos um número";
    } else if(!/[!@#$%^&*]/.test(senhaInput)){
        erros.senhaInput = "Senha deve conter pelo menos um caractere especial";
    }

    if(senhaInput !== confirmarSenhaInput){
        erros.confirmarSenhaInput = "As senhas não coincidem";
    }

    return erros;
}