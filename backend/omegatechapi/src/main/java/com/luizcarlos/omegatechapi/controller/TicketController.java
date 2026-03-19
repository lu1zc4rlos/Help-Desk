package com.luizcarlos.omegatechapi.controller;

import com.luizcarlos.omegatechapi.config.exception.UnauthorizedException;
import com.luizcarlos.omegatechapi.model.dto.AuthResponseDTO;
import com.luizcarlos.omegatechapi.model.dto.RespostaTicketDTO;
import com.luizcarlos.omegatechapi.model.dto.StatusUpdateDTO;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.request.MensagemRequest;
import com.luizcarlos.omegatechapi.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Chamados", description = "Endpoints para abertura, consulta e gerenciamento de chamados técnicos")
public class TicketController {

    @Autowired
    private final TicketService ticketService;

    @Operation(
            summary = "Criar Ticket",
            description = "Criação de ticket"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/criar")
    public ResponseEntity<Void> criarTicket(
            @RequestBody MensagemRequest request,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        ticketService.criarTicket(request.getMensagem(),usuarioAutenticado);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Buscar Tickets",
            description = "Buscar tickets do usuário"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            array = @ArraySchema( schema = @Schema(implementation = TicketResponseDTO.class))
                    )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/meus")
    public ResponseEntity<List<TicketResponseDTO>> getMeusTickets(
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        if (usuarioAutenticado == null) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        Long clienteId = usuarioAutenticado.getId();
        List<TicketResponseDTO> tickets = ticketService.findTicketsByUserIdAndStatus(clienteId, status);

        return ResponseEntity.ok(tickets);
    }

    @Operation(
            summary = "Atualizar status",
            description = "Atualizar status do ticket"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ROLE_TECNICO')")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateDTO request,
            @AuthenticationPrincipal Usuario tecnico) {

        ticketService.atualizarStatus(id, request.getNovoStatus(), tecnico);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Buscar Ticket",
            description = "Buscar ticket do usuário por id"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            schema = @Schema(implementation = TicketResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "403", description = "ForbiddenException", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketResponseDTO> getTicketById(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        TicketResponseDTO ticket = ticketService.buscarTicketPorId(id, usuarioAutenticado.getId(), usuarioAutenticado.getPerfil());

        return ResponseEntity.ok(ticket);
    }

    @Operation(
            summary = "Respondendo Ticket",
            description = "Respondendo ticket do usuário pelo técnico"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "400", description = "BadRequestException", content = @Content),
            @ApiResponse(responseCode = "403", description = "ForbiddenException", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("/resposta/{id}")
    @PreAuthorize("hasRole('ROLE_TECNICO')")
    public ResponseEntity<Void> responderTicket(
            @PathVariable Long id,
            @RequestBody RespostaTicketDTO request,
            @AuthenticationPrincipal Usuario tecnico) {

        ticketService.responderTicket(id, request.getResposta(), tecnico);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Deletar ticket",
            description = "Deletando ticket do usuário"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "BadRequestException", content = @Content),
            @ApiResponse(responseCode = "403", description = "ForbiddenException", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    public ResponseEntity<Void> excluirTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario cliente) {

        ticketService.excluirTicket(id, cliente.getId());

        return ResponseEntity.noContent().build();
    }
}
