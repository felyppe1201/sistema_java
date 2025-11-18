package models;

public class Disciplina {
    private int id;
    private int idCurso;
    private String nome;

    public Disciplina() {}

    public Disciplina(int id, int idCurso, String nome) {
        this.id = id;
        this.idCurso = idCurso;
        this.nome = nome;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
