package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.AlunoMatriculado;
import jakarta.persistence.*;
import java.util.List;

public class AlunoMatriculadoRepository {
    private EntityManager em;

    public AlunoMatriculadoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(AlunoMatriculado obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public AlunoMatriculado findById(long id) {
        return em.find(AlunoMatriculado.class, id);
    }

    public List<AlunoMatriculado> findAll() {
        return em.createQuery("SELECT a FROM AlunoMatriculado a", AlunoMatriculado.class).getResultList();
    }

    public void update(AlunoMatriculado obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        AlunoMatriculado obj = em.find(AlunoMatriculado.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }
}
