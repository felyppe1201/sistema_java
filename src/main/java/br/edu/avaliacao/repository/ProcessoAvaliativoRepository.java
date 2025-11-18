package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.ProcessoAvaliativo;
import jakarta.persistence.*;
import java.util.List;

public class ProcessoAvaliativoRepository {
    private EntityManager em;

    public ProcessoAvaliativoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(ProcessoAvaliativo obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public ProcessoAvaliativo findById(long id) {
        return em.find(ProcessoAvaliativo.class, id);
    }

    public List<ProcessoAvaliativo> findAll() {
        return em.createQuery("SELECT p FROM ProcessoAvaliativo p", ProcessoAvaliativo.class).getResultList();
    }

    public void update(ProcessoAvaliativo obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        ProcessoAvaliativo obj = em.find(ProcessoAvaliativo.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }
}
