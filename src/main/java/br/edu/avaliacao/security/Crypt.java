package br.edu.avaliacao.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitária para centralizar todas as operações de segurança,
 * especialmente hashing de senhas usando a biblioteca BCrypt.
 */
public class Crypt {

    private static final int LOG_ROUNDS = 10; // Custo do hashing. 10 é um bom padrão.

    /**
     * Gera o hash (incluindo o salt) de uma senha fornecida em texto simples.
     * Esta é a função a ser usada ao criar ou atualizar a senha de um usuário.
     * * @param password A senha em texto simples.
     * @return O hash seguro da senha.
     */
    
    public static String hashPassword(String password) {
        // Gera um salt (semente) aleatório e aplica o hash com o número de rounds definido.
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verifica se uma senha fornecida em texto simples corresponde ao hash armazenado.
     * Esta é a função a ser usada no login e nas operações de alteração de senha/email.
     * * @param plainPassword A senha fornecida pelo usuário (texto simples).
     * @param hashedPassword O hash armazenado no banco de dados.
     * @return true se as senhas coincidirem, false caso contrário.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // BCrypt é seguro contra ataques de temporização (timing attacks)
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}