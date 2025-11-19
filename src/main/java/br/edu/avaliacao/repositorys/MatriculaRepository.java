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
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }
    }
    
    public List<Object[]> findTurmasDetalhadasByAlunoId(Long alunoId) {

        String jpql = """
            SELECT t, d, c
            FROM Matricula m
            JOIN m.turma t
            JOIN t.disciplina d
            JOIN d.curso c
            WHERE m.aluno.id = :alunoId
              AND m.ativo = TRUE
        """;

        return em.createQuery(jpql, Object[].class)
                 .setParameter("alunoId", alunoId)
                 .getResultList();
    }


    public Matricula findById(long id) {
        return em.find(Matricula.class, id);
    }

    public List<Matricula> findAll() {
        return em.createQuery("SELECT m FROM Matricula m", Matricula.class).getResultList();
    }

    public void update(Matricula obj) {
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
            Matricula obj = em.find(Matricula.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}