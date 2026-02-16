package com.luizcarlos.omegatechapi.controller;

import com.luizcarlos.omegatechapi.model.dto.RespostaTicketDTO;
import com.luizcarlos.omegatechapi.model.dto.StatusUpdateDTO;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.request.MensagemRequest;
import com.luizcarlos.omegatechapi.service.TicketService;
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

public class TicketController {

    @Autowired
    private final TicketService ticketService;

    @PostMapping("/criar")
    public ResponseEntity<Void> criarTicket(
            @RequestBody MensagemRequest request,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        ticketService.criarTicket(request.getMensagem(),usuarioAutenticado);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/meus")
    public ResponseEntity<List<TicketResponseDTO>> getMeusTickets(
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        Long clienteId = usuarioAutenticado.getId();
        List<TicketResponseDTO> tickets = ticketService.findTicketsByUserIdAndStatus(clienteId, status);

        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ROLE_TECNICO')")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateDTO request,
            @AuthenticationPrincipal Usuario tecnico) {

        ticketService.atualizarStatus(id, request.getNovoStatus(), tecnico);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketResponseDTO> getTicketById(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        TicketResponseDTO ticket = ticketService.buscarTicketPorId(id, usuarioAutenticado.getId(), usuarioAutenticado.getPerfil());

        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/resposta/{id}")
    @PreAuthorize("hasRole('ROLE_TECNICO')")
    public ResponseEntity<Void> responderTicket(
            @PathVariable Long id,
            @RequestBody RespostaTicketDTO request,
            @AuthenticationPrincipal Usuario tecnico) {

        ticketService.responderTicket(id, request.getResposta(), tecnico);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    public ResponseEntity<Void> excluirTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario cliente) {

        ticketService.excluirTicket(id, cliente.getId());

        return ResponseEntity.noContent().build();
    }
}
