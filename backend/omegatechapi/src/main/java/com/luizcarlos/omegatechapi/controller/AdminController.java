/*
Este é um novo controller, dedicado a ações que apenas administradores podem realizar. É uma ótima prática para organizar e proteger seus endpoints.

Lógica Mapeada da BLL: Parte do UsuarioBLL.cs e novas lógicas.

Endpoints que ele conteria:

POST /api/admin/tecnicos: O endpoint que já projetamos para criar um novo técnico. Ele recebe o CriarTecnicoRequest e chama o usuarioService.criarTecnico().

GET /api/admin/usuarios: Para listar todos os usuários do sistema.

DELETE /api/admin/usuarios/{id}: Para deletar um usuário.

PUT /api/admin/usuarios/{id}/perfil: Para alterar o perfil de um usuário (promover para técnico ou admin).
 */

package com.luizcarlos.omegatechapi.controller;
import com.luizcarlos.omegatechapi.model.dto.AuthResponseDTO;
import com.luizcarlos.omegatechapi.model.dto.TecnicoResponseDTO;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.service.TecnicoService;
import com.luizcarlos.omegatechapi.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Administração", description = "Endpoints administrativos para gerenciamento de técnicos do sistema")
public class AdminController {
    @Autowired
    private TecnicoService tecnicoService;

    private final TicketService ticketService;

    @Autowired
    public AdminController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(
            summary = "Cadastro do técnico",
            description = "Cadastro dos novos técnicos do sistema"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            schema = @Schema(implementation = Usuario.class)
                    )),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrarTecnico(@RequestBody Usuario novoTecnico) {

        tecnicoService.criarTecnico(novoTecnico);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Buscar técnicos",
            description = "Endpoint restrito a administradores para listagem de técnicos cadastrados"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema( schema = @Schema(implementation = TecnicoResponseDTO.class))
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/tecnicos")
    public ResponseEntity<List<TecnicoResponseDTO>> buscarTodos() {

        List<TecnicoResponseDTO> tecnicos = tecnicoService.buscarTodosTecnicos();
        return new ResponseEntity<>(tecnicos, HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar técnico",
            description = "Buscar técnico cadastrado no sistema por id"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            schema = @Schema(implementation = TecnicoResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> buscarPorId(@PathVariable("id") Long id) {

            TecnicoResponseDTO tecnico = tecnicoService.buscarTecnicoPorId(id);
            return ResponseEntity.ok(tecnico);

    }

    @Operation(
            summary = "Buscar tickets respondidos",
            description = "Buscar tickets respondidos pelo técnico"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema( schema = @Schema(implementation = TicketResponseDTO.class))
                    )),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/respondidos/{tecnicoId}")
    public ResponseEntity<List<TicketResponseDTO>> buscarTicketsRespondidosPorTecnico(
            @PathVariable("tecnicoId") Long tecnicoId)
    {
        List<TicketResponseDTO> tickets = ticketService.buscarTicketsRespondidos(tecnicoId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
