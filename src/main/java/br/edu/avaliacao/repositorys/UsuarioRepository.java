package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Usuario;
import jakarta.persistence.*;
import java.util.List;

public class UsuarioRepository {
    private EntityManager em;

    public UsuarioRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Usuario obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public Usuario findById(long id) {
        return em.find(Usuario.class, id);
    }

    public Usuario findByEmail(String email) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email AND u.ativo = true", Usuario.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public List<Usuario> findProfessoresAtivos() {
    return em.createQuery(
        "SELECT u FROM Usuario u WHERE LOWER(u.cargo) = 'prof' AND u.ativo = true",
        Usuario.class
    ).getResultList();
}

    public void update(Usuario obj) {
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
            Usuario obj = em.find(Usuario.class, id);
            if (obj != null) em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public boolean authenticate(String email, String senha) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha", Usuario.class);
            query.setParameter("email", email);
            query.setParameter("senha", senha); // Lembre-se: em produção idealmente usaríamos hash, mas para o trabalho ok
            return !query.getResultList().isEmpty();
        } catch (NoResultException e) {
            return false;
        }
    }
}