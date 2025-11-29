package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Submissao;
import jakarta.persistence.*;
import java.util.Collections; 
import java.util.List;
import java.util.Set; 
import java.util.stream.Collectors;


public class SubmissaoRepository {
    private EntityManager em;

    public SubmissaoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Submissao obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Submissao findById(long id) {
        return em.find(Submissao.class, id);
    }

    public List<Submissao> findAll() {
        return em.createQuery("SELECT s FROM Submissao s", Submissao.class).getResultList();
    }

    public void update(Submissao obj) {
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
            Submissao obj = em.find(Submissao.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
    public Set<Long> findFormularioIdsSubmetidos(long alunoId, long turmaId) {
        try {
            // JPQL: Seleciona o ID do Formulário a partir de todas as Submissões
            // feitas pelo aluno na turma especificada.
            String jpql = "SELECT s.formulario.id FROM Submissao s WHERE s.usuario.id = :alunoId AND s.turma.id = :turmaId";

            @SuppressWarnings("unchecked")
            List<Long> results = em.createQuery(jpql)
                                   .setParameter("alunoId", alunoId)
                                   .setParameter("turmaId", turmaId)
                                   .getResultList();

            // Converte a lista para um Set para pesquisa O(1) no Servlet
            return results.stream().collect(Collectors.toSet());

        } catch (Exception e) {
            System.err.println("Erro ao buscar IDs de formulários submetidos: " + e.getMessage());
            return Collections.emptySet();
        }
    }
    public boolean existsByAlunoAndFormulario(Long alunoId, long formularioId) {
        try {
            // JPQL que verifica se existe ALGUM registro de Submissao
            // onde idUsuario é igual a alunoId E idFormulario é igual a formularioId.
            // Usamos COUNT para ser otimizado e rápido.
            String jpql = "SELECT COUNT(s) FROM Submissao s WHERE s.idUsuario = :alunoId AND s.idFormulario = :formularioId";

            Long count = em.createQuery(jpql, Long.class)
                             .setParameter("alunoId", alunoId)
                             .setParameter("formularioId", formularioId)
                             .getSingleResult();

            return count > 0;
        } catch (NoResultException e) {
            // Teoricamente, COUNT sempre retorna 0, mas para segurança.
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao verificar submissão: " + e.getMessage());
            return false;
        }
    }
}
