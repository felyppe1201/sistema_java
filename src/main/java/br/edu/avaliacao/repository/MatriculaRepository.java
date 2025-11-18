package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Matricula;
import jakarta.persistence.*;
import java.util.List;

public class MatriculaRepository {
    private EntityManager em;

    public MatriculaRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Matricula obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public Matricula findById(long id) {
        return em.find(Matricula.class, id);
    }

    public List<Matricula> findAll() {
        return em.createQuery("SELECT m FROM Matricula m", Matricula.class).getResultList();
    }

    public void update(Matricula obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        Matricula obj = em.find(Matricula.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }
}
