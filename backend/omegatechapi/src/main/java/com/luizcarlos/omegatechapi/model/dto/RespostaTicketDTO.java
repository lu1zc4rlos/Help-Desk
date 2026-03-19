package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Resposta do técnico")
@Data
public class RespostaTicketDTO {

    @Schema(example = "Olá! Obrigado por entrar em contato. Entendemos a urgência da situação e vamos te ajudar a restabelecer o acesso o mais rápido possível. Primeiro, agradecemos por já ter feito as verificações iniciais (reinício do computador e do roteador, teste da senha no portal web e confirmação de internet). Isso realmente ajuda na análise. Vamos verificar internamente se há alguma instabilidade no servidor da VPN ou se existe alguma inconsistência no seu usuário de acesso. Enquanto isso, peço que nos envie duas informações para agilizar o diagnóstico: O nome exato do aplicativo utilizado (ex.: Cisco AnyConnect); Se a mensagem de erro exibe algum código adicional além de “Falha na conexão” ou “Servidor não respondeu”. Assim que tivermos o retorno da equipe responsável ou mais detalhes da sua conta, entraremos em contato imediatamente. Obrigado pela paciência — estamos trabalhando para resolver sua conexão o quanto antes.")
    private String resposta;
}
