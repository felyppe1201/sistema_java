package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Formulario;
import jakarta.persistence.*;
import java.util.List;

public class FormularioRepository {
    private EntityManager em;

    public FormularioRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Formulario obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Formulario findById(long id) {
        return em.find(Formulario.class, id);
    }

    public List<Formulario> findAll() {
        return em.createQuery("SELECT f FROM Formulario f", Formulario.class).getResultList();
    }

    public void update(Formulario obj) {
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
            Formulario obj = em.find(Formulario.class, id);
            if (obj != null)
                em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }
    }
    
    public List<Formulario> findAtivosByProcessoId(Long processoId) {
        try {
            TypedQuery<Formulario> q = em.createQuery(
                "SELECT f FROM Formulario f WHERE f.idProcesso = :procId AND f.ativo = true ORDER BY f.id DESC",
                Formulario.class
            );
            q.setParameter("procId", processoId);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar formulários ativos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Soft delete: marca ativo = false e stat = 0
     */
    public void softDelete(Long id) {
        try {
            em.getTransaction().begin();
            Formulario f = em.find(Formulario.class, id);
            if (f != null) {
                f.setAtivo(false);
                f.setStat(0);
                em.merge(f);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new RuntimeException("Erro ao softDelete Formulario: " + e.getMessage(), e);
        }
    }
    
    
    public List<Formulario> findByProcessoId(Long processoId) {
        return em.createQuery(
                "SELECT f FROM Formulario f WHERE f.idProcesso = :pid AND f.ativo = true ORDER BY f.id DESC",
                Formulario.class)
                .setParameter("pid", processoId)
                .getResultList();
    }
    
    public Long findProcessoIdByFormularioId(Long formularioId) {
    try {
        return em.createQuery(
                "SELECT f.idProcesso FROM Formulario f WHERE f.id = :fid",
                Long.class
        ).setParameter("fid", formularioId)
         .getSingleResult();
    } catch (Exception e) {
        return null;
    }
}



}