package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Peso;
import jakarta.persistence.*;
import java.util.List;

public class PesoRepository {
    private EntityManager em;

    public PesoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Peso obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Peso findById(long id) {
        return em.find(Peso.class, id);
    }

    public List<Peso> findAll() {
        return em.createQuery("SELECT p FROM Peso p", Peso.class).getResultList();
    }

    public void update(Peso obj) {
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
            Peso obj = em.find(Peso.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}