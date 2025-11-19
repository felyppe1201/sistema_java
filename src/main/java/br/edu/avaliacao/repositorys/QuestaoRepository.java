package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Questao;
import jakarta.persistence.*;
import java.util.List;

public class QuestaoRepository {
    private EntityManager em;

    public QuestaoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Questao obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Questao findById(long id) {
        return em.find(Questao.class, id);
    }

    public List<Questao> findAll() {
        return em.createQuery("SELECT q FROM Questao q", Questao.class).getResultList();
    }

    public void update(Questao obj) {
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
            Questao obj = em.find(Questao.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}