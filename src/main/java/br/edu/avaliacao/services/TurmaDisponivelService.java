package br.edu.avaliacao.services;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class TurmaDisponivelService {

    public static class TurmaDisponivelDTO {
        private Long idTurma;
        private String nomeDisciplina;
        private String codigoTurma;
        private int vagasRestantes;

        public TurmaDisponivelDTO(Long idTurma, String nomeDisciplina, String codigoTurma, int vagasRestantes) {
            this.idTurma = idTurma;
            this.nomeDisciplina = nomeDisciplina;
            this.codigoTurma = codigoTurma;
            this.vagasRestantes = vagasRestantes;
        }

        public Long getIdTurma() { return idTurma; }
        public String getNomeDisciplina() { return nomeDisciplina; }
        public String getCodigoTurma() { return codigoTurma; }
        public int getVagasRestantes() { return vagasRestantes; }
    }

    public List<TurmaDisponivelDTO> listarTurmasDisponiveis(Long alunoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<AlunoMatriculado> queryAluno = em.createQuery(
                "SELECT am FROM AlunoMatriculado am WHERE am.idUsuario = :id AND am.ativo = true",
                AlunoMatriculado.class
            );
            queryAluno.setParameter("id", alunoId);
            AlunoMatriculado matricula = queryAluno.getResultStream().findFirst().orElse(null);

            if (matricula == null) return new ArrayList<>();

            Long cursoId = matricula.getCursoId();
            int periodoAluno = matricula.getPeriodo();

            TypedQuery<Disciplina> queryDisc = em.createQuery(
                "SELECT d FROM Disciplina d WHERE d.curso.id = :cursoId AND d.periodo = :periodo AND d.ativo = true",
                Disciplina.class
            );
            queryDisc.setParameter("cursoId", cursoId);
            queryDisc.setParameter("periodo", periodoAluno);
            List<Disciplina> disciplinas = queryDisc.getResultList();

            List<TurmaDisponivelDTO> resultado = new ArrayList<>();

            for (Disciplina d : disciplinas) {
                TypedQuery<Turma> queryTurma = em.createQuery(
                    "SELECT t FROM Turma t WHERE t.disciplina.id = :disciplinaId AND t.ativo = true",
                    Turma.class
                );
                queryTurma.setParameter("disciplinaId", d.getId());
                List<Turma> turmas = queryTurma.getResultList();

                for (Turma t : turmas) {
                    TypedQuery<Long> queryAlunoMatriculado = em.createQuery(
                        "SELECT COUNT(m) FROM Matricula m WHERE m.turma.id = :turmaId AND m.aluno.id = :alunoId AND m.ativo = true",
                        Long.class
                    );
                    queryAlunoMatriculado.setParameter("turmaId", t.getId());
                    queryAlunoMatriculado.setParameter("alunoId", alunoId);
                    Long jaMatriculado = queryAlunoMatriculado.getSingleResult();
                    if (jaMatriculado > 0) continue; 

                    TypedQuery<Long> queryMatriculas = em.createQuery(
                        "SELECT COUNT(m) FROM Matricula m WHERE m.turma.id = :turmaId AND m.ativo = true",
                        Long.class
                    );
                    queryMatriculas.setParameter("turmaId", t.getId());
                    Long totalMatriculas = queryMatriculas.getSingleResult();

                    int vagasRestantes = t.getNumeroVagas() - totalMatriculas.intValue();
                    if (vagasRestantes > 0) {
                        resultado.add(new TurmaDisponivelDTO(
                            t.getId(),
                            d.getNome(),
                            t.getCodigoTurma(),
                            vagasRestantes
                        ));
                    }
                }
            }

            return resultado;

        } finally {
            em.close();
        }
    }
}
