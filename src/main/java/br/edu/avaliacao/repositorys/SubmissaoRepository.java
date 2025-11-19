package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Submissao;
import jakarta.persistence.*;
import java.util.List;

public class SubmissaoRepository {
    private EntityManager em;

    public SubmissaoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Submissao obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Submissao findById(long id) {
        return em.find(Submissao.class, id);
    }

    public List<Submissao> findAll() {
        return em.createQuery("SELECT s FROM Submissao s", Submissao.class).getResultList();
    }

    public void update(Submissao obj) {
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
            Submissao obj = em.find(Submissao.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}