package com.luizcarlos.omegatechapi.model.enums;

public enum TipoProblema {
    // ==========================
    // 💻 PROBLEMAS DE DESEMPENHO
    // ==========================
    COMPUTADOR_TRAVANDO("Computador travando"),
    LENTIDAO_DO_SISTEMA("Lentidão do sistema"),
    APLICATIVO_NAO_RESPONDE("Aplicativo não responde"),
    SISTEMA_OPERACIONAL_LENTO("Sistema operacional lento"),
    REINICIALIZACOES_FREQUENTES("Reinicializações automáticas ou falhas frequentes"),

    // ==========================
    // 🖥️ HARDWARE
    // ==========================
    FALHA_NO_MONITOR("Falha no monitor"),
    TECLADO_OU_MOUSE_NAO_FUNCIONAM("Teclado ou mouse não funcionam"),
    IMPRESSORA_NAO_IMPRIME("Impressora não imprime"),
    PROBLEMAS_COM_SCANNER("Problemas com scanner"),
    FALHA_NO_HD_OU_SSD("Falha no HD/SSD"),
    PROBLEMA_DE_ENERGIA("Problema de energia (não liga ou desliga sozinho)"),
    PERIFERICOS_NAO_RECONHECIDOS("Periféricos não reconhecidos"),

    // ==========================
    // 🌐 REDE / INTERNET
    // ==========================
    SEM_ACESSO_A_INTERNET("Sem acesso à internet"),
    CONEXAO_INSTAVEL("Conexão instável ou lenta"),
    PROBLEMAS_COM_WIFI("Problemas com Wi-Fi"),
    FALHA_VPN("Falha de conexão VPN"),
    ERRO_DE_REDE_INTERNA("Erro de rede corporativa"),
    COMPARTILHAMENTO_DE_ARQUIVOS("Falha no compartilhamento de arquivos"),

    // ==========================
    // 📧 E-MAIL E COMUNICAÇÃO
    // ==========================
    ERRO_AO_ENVIAR_EMAIL("Erro ao enviar ou receber e-mails"),
    SENHA_DE_EMAIL_ESQUECIDA("Senha de e-mail esquecida"),
    CAIXA_DE_ENTRADA_CHEIA("Caixa de entrada cheia"),
    PROBLEMAS_NO_OUTLOOK("Problemas no Microsoft Outlook"),
    FALHA_DE_SINCRONIZACAO("Falha de sincronização com e-mail ou Teams"),

    // ==========================
    // 🔐 ACESSO E AUTENTICAÇÃO
    // ==========================
    ESQUECI_MINHA_SENHA("Esqueci minha senha"),
    CONTA_BLOQUEADA("Conta bloqueada"),
    PROBLEMAS_COM_2FA("Problemas com autenticação de dois fatores"),
    ACESSO_NEGADO("Acesso negado a sistema ou pasta"),
    USUARIO_SEM_PERMISSAO("Usuário sem permissão"),

    // ==========================
    // 🧰 SOFTWARES E APLICATIVOS
    // ==========================
    ERRO_AO_ABRIR_PROGRAMA("Erro ao abrir programa"),
    INSTALACAO_DE_SOFTWARE("Instalação de software necessária"),
    FALHA_NO_APLICATIVO("Aplicativo com falha ou travamento"),
    ATUALIZACAO_PENDENTE("Atualização de software pendente"),
    LICENCA_EXPIRADA("Licença de software expirada"),
    FALHA_EM_SISTEMA_INTERNO("Falha em sistema interno (ERP, CRM, etc.)"),

    // ==========================
    // 🗄️ ARQUIVOS E ARMAZENAMENTO
    // ==========================
    PERDA_DE_ARQUIVOS("Perda de arquivos"),
    PROBLEMAS_COM_BACKUP("Problemas com backup"),
    ESPACO_EM_DISCO_INSUFICIENTE("Espaço em disco insuficiente"),
    ARQUIVO_CORROMPIDO("Arquivo corrompido"),
    FALHA_AO_ACESSAR_PASTA("Falha ao acessar pasta de rede"),

    // ==========================
    // 🖨️ IMPRESSORAS E DIGITALIZAÇÃO
    // ==========================
    IMPRESSORA_OFFLINE("Impressora offline"),
    IMPRESSAO_TRAVADA("Impressão travada na fila"),
    ERRO_DE_DRIVER_DE_IMPRESSORA("Erro de driver de impressora"),
    CONFIGURACAO_DE_IMPRESSORA("Configuração de nova impressora"),
    FALHA_NA_DIGITALIZACAO("Falha na digitalização"),

    // ==========================
    // 🔌 EQUIPAMENTOS E INFRA
    // ==========================
    CABEAMENTO_DANIFICADO("Cabeamento de rede danificado"),
    TOMADA_SEM_ENERGIA("Tomada sem energia"),
    PROBLEMAS_COM_NOBREAK("Problemas com nobreak"),
    REQUISICAO_DE_EQUIPAMENTO("Requisição de novo equipamento"),
    SUBSTITUICAO_DE_PECA("Substituição de peça defeituosa"),

    // ==========================
    // 🧑‍💼 SOLICITAÇÕES GERAIS
    // ==========================
    CRIACAO_DE_USUARIO("Criação de novo usuário"),
    INSTALACAO_DE_EQUIPAMENTO("Instalação de equipamento"),
    SOLICITACAO_DE_ACESSO("Solicitação de acesso a pasta ou sistema"),
    CONFIGURACAO_DE_EMAIL("Configuração de e-mail corporativo"),
    DUVIDA_TECNICA("Dúvida sobre procedimento técnico"),

    // ==========================
    // 🔒 SEGURANÇA DA INFORMAÇÃO
    // ==========================
    SUSPEITA_DE_VIRUS("Suspeita de vírus ou malware"),
    EMAIL_DE_PHISHING("E-mail de phishing recebido"),
    DISPOSITIVO_COMPROMETIDO("Dispositivo comprometido"),
    SOLICITACAO_DE_ANTIVIRUS("Solicitação de antivírus"),
    BLOQUEIO_DE_SITE("Bloqueio de site indevido"),

    // ==========================
    // ⚙️ SUPORTE E ATENDIMENTO
    // ==========================
    SOLICITACAO_DE_ATENDIMENTO_REMOTO("Solicitação de atendimento remoto"),
    PROBLEMA_NAO_RESOLVIDO("Problema não resolvido em chamado anterior"),
    AGENDAMENTO_DE_MANUTENCAO("Agendamento de manutenção");

    private final String descricao;

    TipoProblema(String descricao) {
        this.descricao = descricao;
    }
}
