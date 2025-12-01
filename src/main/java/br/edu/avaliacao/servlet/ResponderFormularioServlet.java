package br.edu.avaliacao.servlet;

import br.edu.avaliacao.services.ResponderFormularioService;
import br.edu.avaliacao.services.ResponderFormularioService.FormularioDTO;
import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.Resposta;
import br.edu.avaliacao.models.Submissao;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.repositorys.FormularioRepository;
import br.edu.avaliacao.repositorys.RespostaRepository;
import br.edu.avaliacao.repositorys.SubmissaoRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/aluno/responderFormulario")
public class ResponderFormularioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do formulário não fornecido.");
            return;
        }

        Long formularioId;
        try {
            formularioId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do formulário inválido.");
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        Long usuarioId = usuario.getId();

        ResponderFormularioService service = new ResponderFormularioService();
        FormularioDTO formularioDTO = service.montarFormulario(formularioId);

        if (formularioDTO == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Formulário não encontrado.");
            return;
        }

        req.setAttribute("usuarioId", usuarioId);
        req.setAttribute("formularioId", formularioId);
        req.setAttribute("formulario", formularioDTO);

        req.getRequestDispatcher("/WEB-INF/views/aluno/responderFormulario.jsp")
                .forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            long formularioId = Long.parseLong(req.getParameter("formularioId"));
            long usuarioId = Long.parseLong(req.getParameter("usuarioId"));

            Turma turma = buscarTurmaDoFormulario(em, formularioId);
            if (turma == null) {
                em.getTransaction().rollback();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Turma não encontrada para o formulário.");
                return;
            }

            ResponderFormularioService service = new ResponderFormularioService();
            ResponderFormularioService.FormularioDTO formDTO = service.montarFormulario(formularioId);

            if (formDTO == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Formulário não encontrado.");
                return;
            }

            Submissao submissao = new Submissao();
            submissao.setIdFormulario(formularioId);
            submissao.setIdTurma(turma.getId());
            submissao.setIdUsuario(usuarioId);

            SubmissaoRepository subRepo = new SubmissaoRepository(em);
            subRepo.save(submissao);

            em.flush(); 

            RespostaRepository respRepo = new RespostaRepository(em);

            for (var questao : formDTO.getQuestoes()) {

                long questaoId = questao.getId();
                String tipo = questao.getTipo();

                if (tipo.equals("obj")) {

                    String[] selecionadas = req.getParameterValues("questao_obj_" + questaoId);

                    if (selecionadas != null) {
                        for (String opcaoIdStr : selecionadas) {

                            Resposta r = new Resposta();
                            r.setIdSubmissao(submissao.getId());
                            r.setIdQuestao(questaoId);
                            r.setIdOpcao(Long.parseLong(opcaoIdStr));

                            respRepo.save(r);
                        }
                    }
                }

                else if (tipo.equals("vf")) {

                    for (var opcao : questao.getOpcoes()) {

                        String nome = "questao_vf_" + questaoId + "_opcao_" + opcao.getId();
                        String valor = req.getParameter(nome);

                        if (valor == null)
                            continue;

                        Resposta r = new Resposta();
                        r.setIdSubmissao(submissao.getId());
                        r.setIdQuestao(questaoId);
                        r.setIdOpcao(opcao.getId());
                        r.setRespostaVf(valor.equals("V"));

                        respRepo.save(r);
                    }
                }

                else if (tipo.equals("disc")) {

                    String texto = req.getParameter("questao_disc_" + questaoId);
                    if (texto != null && !texto.isBlank()) {

                        Resposta r = new Resposta();
                        r.setIdSubmissao(submissao.getId());
                        r.setIdQuestao(questaoId);
                        r.setTexto(texto);

                        respRepo.save(r);
                    }
                }
            }


            em.getTransaction().commit();
            resp.sendRedirect(req.getContextPath() + "/dashboard?msg=ok");

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            resp.sendError(500, "Erro ao enviar formulário: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private Turma buscarTurmaDoFormulario(EntityManager em, long formularioId) {

        FormularioRepository formularioRepo = new FormularioRepository(em);
        Formulario form = formularioRepo.findById(formularioId);
        if (form == null) return null;

        var processo = em.find(br.edu.avaliacao.models.ProcessoAvaliativo.class, form.getIdProcesso());
        if (processo == null) return null;

        return em.find(Turma.class, processo.getIdTurma());
    }


}
