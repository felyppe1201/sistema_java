<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Painel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/professorDashboard.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
  </head>

  <body>
    <!-- NAVBAR -->
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

    <!-- CONTEÚDO DO DASHBOARD -->
    <main class="dashboard-content no-select">
      <h1>Bem-vindo, Administrador</h1>

      <div class="cards-container">
        <div class="card">Gerenciar Usuários</div>
        <div class="card">Gerenciar Cursos</div>
        <div class="card">Relatórios</div>
        <div class="card">Configurações</div>
      </div>
    </main>

    <script>
      function toggleMenu() {
        const nav = document.getElementById("nav-links");
        nav.classList.toggle("nav-open");
      }
    </script>
  </body>
</html>
