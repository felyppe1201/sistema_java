package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Usuario;
import br.edu.avaliacao.security.Crypt; // NOVO: Importa a classe de segurança
import jakarta.persistence.*;
import java.util.List;

public class UsuarioRepository {
    private EntityManager em;

    public UsuarioRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Usuario obj) {
        try {
            // APLICA O HASH NA SENHA ANTES DE SALVAR NO BANCO
            if (obj.getSenha() != null && !obj.getSenha().startsWith("$2a$")) {
                obj.setSenha(Crypt.hashPassword(obj.getSenha()));
            }

            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Usuario findById(long id) {
        return em.find(Usuario.class, id);
    }

    // Mantido, busca usuários ativos
    public Usuario findByEmail(String email) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email AND u.ativo = true", Usuario.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public List<Usuario> findProfessoresAtivos() {
        TypedQuery<Usuario> q = em.createQuery(
            "SELECT u FROM Usuario u WHERE TRIM(LOWER(u.cargo)) = :cargo AND u.ativo = true",
            Usuario.class);
        q.setParameter("cargo", "prof");
        return q.getResultList();
    }


    public void update(Usuario obj) {
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public void delete(long id) {
        try {
            em.getTransaction().begin();
            Usuario obj = em.find(Usuario.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * NOVO: Autentica o usuário comparando a senha em texto simples com o hash armazenado.
     */
    public boolean authenticate(String email, String senha) {
        Usuario usuario = findByEmail(email); // Busca o usuário ativo apenas pelo email

        if (usuario != null) {
            // Verifica a senha em texto simples contra o hash do banco de dados
            return Crypt.checkPassword(senha, usuario.getSenha());
        }

        return false;
    }

    // =================================================================
    // MÉTODOS DE ATUALIZAÇÃO DA PÁGINA CONTA (REVERTIDO PARA NOMES ANTIGOS E ID LONG)
    // =================================================================

    /**
     * Tenta atualizar o email de um usuário, exigindo a senha atual para confirmação.
     * REVERTIDO: Nome do método para 'updateUserEmail'.
     * REVERTIDO: Tipo do parâmetro idUsuario para Long.
     * @return true se a senha estiver correta e a atualização for feita, false caso contrário.
     */
    public boolean updateUserEmail(Long idUsuario, String novoEmail, String senhaAtual) {
        if (idUsuario == null) return false;
        
        try {
            em.getTransaction().begin();
            // Busca usando o ID Long diretamente
            Usuario usuario = em.find(Usuario.class, idUsuario); 
            
            // 1. Verifica se o usuário existe e se a senha atual está correta
            if (usuario != null && Crypt.checkPassword(senhaAtual, usuario.getSenha())) {
                
                // 2. Verifica se o novo email já está em uso por outro usuário ativo
                Usuario existingUser = findByEmail(novoEmail);
                // Compara o ID Long
                if (existingUser != null && existingUser.getId() != idUsuario) {
                    em.getTransaction().rollback();
                    return false; // Email já em uso
                }
                
                // 3. Aplica a mudança e salva
                usuario.setEmail(novoEmail);
                em.merge(usuario);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false; // Senha incorreta ou usuário não encontrado
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Erro ao atualizar email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tenta atualizar a senha de um usuário, exigindo a senha antiga para confirmação.
     * REVERTIDO: Nome do método para 'updateUserPassword'.
     * REVERTIDO: Tipo do parâmetro idUsuario para Long.
     * @return true se a senha antiga estiver correta e a nova senha for salva, false caso contrário.
     */
    public boolean updateUserPassword(Long idUsuario, String senhaAntiga, String novaSenha) {
        if (idUsuario == null) return false;
        
        try {
            em.getTransaction().begin();
             // Busca usando o ID Long diretamente
            Usuario usuario = em.find(Usuario.class, idUsuario);
            
            // 1. Verifica se o usuário existe e se a senha antiga está correta
            if (usuario != null && Crypt.checkPassword(senhaAntiga, usuario.getSenha())) {
                
                // 2. Aplica o hash na nova senha antes de salvar
                usuario.setSenha(Crypt.hashPassword(novaSenha));
                em.merge(usuario);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false; // Senha antiga incorreta ou usuário não encontrado
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Erro ao atualizar senha: " + e.getMessage());
            return false;
        }
    }


    // =================================================================
    // CÓDIGO CONTA DTO E BUSCAR DADOS (AJUSTADO O TIPO DO ID)
    // =================================================================

    public static class ContaDTO {
        private String cursoNome;
        private Integer periodoAtual;

        public ContaDTO(String cursoNome, Integer periodoAtual) {
            this.cursoNome = cursoNome;
            this.periodoAtual = periodoAtual;
        }

        public String getCursoNome() {
            return cursoNome;
        }

        public Integer getPeriodoAtual() {
            return periodoAtual;
        }
    }
    
    /**
     * Busca o nome do curso e o período atual do aluno.
     * REVERTIDO: Tipo do parâmetro idUsuario para Long.
     */
    public ContaDTO buscarDadosAcademicosDoAluno(Long idUsuario) {
        if (idUsuario == null) {
             return new ContaDTO("ID Inválido", 0);
        }
        
        String cursoNome = "N/A";
        
        // 1. Busca o nome do curso
        try {
            String jpqlCurso = "SELECT c.nome FROM AlunoMatriculado am JOIN am.curso c WHERE am.usuario.id = :idUsuario";
            TypedQuery<String> queryCurso = em.createQuery(jpqlCurso, String.class);
            queryCurso.setParameter("idUsuario", idUsuario);
            cursoNome = queryCurso.getSingleResult();
        } catch (NoResultException e) {
            cursoNome = "Nenhum Curso Ativo";
        } catch (Exception e) {
            System.err.println("Erro ao buscar nome do curso: " + e.getMessage());
             cursoNome = "Erro na Busca do Curso";
        }

        Integer periodoAtual = 1; 
        
        // 2. Busca o período atual (turma mais avançada)
        try {
            String jpqlPeriodo = "SELECT MAX(t.periodo) FROM Turma t JOIN Matricula m ON t.id = m.turma.id WHERE m.aluno.id = :idUsuario AND m.ativo = TRUE";
            TypedQuery<Integer> queryPeriodo = em.createQuery(jpqlPeriodo, Integer.class);
            queryPeriodo.setParameter("idUsuario", idUsuario);
            
            Integer maxPeriodo = queryPeriodo.getSingleResult();
            
            if (maxPeriodo != null) {
                periodoAtual = maxPeriodo;
            }

        } catch (NoResultException e) {
            // Se não houver matrícula, mantém 1
        } catch (Exception e) {
            System.err.println("Erro ao buscar período: " + e.getMessage());
        }

        return new ContaDTO(cursoNome, periodoAtual);
    }
}