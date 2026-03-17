package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "resposta de erro da Api")
public class ErrorResponseDTO {

    @Schema(description = "status code error")
    private int status;

    @Schema(description = "description status code error")
    private String message;
}
