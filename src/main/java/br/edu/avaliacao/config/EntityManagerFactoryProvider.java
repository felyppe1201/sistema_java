package br.edu.avaliacao.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerFactoryProvider {

    private static final EntityManagerFactory emf = buildFactory();

    private static EntityManagerFactory buildFactory() {

        Map<String, String> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.url", Env.get("DB_URL"));
        overrides.put("jakarta.persistence.jdbc.user", Env.get("DB_USER"));
        overrides.put("jakarta.persistence.jdbc.password", Env.get("DB_PASS"));

        return Persistence.createEntityManagerFactory("default", overrides);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
