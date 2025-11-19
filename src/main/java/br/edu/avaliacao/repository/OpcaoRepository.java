package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Opcao;
import jakarta.persistence.*;
import java.util.List;

public class OpcaoRepository {
    private EntityManager em;

    public OpcaoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Opcao obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Opcao findById(long id) {
        return em.find(Opcao.class, id);
    }

    public List<Opcao> findAll() {
        return em.createQuery("SELECT o FROM Opcao o", Opcao.class).getResultList();
    }

    public void update(Opcao obj) {
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
            Opcao obj = em.find(Opcao.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}