package com.example.login.controllers;

import com.example.login.dtos.LoginRequestDTO;
import com.example.login.dtos.LoginResponseDTO;
import com.example.login.models.Usuario;
import com.example.login.services.UsuarioService;
import com.example.login.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        usuarioService.criarUsuario(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioService.buscarPorNomeUsuario(loginRequest.getNomeUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = JwtUtil.gerarToken(usuario.getNomeUsuario());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
