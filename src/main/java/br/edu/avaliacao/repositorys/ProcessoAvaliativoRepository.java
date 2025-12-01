package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.ProcessoAvaliativo;
import jakarta.persistence.*;
import java.util.List;

/**
 * Repositório para operações CRUD e consultas específicas na entidade ProcessoAvaliativo.
 */
public class ProcessoAvaliativoRepository {
    private EntityManager em;

    public ProcessoAvaliativoRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * Busca todos os processos avaliativos ativos associados a uma turma específica.
     * A consulta está ajustada para usar 'ativo' e ordenar por 'periodo' e 'nome'.
     * @param turmaId O ID da turma.
     * @return Lista de ProcessosAvaliativos ativos.
     */
    public List<ProcessoAvaliativo> findByTurmaId(Long turmaId) {
        try {
            TypedQuery<ProcessoAvaliativo> query = em.createQuery(
                "SELECT pa FROM ProcessoAvaliativo pa WHERE pa.turma.id = :turmaId AND pa.ativo = true ORDER BY pa.periodo DESC, pa.nome ASC",
                ProcessoAvaliativo.class
            );
            query.setParameter("turmaId", turmaId);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar processos avaliativos da turma " + turmaId + ": " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }
    
    // --- Métodos CRUD ---

    /**
     * Salva uma nova instância de ProcessoAvaliativo no banco de dados.
     * @param obj O objeto ProcessoAvaliativo a ser persistido.
     */
    public void save(ProcessoAvaliativo obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar ProcessoAvaliativo: " + e.getMessage(), e);
        }
    }
    
    public String findNomeById(Long id) {
        try {
            return em.createQuery(
                    "SELECT p.nome FROM ProcessoAvaliativo p WHERE p.id = :id",
                    String.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public Long findTurmaById(Long id) {
            try {
                return em.createQuery(
                    "SELECT p.turma.id FROM ProcessoAvaliativo p WHERE p.id = :id",
                    Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
     }




    /**
     * Busca um ProcessoAvaliativo pelo seu ID.
     * @param id O ID do processo avaliativo.
     * @return O ProcessoAvaliativo encontrado ou null se não existir.
     */
    public ProcessoAvaliativo findById(long id) {
        return em.find(ProcessoAvaliativo.class, id);
    }

    /**
     * Retorna todos os Processos Avaliativos.
     * @return Lista de todos os ProcessosAvaliativos.
     */
    public List<ProcessoAvaliativo> findAll() {
        return em.createQuery("SELECT p FROM ProcessoAvaliativo p", ProcessoAvaliativo.class).getResultList();
    }

    /**
     * Atualiza os dados de um ProcessoAvaliativo existente.
     * @param obj O objeto ProcessoAvaliativo com os dados atualizados.
     */
    public void update(ProcessoAvaliativo obj) {
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar ProcessoAvaliativo: " + e.getMessage(), e);
        }
    }

    /**
     * Remove um ProcessoAvaliativo pelo seu ID.
     * @param id O ID do processo avaliativo a ser excluído.
     */
    public void delete(long id) {
        try {
            em.getTransaction().begin();
            ProcessoAvaliativo obj = em.find(ProcessoAvaliativo.class, id);
            if (obj != null) {
                em.remove(obj);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new RuntimeException("Erro ao deletar ProcessoAvaliativo: " + e.getMessage(), e);
        }
    }
    
    public List<ProcessoAvaliativo> findAtivosByTurmaId(Long turmaId) {
        try {
            return em.createQuery(
                    "SELECT pa FROM ProcessoAvaliativo pa " +
                            "WHERE pa.turma.id = :turmaId AND pa.ativo = true " +
                            "ORDER BY pa.periodo DESC, pa.nome ASC",
                    ProcessoAvaliativo.class)
                    .setParameter("turmaId", turmaId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar processos ativos: " + e.getMessage());
            return List.of();
        }
    }
    
    public void softDelete(Long id) {
        try {
            em.getTransaction().begin();
            ProcessoAvaliativo pa = em.find(ProcessoAvaliativo.class, id);

            if (pa != null) {
                pa.setAtivo(false);
                pa.setStat(0);
                em.merge(pa);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro no softDelete: " + e.getMessage(), e);
        }
    }


}