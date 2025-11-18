package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Usuario;
import jakarta.persistence.*;
import java.util.List;

public class UsuarioRepository {
    private EntityManager em;

    public UsuarioRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Usuario obj) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }

    public Usuario findById(long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public void update(Usuario obj) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }

    public void delete(long id) {
        em.getTransaction().begin();
        Usuario obj = em.find(Usuario.class, id);
        if (obj != null) em.remove(obj);
        em.getTransaction().commit();
    }

    public boolean authenticate(String email, String senha) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha", Usuario.class);
        query.setParameter("email", email);
        query.setParameter("senha", senha);
        return !query.getResultList().isEmpty();
    }
}
