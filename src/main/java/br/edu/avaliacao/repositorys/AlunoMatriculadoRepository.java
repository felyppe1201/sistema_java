package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.AlunoMatriculado;
import jakarta.persistence.*;
import java.util.List;

public class AlunoMatriculadoRepository {
    private EntityManager em;

    public AlunoMatriculadoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(AlunoMatriculado obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e; // Repassa o erro para quem chamou tratar
        }
    }

    public AlunoMatriculado findById(long id) {
        return em.find(AlunoMatriculado.class, id);
    }

    public AlunoMatriculado findByUsuarioId(Long usuarioId) {
        String jpql = """
                    SELECT a
                    FROM AlunoMatriculado a
                    JOIN FETCH a.curso
                    WHERE a.usuario.id = :usuarioId
                      AND a.ativo = TRUE
                """;

        TypedQuery<AlunoMatriculado> q = em.createQuery(jpql, AlunoMatriculado.class);
        q.setParameter("usuarioId", usuarioId);

        return q.getResultStream().findFirst().orElse(null);
    }
    
    public List<AlunoMatriculado> findAll() {
        return em.createQuery("SELECT a FROM AlunoMatriculado a", AlunoMatriculado.class).getResultList();
    }

    public void update(AlunoMatriculado obj) {
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void delete(long id) {
        try {
            em.getTransaction().begin();
            AlunoMatriculado obj = em.find(AlunoMatriculado.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
}