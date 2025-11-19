package br.edu.avaliacao.security;

import br.edu.avaliacao.models.Usuario;
import br.edu.avaliacao.repositorys.UsuarioRepository;
import jakarta.persistence.EntityManager;

public class AuthService {

    private final UsuarioRepository repo;

    public AuthService(EntityManager em) {
        this.repo = new UsuarioRepository(em);
    }

    public Usuario autenticar(String email, String senha) {
        Usuario u = repo.findByEmail(email);

        if (u == null) return null;

        if (!u.getSenha().equals(senha)) return null;

        return u;
    }
}
