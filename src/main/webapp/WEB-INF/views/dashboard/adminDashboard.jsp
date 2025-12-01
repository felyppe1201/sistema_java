<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Painel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/adminDashboard.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
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
          <a href="${pageContext.request.contextPath}/central_admin"
            >Central de administração</a
          >
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/relatorios">Relatórios</a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <main class="dashboard-content no-select">
      <h1 class="main-title">Professores</h1>

      <div class="prof-list">
        <c:forEach var="prof" items="${professores}">
          <div class="prof-card">
            <div class="prof-header" onclick="toggleTurmas('${prof.id}')">
              <span class="prof-name">${prof.nome}</span>
              <span class="prof-count">${prof.quantidadeDeTurmas} turmas</span>
            </div>

            <div id="turmas-${prof.id}" class="turmas hidden">
              <c:forEach var="t" items="${prof.turmas}">
                <div class="turma-item">
                  <strong>Disciplina ${t.disciplinaId}</strong><br /> Período:
                  ${t.periodo}<br /> Vagas: ${t.vagas}<br /> Código:
                  ${t.codigo}<br />
                </div>
              </c:forEach>
            </div>
          </div>
        </c:forEach>
      </div>
    </main>

    <script>
      function toggleTurmas(id) {
        const el = document.getElementById("turmas-" + id);
        el.classList.toggle("hidden");
      }
    </script>

    <script>
      function toggleMenu() {
        const nav = document.getElementById("nav-links");
        nav.classList.toggle("nav-open");
      }
    </script>
  </body>
</html>
