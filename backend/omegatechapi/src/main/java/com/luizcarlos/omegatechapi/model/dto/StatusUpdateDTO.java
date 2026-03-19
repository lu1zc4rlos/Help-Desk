package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Novo status")
@Data
public class StatusUpdateDTO {

    @Schema(example = "EM_ANDAMENTO")
    private String novoStatus;
}
