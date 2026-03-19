package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Tickets do usuário")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "ERRO_AO_ABRIR_PROGRAMA")
    private String titulo;

    @Schema(example = "O usuário informou que, ao tentar acessar o sistema corporativo nesta manhã, não conseguiu realizar o login. Segundo ele, após inserir suas credenciais, a tela permanece carregando por alguns segundos e retorna uma mensagem de erro informando “Falha na autenticação. Tente novamente mais tarde.”\\n\\nO usuário relata que o problema começou após a atualização automática do Windows ocorrida no dia anterior. Ele já tentou reiniciar o computador e trocar a senha, porém o erro persiste. Outros aplicativos locais funcionam normalmente, indicando que o computador está operacional. No entanto, o acesso ao sistema interno permanece indisponível, impedindo a execução de suas atividades diárias.\\n\\nFoi solicitado ao helpdesk que verifique se há instabilidade no servidor de autenticação, problemas na comunicação com o domínio ou necessidade de resetar o perfil do usuário. O chamado permanece em aberto, aguardando análise técnica.")
    private String descricao;

    @Schema(example = "2025-11-22")
    private LocalDate dataCriacao;

    @Schema(example = "ALTA")
    private String prioridade;

    @Schema(example = "CONCLUIDO")
    private String status;

    @Schema(example = "Olá! Obrigado por entrar em contato.\\n\\nEntendemos a urgência da situação e vamos te ajudar a restabelecer o acesso o mais rápido possível.\\n\\nPrimeiro, agradecemos por já ter feito as verificações iniciais (reinício do computador e do roteador, teste da senha no portal web e confirmação de internet). Isso realmente ajuda na análise.\\n\\nVamos verificar internamente se há alguma instabilidade no servidor da VPN ou se existe alguma inconsistência no seu usuário de acesso. Enquanto isso, peço que nos envie duas informações para agilizar o diagnóstico:\\n\\nO nome exato do aplicativo utilizado (ex.: Cisco AnyConnect);\\n\\nSe a mensagem de erro exibe algum código adicional além de “Falha na conexão” ou “Servidor não respondeu”.\\n\\nAssim que tivermos o retorno da equipe responsável ou mais detalhes da sua conta, entraremos em contato imediatamente.\\n\\nObrigado pela paciência — estamos trabalhando para resolver sua conexão o quanto antes.")
    private String resposta;

    @Schema(example = "12")
    private Long clienteId;

    @Schema(example = "Usuário")
    private String nomeCliente;

    @Schema(example = "14")
    private Long tecnicoId;

    @Schema(example = "Técnico")
    private String nomeTecnico;
}
