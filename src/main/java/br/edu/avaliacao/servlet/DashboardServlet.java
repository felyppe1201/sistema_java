package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerFactoryProvider;
import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import br.edu.avaliacao.repositorys.AlunoMatriculadoRepository;
import br.edu.avaliacao.repositorys.AtribuicaoProfessorRepository;
import br.edu.avaliacao.repositorys.MatriculaRepository;
import br.edu.avaliacao.repositorys.TurmaRepository; // Import necessário
import br.edu.avaliacao.repositorys.UsuarioRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = EntityManagerFactoryProvider.getEntityManagerFactory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Nenhuma sessão → redireciona para login
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Recupera o usuário logado
        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        String cargo = usuario.getCargo();

        // ----------- Controle de exibição por cargo -------------
        String destino;

        switch (cargo.toUpperCase()) {
            case "ADM":
                destino = "/WEB-INF/views/dashboard/adminDashboard.jsp";
                System.out.println("=== DB URL (JPA props) ===");
                    try {
                        EntityManager emTemp = EntityManagerUtil.getEntityManager();
                        Object url = emTemp.getEntityManagerFactory().getProperties()
                                            .getOrDefault("jakarta.persistence.jdbc.url",
                                                    emTemp.getEntityManagerFactory().getProperties().get("javax.persistence.jdbc.url"));
                        System.out.println("JPA URL = " + url);
                        emTemp.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                carregarDadosAdmin(req);  
                break;

            case "ALU":
                destino = "/WEB-INF/views/dashboard/alunoDashboard.jsp";
                carregarDadosAluno(req, usuario.getId());
                break;

            case "PROF":
                // Redireciona para o JSP específico do professor
                destino = "/WEB-INF/views/dashboard/professorDashboard.jsp";
                // Carrega os dados de turmas e perfil do professor
                carregarDadosProfessor(req, usuario.getId());
                break;

            case "COORD":
                destino = "/WEB-INF/views/dashboard/coordDashboard.jsp";
                break;

            default:
                // Cargo inválido → volta para login
                resp.sendRedirect(req.getContextPath() + "/login.jsp?erro=perfil");
                return;
        }

        // Encaminha para a view correta
        req.setAttribute("usuario", usuario);
        req.getRequestDispatcher(destino).forward(req, resp);
    }
    
    /**
     * Carrega informações básicas, turmas atuais e turmas disponíveis
     * para uso no alunoDashboard.jsp.
     */
    private void carregarDadosAluno(HttpServletRequest req, Long alunoId) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            AlunoMatriculadoRepository alunoMatRepo = new AlunoMatriculadoRepository(em);
            MatriculaRepository matriculaRepo = new MatriculaRepository(em);

            // 1) Buscar matrícula acadêmica do aluno (curso + período)
            AlunoMatriculado am = alunoMatRepo.findByUsuarioId(alunoId);

            if (am == null) {
                req.setAttribute("erroAluno", "Aluno não possui vínculo acadêmico ativo.");
                return;
            }

            String cursoNome = am.getCurso().getNome();
            Integer periodoAtual = am.getPeriodo();

            // 2) Buscar turmas em que o aluno está matriculado
            List<Object[]> resultados = matriculaRepo.findTurmasDetalhadasByAlunoId(alunoId);

            List<Map<String, Object>> turmasDTO = new ArrayList<>();

            for (Object[] linha : resultados) {
                Turma turma = (Turma) linha[0];
                Disciplina disc = (Disciplina) linha[1];
                Curso curso = (Curso) linha[2];

                Map<String, Object> map = new HashMap<>();
                map.put("id", turma.getId());
                map.put("codigo", turma.getCodigoTurma());
                map.put("periodo", turma.getPeriodo());
                map.put("disciplina", disc.getNome());
                map.put("curso", curso.getNome());

                turmasDTO.add(map);
            }

            // 3) Atribuir ao request
            req.setAttribute("curso", cursoNome);
            req.setAttribute("periodo", periodoAtual);
            req.setAttribute("turmasMatriculadas", turmasDTO);

        } finally {
            em.close();
        }
    }

    /**
     * Carrega os dados de perfil e a lista de turmas que o professor está lecionando.
     */
    private void carregarDadosProfessor(HttpServletRequest req, Long professorId) {
        
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);

            // 1. BUSCAR DADOS COMPLETOS DO PROFESSOR (Nome, Email, etc.)
            Usuario professor = usuarioRepo.findById(professorId);

            // 2. BUSCAR TURMAS ATIVAS
            // Requer que TurmaRepository tenha o método findTurmasByProfessorId(Long)
            List<Turma> turmas = turmaRepo.findTurmasByProfessorId(professorId);
            
            // 3. SETAR ATRIBUTOS PARA O JSP
            req.setAttribute("professor", professor); // Objeto completo do professor
            req.setAttribute("turmas", turmas);

            // Mock de Indicadores de Alerta (A ser implementado com lógica real de Avaliação)
            req.setAttribute("avaliacoesPendentes", turmas.size() * 2); // Exemplo de valor mock
            req.setAttribute("alunosEmRisco", (int) (turmas.size() * 1.5)); // Exemplo de valor mock
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do Dashboard do Professor: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("erroDashboard", "Não foi possível carregar as turmas do professor.");
        } finally {
            em.close();
        }
    }

    /**
     * Carrega lista de professores e suas turmas
     * para uso no adminDashboard.jsp
     */
    private void carregarDadosAdmin(HttpServletRequest req) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            System.out.println("DEBUG: abrindo EM para carregarDadosAdmin");
            AtribuicaoProfessorRepository atribuicaoRepo =
                    new AtribuicaoProfessorRepository(em);
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            // 1) Buscar todos os professores (via Repository)
            List<Usuario> professores = usuarioRepo.findProfessoresAtivos();
            System.out.println("DEBUG: professores.size() = " + (professores == null ? "null" : professores.size()));

            // 2) Buscar todas as atribuições
            List<AtribuicaoProfessor> atribuicoes = atribuicaoRepo.findAll();
            System.out.println("DEBUG: atribuicoes.size() = " + (atribuicoes == null ? "null" : atribuicoes.size()));

            // 3) Estrutura final para enviar ao JSP
            List<Map<String, Object>> professoresDTO = new ArrayList<>();

            for (Usuario prof : professores) {

                List<Map<String, Object>> turmasDoProfessor = new ArrayList<>();

                // filtrar turmas deste professor
                for (AtribuicaoProfessor ap : atribuicoes) {
                    if (ap.getProfessor().getId() == prof.getId()) {

                        Turma t = ap.getTurma();

                        Map<String, Object> turmaMap = new HashMap<>();
                        turmaMap.put("id", t.getId());
                        turmaMap.put("disciplinaId", t.getDisciplina().getId());
                        turmaMap.put("periodo", t.getPeriodo());
                        turmaMap.put("codigo", t.getCodigoTurma());
                        turmaMap.put("vagas", t.getNumeroVagas());

                        turmasDoProfessor.add(turmaMap);
                    }
                }

                Map<String, Object> profMap = new HashMap<>();
                profMap.put("id", prof.getId());
                profMap.put("nome", prof.getNome());
                profMap.put("quantidadeDeTurmas", turmasDoProfessor.size());
                profMap.put("turmas", turmasDoProfessor);

                professoresDTO.add(profMap);
            }

            req.setAttribute("professores", professoresDTO);

        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}