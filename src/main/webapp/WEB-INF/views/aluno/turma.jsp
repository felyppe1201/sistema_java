<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <title>${turma.codigoTurma}</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/turma2.css"
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
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <div class="container">
      <!-- CABEÇALHO DA TURMA -->
      <div class="cabecalho-turma">
        <h1 class="codigo-turma">${turma.codigoTurma}</h1>
        <div class="info-turma">
          <p><strong>Disciplina:</strong> ${disciplina.nome}</p>
          <p><strong>Período:</strong> ${turma.periodo}</p>
          <p>
            <strong>Professor:</strong> ${professor.nome} -
            <span class="email-professor">${professor.email}</span>
          </p>
        </div>
      </div>

      <!-- LISTA DE PROCESSOS AVALIATIVOS -->
      <div class="processos-lista">
        <c:forEach var="processo" items="${processosAluno}">
          <a
            href="${pageContext.request.contextPath}/aluno/processoAval?id=${processo.idProcesso}"
            class="processo-btn"
          >
            <div class="processo-info">
              <p class="nome-processo">${processo.nomeProcesso}</p>
              <p class="forms-info">
                ${processo.respondidos}/${processo.totalFormularios}
                <c:if
                  test="${processo.respondidos == processo.totalFormularios && processo.totalFormularios > 0}"
                >
                  <span class="check">&#10003;</span>
                </c:if>
              </p>
            </div>
          </a>
        </c:forEach>
      </div>
    </div>
  </body>
</html>
