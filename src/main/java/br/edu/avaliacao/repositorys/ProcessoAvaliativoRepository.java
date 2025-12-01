package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.ProcessoAvaliativo;
import jakarta.persistence.*;
import java.util.List;

public class ProcessoAvaliativoRepository {
    private EntityManager em;

    public ProcessoAvaliativoRepository(EntityManager em) {
        this.em = em;
    }

    public List<ProcessoAvaliativo> findByTurmaId(Long turmaId) {
        try {
            TypedQuery<ProcessoAvaliativo> query = em.createQuery(
                "SELECT pa FROM ProcessoAvaliativo pa WHERE pa.turma.id = :turmaId AND pa.ativo = true ORDER BY pa.periodo DESC, pa.nome ASC",
                ProcessoAvaliativo.class
            );
            query.setParameter("turmaId", turmaId);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar processos avaliativos da turma " + turmaId + ": " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }
    
    public void save(ProcessoAvaliativo obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar ProcessoAvaliativo: " + e.getMessage(), e);
        }
    }
    
    public String findNomeById(Long id) {
        try {
            return em.createQuery(
                    "SELECT p.nome FROM ProcessoAvaliativo p WHERE p.id = :id",
                    String.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public Long findTurmaById(Long id) {
            try {
                return em.createQuery(
                    "SELECT p.turma.id FROM ProcessoAvaliativo p WHERE p.id = :id",
                    Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
     }

    public ProcessoAvaliativo findById(long id) {
        return em.find(ProcessoAvaliativo.class, id);
    }

    public List<ProcessoAvaliativo> findAll() {
        return em.createQuery("SELECT p FROM ProcessoAvaliativo p", ProcessoAvaliativo.class).getResultList();
    }

    public void update(ProcessoAvaliativo obj) {
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar ProcessoAvaliativo: " + e.getMessage(), e);
        }
    }

    public void delete(long id) {
        try {
            em.getTransaction().begin();
            ProcessoAvaliativo obj = em.find(ProcessoAvaliativo.class, id);
            if (obj != null) {
                em.remove(obj);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new RuntimeException("Erro ao deletar ProcessoAvaliativo: " + e.getMessage(), e);
        }
    }
    
    public List<ProcessoAvaliativo> findAtivosByTurmaId(Long turmaId) {
        try {
            return em.createQuery(
                    "SELECT pa FROM ProcessoAvaliativo pa " +
                            "WHERE pa.turma.id = :turmaId AND pa.ativo = true " +
                            "ORDER BY pa.periodo DESC, pa.nome ASC",
                    ProcessoAvaliativo.class)
                    .setParameter("turmaId", turmaId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar processos ativos: " + e.getMessage());
            return List.of();
        }
    }
    
    public void softDelete(Long id) {
        try {
            em.getTransaction().begin();
            ProcessoAvaliativo pa = em.find(ProcessoAvaliativo.class, id);

            if (pa != null) {
                pa.setAtivo(false);
                pa.setStat(0);
                em.merge(pa);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro no softDelete: " + e.getMessage(), e);
        }
    }


}