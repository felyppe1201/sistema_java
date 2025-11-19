package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Usuario;
import jakarta.persistence.EntityManager;

/**
 * MOCK TEMPORÁRIO — remover assim que o outro aluno completar os repositories.
 */
public class UsuarioRepository {

    private EntityManager em;

    public UsuarioRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * Mock: retorna um usuário fixo APENAS para fins de teste.
     * Futuramente será substituído pelo findByEmail real via JPA.
     */
    public Usuario findByEmail(String email) {

        // Evita NullPointerException se email for null
        if (email == null) {
            return null;
        }

        // Criando um usuário falso apenas para permitir login
        if (email.equalsIgnoreCase("admin@teste.com")) {
            Usuario u = new Usuario();
            u.setId(1L);
            u.setNome("Administrador do Sistema");
            u.setEmail("admin@teste.com");
            u.setSenha("123"); // Apenas mock — será removido quando integrarmos o repositório
            u.setCargo("adm");
            u.setAtivo(true);
            u.setStat(1);
            return u;
        }

        // Retorno padrão quando usuário não existe (corrige erro de compilação)
        return null;
    }
}
