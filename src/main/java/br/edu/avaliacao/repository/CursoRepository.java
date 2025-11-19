package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Curso;
import jakarta.persistence.*;
import java.util.List;

public class CursoRepository {
    private EntityManager em;

    public CursoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Curso obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Curso findById(long id) {
        return em.find(Curso.class, id);
    }

    public List<Curso> findAll() {
        return em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
    }

    public void update(Curso obj) {
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
            Curso obj = em.find(Curso.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}