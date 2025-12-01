<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Painel do Aluno</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/alunoDashboard.css"
    />
  </head>

  <body>
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico</div>

      <button class="nav-toggle" onclick="toggleMenu()">☰</button>

      <ul id="nav-links" class="nav-links">
        <li>
          <a href="${pageContext.request.contextPath}/dashboard">Painel</a>
        </li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li>
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <main class="dashboard-content no-select">
      <h1 class="titulo">Painel do Aluno</h1>

      <div class="info-card">
        <h2>Bem-vindo, ${usuario.nome}!</h2>
        <p><strong>Curso:</strong> ${curso}</p>
        <p>
          <strong>Período Atual:</strong> ${periodo != null ? periodo : 'Não
          informado'}
        </p>
      </div>

      <section class="turmas-section">
        <h2>Minhas Turmas</h2>

        <div class="turmas-list">
          <c:choose>
            <c:when test="${not empty turmasMatriculadas}">
              <c:forEach var="turma" items="${turmasMatriculadas}">
                <a
                  class="turma-card"
                  href="${pageContext.request.contextPath}/turma?id=${turma.id}"
                >
                  <h3>${turma.disciplina}</h3>

                  <p><strong>Código:</strong> ${turma.codigo}</p>
                  <p><strong>Período:</strong> ${turma.periodo}</p>
                  <p><strong>Curso:</strong> ${turma.curso}</p>
                </a>
              </c:forEach>
            </c:when>

            <c:otherwise>
              <p class="sem-turmas">
                Você não está matriculado em nenhuma turma.
              </p>
            </c:otherwise>
          </c:choose>
          <a
            class="btn-nova-matricula"
            href="${pageContext.request.contextPath}/aluno/matricula"
          >
            <div class="btn-plus">＋</div>
            <div class="btn-text">Nova matrícula</div>
          </a>
        </div>
      </section>
    </main>

    <script>
      function toggleMenu() {
        document.getElementById("nav-links").classList.toggle("active");
      }
    </script>
  </body>
</html>
