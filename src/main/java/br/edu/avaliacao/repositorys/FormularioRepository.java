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
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}