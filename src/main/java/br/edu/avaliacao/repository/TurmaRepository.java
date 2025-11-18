package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Turma;
import jakarta.persistence.*;
import java.util.List;

public class TurmaRepository {
    private EntityManager em;

    public TurmaRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Turma obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public Turma findById(long id) {
        return em.find(Turma.class, id);
    }

    public List<Turma> findAll() {
        return em.createQuery("SELECT t FROM Turma t", Turma.class).getResultList();
    }

    public void update(Turma obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        Turma obj = em.find(Turma.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }
}
