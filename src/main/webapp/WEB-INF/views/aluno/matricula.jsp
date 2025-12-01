<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Nova Matrícula</title>
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
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/matricula.css"
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
      <h1 class="titulo">Nova Matrícula</h1>

      <c:if test="${not empty turmasDisponiveis}">
        <div class="turmas-list">
          <c:forEach var="t" items="${turmasDisponiveis}">
            <form
              method="post"
              action="${pageContext.request.contextPath}/aluno/matricula"
              class="turma-card-matricula"
            >
              <input type="hidden" name="turmaId" value="${t.idTurma}" />
              <div class="turma-info">
                <h3>${t.nomeDisciplina}</h3>
                <p><strong>Código:</strong> ${t.codigoTurma}</p>
                <p><strong>Vagas restantes:</strong> ${t.vagasRestantes}</p>
              </div>
              <button type="submit" class="btn-matricula">+ Matricular</button>
            </form>
          </c:forEach>
        </div>
      </c:if>

      <c:if test="${empty turmasDisponiveis}">
        <p class="sem-turmas">
          Não há turmas disponíveis para matrícula neste período.
        </p>
      </c:if>
    </main>

    <script>
      function toggleMenu() {
        document.getElementById("nav-links").classList.toggle("active");
      }
    </script>
  </body>
</html>
