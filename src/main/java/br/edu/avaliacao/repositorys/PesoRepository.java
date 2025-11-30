package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Peso;
import jakarta.persistence.*;
import java.util.List;

public class PesoRepository {
    private final EntityManager em;

    public PesoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Peso obj) {
        em.persist(obj);
    }

    public Peso findById(long id) {
        return em.find(Peso.class, id);
    }

    public List<Peso> findAll() {
        return em.createQuery("SELECT p FROM Peso p", Peso.class).getResultList();
    }

    public void update(Peso obj) {
        em.merge(obj);
    }

    public void delete(long id) {
        Peso obj = em.find(Peso.class, id);
        if (obj != null) {
            em.remove(em.contains(obj) ? obj : em.merge(obj));
        }
    }

    public Double findPesoByQuestaoId(Long questaoId) {
        TypedQuery<Double> q = em.createQuery(
                "SELECT p.peso FROM Peso p WHERE p.idQuestao = :qid ORDER BY p.id",
                Double.class
        );
        q.setParameter("qid", questaoId);
        List<Double> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    public Double findPesoByOpcaoId(Long opcaoId) {
        TypedQuery<Double> q = em.createQuery(
                "SELECT p.peso FROM Peso p WHERE p.idOpcao = :oid ORDER BY p.id",
                Double.class);
        q.setParameter("oid", opcaoId);
        List<Double> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
    
    public List<Peso> findByQuestao(Long questaoId) {
        return em.createQuery(
                "SELECT p FROM Peso p WHERE p.idQuestao = :qid ORDER BY p.id",
                Peso.class)
                .setParameter("qid", questaoId)
                .getResultList();
    }
        
    public java.util.Map<Long, Double> findMapPesosPorOpcoes(List<Long> opcoesIds) {
        java.util.Map<Long, Double> map = new java.util.HashMap<>();
        if (opcoesIds == null || opcoesIds.isEmpty()) return map;

        TypedQuery<Peso> q = em.createQuery(
                "SELECT p FROM Peso p WHERE p.idOpcao IN :ids", Peso.class);
        q.setParameter("ids", opcoesIds);
        List<Peso> pesos = q.getResultList();

        for (Peso p : pesos) {
            if (p.getIdOpcao() != null) {
                map.put(p.getIdOpcao(), p.getPeso());
            }
        }
        return map;
    }

}
