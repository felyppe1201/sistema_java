<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Dashboard do Professor</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/generico.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/professorDashboard.css"
    />
  </head>
  <body class="bg-gray m-0">
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico - PROFESSOR</div>
      <button class="nav-toggle" onclick="toggleMenu()">☰</button>
      <ul id="nav-links" class="nav-links">
        <li>
          <a href="${pageContext.request.contextPath}/relatorio">Relatório</a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/dashboard">Painel</a>
        </li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li>
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <div class="dashboard-container">
      <h1>Dashboard do Professor</h1>
      <p class="text-secondary">Bem-vindo, ${professor.nome}!</p>

      <div class="m-t-5">
        <div class="tabs-header">
          <button
            class="tab-button active"
            onclick="openTab('visao-geral', this)"
          >
            Visão Geral
          </button>
          <button class="tab-button" onclick="openTab('turmas', this)">
            Minhas Turmas
          </button>
        </div>

        <div id="visao-geral" class="tab-content active">
          <h2>Informações de Perfil</h2>
          <div
            class="info-card bg-white p-6 flex flex-col gap-3 rounded-lg m-b-4 text-left"
          >
            <div class="flex-col">
              <span class="info-label">NOME COMPLETO</span>
              <span class="info-value text-xl">${professor.nome}</span>
            </div>
            <div class="flex-col">
              <span class="info-label">EMAIL CADASTRADO</span>
              <span class="info-value text-lg">${professor.email}</span>
            </div>
          </div>

          <h2>Indicadores Rápidos</h2>
          <div class="card-grid">
            <div class="info-card">
              <div class="card-value">${turmas.size()}</div>
              <div class="card-label">Total de Turmas Ativas</div>
            </div>

            <div class="info-card">
              <div class="card-value">0</div>
              <div class="card-label">Avaliações Pendentes</div>
            </div>

            <div class="info-card">
              <div class="card-value">0</div>
              <div class="card-label">Alunos em Risco</div>
            </div>
          </div>
        </div>

        <div id="turmas" class="tab-content">
          <h2>Turmas em Andamento</h2>
          <div class="bg-white rounded-lg p-4 shadow">
            <c:choose>
              <c:when test="${not empty turmas}">
                <div class="turmas-grid">
                  <c:forEach items="${turmas}" var="turma">
                    <a
                      class="turma-button"
                      href="${pageContext.request.contextPath}/lecio/turma?id=${turma.id}"
                    >
                      <span class="disciplina">${turma.disciplina}</span>
                      <span class="turma"> ${turma.codigo}</span>
                      <span class="Periodo">Período ${turma.periodo}°</span>
                    </a>
                  </c:forEach>
                </div>
              </c:when>

              <c:otherwise>
                <p class="text-secondary p-4">
                  Você não está lecionando nenhuma turma neste momento.
                </p>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </div>
    <script>
      function toggleMenu() {
        const navLinks = document.getElementById("nav-links");
        if (
          navLinks.classList.contains("active") ||
          navLinks.classList.contains("nav-open")
        ) {
          navLinks.classList.remove("active");
          navLinks.classList.remove("nav-open");
        } else {
          navLinks.classList.add("active");
        }
      }
      function openTab(tabId, element) {
        document.querySelectorAll(".tab-content").forEach((content) => {
          content.classList.remove("active");
        });

        document.querySelectorAll(".tab-button").forEach((btn) => {
          btn.classList.remove("active");
        });

        document.getElementById(tabId).classList.add("active");

        element.classList.add("active");
      }
    </script>
  </body>
</html>
