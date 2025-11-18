package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Disciplina;
import jakarta.persistence.*;
import java.util.List;

public class DisciplinaRepository {
    private EntityManager em;

    public DisciplinaRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Disciplina obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public Disciplina findById(long id) {
        return em.find(Disciplina.class, id);
    }

    public List<Disciplina> findAll() {
        return em.createQuery("SELECT d FROM Disciplina d", Disciplina.class).getResultList();
    }

    public void update(Disciplina obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        Disciplina obj = em.find(Disciplina.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }
}
