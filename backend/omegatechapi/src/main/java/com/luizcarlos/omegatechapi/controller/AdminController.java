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
import com.luizcarlos.omegatechapi.model.dto.TecnicoResponseDTO;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.service.TecnicoService;
import com.luizcarlos.omegatechapi.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private TecnicoService tecnicoService;

    private final TicketService ticketService;

    @Autowired
    public AdminController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrarTecnico(@RequestBody Usuario novoTecnico) {

        tecnicoService.criarTecnico(novoTecnico);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/tecnicos")
    public ResponseEntity<List<TecnicoResponseDTO>> buscarTodos() {

        List<TecnicoResponseDTO> tecnicos = tecnicoService.buscarTodosTecnicos();
        return new ResponseEntity<>(tecnicos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> buscarPorId(@PathVariable("id") Long id) {

            TecnicoResponseDTO tecnico = tecnicoService.buscarTecnicoPorId(id);
            return ResponseEntity.ok(tecnico);

    }
    @GetMapping("/respondidos/{tecnicoId}")
    public ResponseEntity<List<TicketResponseDTO>> buscarTicketsRespondidosPorTecnico(
            @PathVariable("tecnicoId") Long tecnicoId)
    {
        List<TicketResponseDTO> tickets = ticketService.buscarTicketsRespondidos(tecnicoId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
