package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Opcao;
import jakarta.persistence.*;
import java.util.List;

public class OpcaoRepository {
    private final EntityManager em;

    public OpcaoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Opcao obj) {
        em.persist(obj);
    }

    public Opcao findById(long id) {
        return em.find(Opcao.class, id);
    }

    public List<Opcao> findAll() {
        return em.createQuery("SELECT o FROM Opcao o", Opcao.class).getResultList();
    }

    public void update(Opcao obj) {
        em.merge(obj);
    }

    public void delete(Opcao obj) {
        if (obj != null) {
            em.remove(em.contains(obj) ? obj : em.merge(obj));
        }
    }
    
    public List<Opcao> findByQuestaoId(Long questaoId) {
        TypedQuery<Opcao> q = em.createQuery(
            "SELECT o FROM Opcao o WHERE o.idQuestao = :qid ORDER BY o.id", Opcao.class);
        q.setParameter("qid", questaoId);
        return q.getResultList();
    }

}
