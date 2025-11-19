package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import br.edu.avaliacao.repositorys.*;
import br.edu.avaliacao.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/aluno/avaliacoes")
public class MinhasAvaliacoesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario"); // Ajuste o cast conforme seu objeto de sessão
        
        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            MatriculaRepository matRepo = new MatriculaRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            FormularioRepository formRepo = new FormularioRepository(em);
            SubmissaoRepository subRepo = new SubmissaoRepository(em);

            // 1. Busca turmas onde o aluno está matriculado
            // Nota: Idealmente criar um método 'findByAluno' no Repo, mas faremos filtro Java por agilidade
            List<Matricula> matriculas = matRepo.findAll().stream()
                .filter(m -> m.getIdAluno() == usuario.getId())
                .toList();

            // Lista DTO simples para a View
            List<AvaliacaoPendenteDTO> pendentes = new ArrayList<>();

            for (Matricula m : matriculas) {
                Turma t = turmaRepo.findById(m.getIdTurma());
                
                // 2. Busca processos avaliativos ativos para o período da turma
                List<ProcessoAvaliativo> processos = procRepo.findAll().stream()
                    .filter(p -> p.getPeriodo() == t.getPeriodo() && p.getStat() == 1)
                    .toList();

                for (ProcessoAvaliativo p : processos) {
                    // 3. Busca formulários desse processo destinados a alunos
                    List<Formulario> formularios = formRepo.findAll().stream()
                        .filter(f -> f.getIdProcesso() == p.getId())
                        .toList();

                    for (Formulario f : formularios) {
                        // RF13 - Verifica se já respondeu
                        boolean jaRespondeu = subRepo.findAll().stream()
                            .anyMatch(s -> s.getIdFormulario() == f.getId() 
                                        && s.getIdTurma() == t.getId() 
                                        && s.getIdUsuario() == usuario.getId()); // Atenção ao anonimato depois

                        // Adiciona à lista
                        pendentes.add(new AvaliacaoPendenteDTO(
                            f.getId(), f.getTitulo(), 
                            t.getId(), t.getCodigoTurma(), 
                            jaRespondeu
                        ));
                    }
                }
            }

            req.setAttribute("avaliacoes", pendentes);
            req.getRequestDispatcher("/WEB-INF/views/aluno/lista-avaliacoes.jsp").forward(req, resp);

        } finally {
            if (em.isOpen()) em.close();
        }
    }
    
    // DTO interno simples
    public static class AvaliacaoPendenteDTO {
        public long idForm;
        public String tituloForm;
        public long idTurma;
        public String codigoTurma;
        public boolean respondida;
        
        public AvaliacaoPendenteDTO(long idF, String tF, long idT, String cT, boolean resp) {
            this.idForm = idF; this.tituloForm = tF; this.idTurma = idT; this.codigoTurma = cT; this.respondida = resp;
        }
        // Getters necessários para o JSP...
        public long getIdForm() { return idForm; }
        public String getTituloForm() { return tituloForm; }
        public long getIdTurma() { return idTurma; }
        public String getCodigoTurma() { return codigoTurma; }
        public boolean isRespondida() { return respondida; }
    }
}