<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Painel do Aluno</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
        rel="stylesheet"
        type="text/css"
        href="${pageContext.request.contextPath}/assets/css/alunoDashboard.css"
    />
    <link
        rel="stylesheet"
        type="text/css"
        href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <style>
        /* Estilos básicos para o modal (você deve movê-los para alunoDashboard.css) */
        .dashboard-content { max-width: 900px; margin: 20px auto; padding: 20px; }
        .info-card { background-color: #e0f7fa; padding: 15px; border-radius: 6px; margin-bottom: 20px; border-left: 5px solid #00bcd4; }
        .info-card h2 { margin-top: 0; color: #00796b; }
        .turmas-list { margin-top: 10px; padding-top: 10px; }
        .turma-item { background: #f9f9f9; padding: 12px; border-radius: 4px; margin-bottom: 8px; border-left: 3px solid #64b5f6; }
        .btn-add { background-color: #4caf50; color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; margin-top: 15px; }

        /* Estilos para o Modal */
        .modal {
            display: none; 
            position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%;
            overflow: auto; background-color: rgba(0,0,0,0.6);
        }
        .modal-content {
            background-color: #fefefe; margin: 10% auto; padding: 20px;
            border: 1px solid #888; width: 80%; max-width: 600px; border-radius: 8px;
        }
        .close-btn { color: #aaa; float: right; font-size: 28px; font-weight: bold; }
        .close-btn:hover, .close-btn:focus { color: black; text-decoration: none; cursor: pointer; }
        .turmas-disponiveis .turma-disp { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px dashed #eee; }
        .turmas-disponiveis button { background-color: #2196f3; color: white; border: none; padding: 6px 10px; border-radius: 4px; cursor: pointer; }
    </style>
</head>

<body>
    <nav class="navbar no-select">
        <div class="nav-logo">Sistema Acadêmico</div>

        <button class="nav-toggle" onclick="toggleMenu()">☰</button>

        <ul id="nav-links" class="nav-links">
            <li><a href="${pageContext.request.contextPath}/dashboard">Painel</a></li>
            <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
            <li><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
        </ul>
    </nav>

    <main class="dashboard-content no-select">
        <h1>Painel do Aluno</h1>

        <div class="info-card">
            <h2>Bem-vindo, ${usuario.nome}!</h2>
            <p><strong>Curso:</strong> ${curso}</p>
            <p><strong>Período Atual:</strong> ${periodo}</p>
            <p><strong>Matrícula:</strong> ${usuario.matricula}</p>
        </div>

        <section>
            <h2>Minhas Turmas Atuais</h2>
            <div class="turmas-list">
                <c:choose>
                    <c:when test="${not empty turmasAtuais}">
                        <c:forEach var="turma" items="${turmasAtuais}">
                            <div class="turma-item">
                                <strong>${turma.disciplina}</strong>
                                <br/> Código: ${turma.codigo} | Período: ${turma.periodo}
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Você não está matriculado em nenhuma turma neste momento.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <button class="btn-add" onclick="openModal()">Adicionar Turmas</button>
        </section>
    </main>

    <div id="addTurmasModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeModal()">&times;</span>
            <h2>Turmas Disponíveis</h2>
            <p>Selecione as turmas para realizar a matrícula.</p>

            <div class="turmas-disponiveis">
                <c:choose>
                    <c:when test="${not empty turmasDisponiveis}">
                        <c:forEach var="disp" items="${turmasDisponiveis}">
                            <div class="turma-disp">
                                <div>
                                    <strong>${disp.disciplina}</strong> (${disp.codigo})
                                    <br/> Vagas: ${disp.vagas} | ID: ${disp.id}
                                </div>
                                <button onclick="matricular('${disp.id}')">Matricular</button>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Não há turmas disponíveis para matrícula no catálogo.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script>
        const modal = document.getElementById("addTurmasModal");

        function toggleMenu() {
            const nav = document.getElementById("nav-links");
            nav.classList.toggle("nav-open");
        }

        function openModal() {
            modal.style.display = "block";
        }

        function closeModal() {
            modal.style.display = "none";
        }

        // Fecha o modal ao clicar fora dele
        window.onclick = function(event) {
            if (event.target == modal) {
                closeModal();
            }
        }

        // Função de Matrícula (Isto deve chamar um Servlet via AJAX/Fetch ou redirecionamento)
        function matricular(turmaId) {
            alert("Ação: Matricular na Turma ID " + turmaId + ". Implementar Servlet de Matrícula aqui.");
            
            // Exemplo de Redirecionamento (requer Servlet de Matrícula):
            // window.location.href = "${pageContext.request.contextPath}/matricula?turmaId=" + turmaId;
            
            // Para testes, apenas feche o modal:
            closeModal();
        }
    </script>
</body>
</html>