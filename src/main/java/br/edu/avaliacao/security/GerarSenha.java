package br.edu.avaliacao.security;

import org.mindrot.jbcrypt.BCrypt;

// apenas para debug e hash manual
public class GerarSenha {
    public static void main(String[] args) {
        String senha = "123"; // sua senha de teste
        String hash = BCrypt.hashpw(senha, BCrypt.gensalt());
        System.out.println(hash);
    }
}