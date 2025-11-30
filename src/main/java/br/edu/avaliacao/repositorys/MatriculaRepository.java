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
    
    /**
     * Busca detalhes das turmas em que o aluno está matriculado.
     * Retorna [Turma, Disciplina, Curso].
     */
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

    /**
     * NOVO: Conta o número de matrículas ativas em uma turma. Usado para checar vagas.
     * @param turmaId ID da turma.
     * @return O número total de alunos matriculados ativos.
     */
    public long countActiveEnrollmentsByTurmaId(Long turmaId) {
        try {
            String jpql = "SELECT COUNT(m) FROM Matricula m WHERE m.idTurma = :turmaId AND m.ativo = true";
            Long result = em.createQuery(jpql, Long.class)
                            .setParameter("turmaId", turmaId)
                            .getSingleResult();
            return result != null ? result : 0;
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            System.err.println("Erro ao contar matrículas ativas: " + e.getMessage());
            return -1; // Sinaliza erro
        }
    }

    /**
     * NOVO: Verifica se o aluno já está matriculado em uma turma específica e ativa.
     */
    public boolean isStudentEnrolled(Long alunoId, Long turmaId) {
        try {
            String jpql = "SELECT COUNT(m) FROM Matricula m WHERE m.idAluno = :alunoId AND m.idTurma = :turmaId AND m.ativo = true";
            Long count = em.createQuery(jpql, Long.class)
                           .setParameter("alunoId", alunoId)
                           .setParameter("turmaId", turmaId)
                           .getSingleResult();
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Erro ao verificar matrícula: " + e.getMessage());
            return false;
        }
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