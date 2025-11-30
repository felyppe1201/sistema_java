package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Questao;
import jakarta.persistence.*;
import java.util.List;

public class QuestaoRepository {
    private EntityManager em;

    public QuestaoRepository(EntityManager em) {
        this.em = em;
    }


    public void save(Questao q) {
        em.persist(q);
    }


    public Questao findById(long id) {
        return em.find(Questao.class, id);
    }

    public List<Questao> findAll() {
        return em.createQuery("SELECT q FROM Questao q", Questao.class).getResultList();
    }

    public void update(Questao q) {
        em.merge(q);
    }


    public void delete(long id) {
        Questao obj = em.find(Questao.class, id);
        if (obj != null) {
            em.remove(em.contains(obj) ? obj : em.merge(obj));
        }
    }

    
    public List<Questao> findByFormularioId(Long formularioId) {
        try {
            TypedQuery<Questao> q = em.createQuery(
                "SELECT q FROM Questao q WHERE q.idFormulario = :fid ORDER BY q.id", Questao.class);
            q.setParameter("fid", formularioId);
            return q.getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

}