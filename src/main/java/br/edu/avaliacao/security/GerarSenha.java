package br.edu.avaliacao.security;

import org.mindrot.jbcrypt.BCrypt;

public class GerarSenha {
    public static void main(String[] args) {
        String senha = "123"; 
        String hash = BCrypt.hashpw(senha, BCrypt.gensalt());
        System.out.println(hash);
    }
}