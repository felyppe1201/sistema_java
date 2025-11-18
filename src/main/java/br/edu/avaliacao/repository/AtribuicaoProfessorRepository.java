package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.AtribuicaoProfessor;
import jakarta.persistence.*;
import java.util.List;

public class AtribuicaoProfessorRepository {
    private EntityManager em;

    public AtribuicaoProfessorRepository(EntityManager em) {
        this.em = em;
    }

    public void save(AtribuicaoProfessor obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public AtribuicaoProfessor findById(long id) {
        return em.find(AtribuicaoProfessor.class, id);
    }

    public List<AtribuicaoProfessor> findAll() {
        return em.createQuery("SELECT a FROM AtribuicaoProfessor a", AtribuicaoProfessor.class).getResultList();
    }

    public void update(AtribuicaoProfessor obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        AtribuicaoProfessor obj = em.find(AtribuicaoProfessor.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }
}
