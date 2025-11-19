package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Resposta;
import jakarta.persistence.*;
import java.util.List;

public class RespostaRepository {
    private EntityManager em;

    public RespostaRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Resposta obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Resposta findById(long id) {
        return em.find(Resposta.class, id);
    }

    public List<Resposta> findAll() {
        return em.createQuery("SELECT r FROM Resposta r", Resposta.class).getResultList();
    }

    public void update(Resposta obj) {
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
            Resposta obj = em.find(Resposta.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}