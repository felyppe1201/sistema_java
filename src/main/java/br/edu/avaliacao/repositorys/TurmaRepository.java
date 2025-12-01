package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Turma;
import jakarta.persistence.*;
import java.util.List;

public class TurmaRepository {
    private EntityManager em;

    public TurmaRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Turma obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Turma findById(long id) {
        return em.find(Turma.class, id);
    }

    public List<Turma> findAll() {
        return em.createQuery("SELECT t FROM Turma t", Turma.class).getResultList();
    }

    public List<Turma> findTurmasByProfessorId(Long professorId) {
    try {
        TypedQuery<Turma> query = em.createQuery(
            "SELECT ap.turma FROM AtribuicaoProfessor ap " +
            "WHERE ap.professor.id = :professorId AND ap.turma.ativo = true " +
            "ORDER BY ap.turma.periodo, ap.turma.disciplina.nome",
            Turma.class
        );
        query.setParameter("professorId", professorId);
        return query.getResultList();
    } catch (Exception e) {
        System.err.println("Erro ao buscar turmas do professor: " + e.getMessage());
        return List.of(); // Retorna lista vazia em caso de erro
    }
}

    public void update(Turma obj) {
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
            Turma obj = em.find(Turma.class, id);
            if (obj != null)
                em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }
    }
    
    public boolean professorLecionaTurma(Long professorId, Long turmaId) {
        try {
            Long count = em.createQuery(
                "SELECT COUNT(ap) FROM AtribuicaoProfessor ap " +
                "WHERE ap.professor.id = :pid AND ap.turma.id = :tid", Long.class
            )
            .setParameter("pid", professorId)
            .setParameter("tid", turmaId)
            .getSingleResult();

            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Erro em professorLecionaTurma: " + e.getMessage());
            return false;
        }
    }
}