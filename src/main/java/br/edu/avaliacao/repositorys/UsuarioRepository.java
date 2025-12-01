package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Usuario;
import br.edu.avaliacao.security.Crypt; 
import jakarta.persistence.*;
import java.util.List;

public class UsuarioRepository {
    private EntityManager em;

    public UsuarioRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Usuario obj) {
        try {
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

    public boolean authenticate(String email, String senha) {
        Usuario usuario = findByEmail(email); 

        if (usuario != null) {
            return Crypt.checkPassword(senha, usuario.getSenha());
        }

        return false;
    }

    public boolean updateUserEmail(Long idUsuario, String novoEmail, String senhaAtual) {
        if (idUsuario == null) return false;
        
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, idUsuario); 
            if (usuario != null && Crypt.checkPassword(senhaAtual, usuario.getSenha())) {
                Usuario existingUser = findByEmail(novoEmail);
                if (existingUser != null && existingUser.getId() != idUsuario) {
                    em.getTransaction().rollback();
                    return false; 
                }
                usuario.setEmail(novoEmail);
                em.merge(usuario);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false; 
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Erro ao atualizar email: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserPassword(Long idUsuario, String senhaAntiga, String novaSenha) {
        if (idUsuario == null) return false;
        
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, idUsuario);
            
            if (usuario != null && Crypt.checkPassword(senhaAntiga, usuario.getSenha())) {
                usuario.setSenha(Crypt.hashPassword(novaSenha));
                em.merge(usuario);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false; 
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Erro ao atualizar senha: " + e.getMessage());
            return false;
        }
    }

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

    public ContaDTO buscarDadosAcademicosDoAluno(Long idUsuario) {
        if (idUsuario == null) {
             return new ContaDTO("ID Inválido", 0);
        }
        
        String cursoNome = "N/A";
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

        try {
            String jpqlPeriodo = "SELECT MAX(t.periodo) FROM Turma t JOIN Matricula m ON t.id = m.turma.id WHERE m.aluno.id = :idUsuario AND m.ativo = TRUE";
            TypedQuery<Integer> queryPeriodo = em.createQuery(jpqlPeriodo, Integer.class);
            queryPeriodo.setParameter("idUsuario", idUsuario);
            
            Integer maxPeriodo = queryPeriodo.getSingleResult();
            
            if (maxPeriodo != null) {
                periodoAtual = maxPeriodo;
            }

        } catch (NoResultException e) {
        } catch (Exception e) {
            System.err.println("Erro ao buscar período: " + e.getMessage());
        }

        return new ContaDTO(cursoNome, periodoAtual);
    }
}