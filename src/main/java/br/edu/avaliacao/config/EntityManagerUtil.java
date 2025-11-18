package br.edu.avaliacao.config;

import jakarta.persistence.EntityManager;

public class EntityManagerUtil {

    public static EntityManager getEntityManager() {
        return EntityManagerFactoryProvider
                .getEntityManagerFactory()
                .createEntityManager();
    }
}
