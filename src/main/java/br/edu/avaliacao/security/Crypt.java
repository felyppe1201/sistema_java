package br.edu.avaliacao.security;

import org.mindrot.jbcrypt.BCrypt;

public class Crypt {

    private static final int LOG_ROUNDS = 10; 
    
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
