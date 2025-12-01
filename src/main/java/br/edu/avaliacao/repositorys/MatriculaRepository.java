package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Matricula;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MatriculaRepository {
    private final EntityManager em;

    public MatriculaRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Matricula obj) {
        em.persist(obj);
    }

    public Matricula findById(long id) {
        return em.find(Matricula.class, id);
    }

    public List<Matricula> findAll() {
        return em.createQuery("SELECT m FROM Matricula m", Matricula.class).getResultList();
    }

    public void update(Matricula obj) {
        em.merge(obj);
    }

    public void delete(long id) {
        Matricula obj = em.find(Matricula.class, id);
        if (obj != null) em.remove(obj);
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


    public Matricula createMatricula(Long alunoId, Long turmaId) {
        Matricula m = new Matricula();
        m.setIdAluno(alunoId);
        m.setIdTurma(turmaId);
        m.setAtivo(true); 
        m.setStat(1);     
        return m;
    }
}
