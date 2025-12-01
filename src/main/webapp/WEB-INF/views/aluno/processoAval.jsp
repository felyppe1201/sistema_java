<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8" />
    <title>Formulários do Processo</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/processoAval.css"
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
    <div class="container-geral">
      <a
        class="voltarbtn"
        href="${pageContext.request.contextPath}/turma?id=${turmaId}"
        ><span class="icon">←</span>Voltar</a
      >
      <h1 class="titulo">${nomeProcesso}</h1>

      <c:if test="${not empty naoRespondidos}">
        <h2 class="subtitulo">Formulários Pendentes</h2>

        <div class="grid-formularios">
          <c:forEach var="form" items="${naoRespondidos}">
            <a
              class="form-card nao-respondido"
              href="${pageContext.request.contextPath}/aluno/responderFormulario?id=${form.idFormulario}"
            >
              <div class="form-titulo">${form.titulo}</div>

              <div class="identificacao">
                <c:choose>
                  <c:when test="${form.identificado}">
                    <span class="icone-identificado">👤</span>
                    <span>Identificado</span>
                  </c:when>
                  <c:otherwise>
                    <span class="icone-identificado">🕶️</span>
                    <span>Anônimo</span>
                  </c:otherwise>
                </c:choose>
              </div>
            </a>
          </c:forEach>
        </div>
      </c:if>

      <c:if test="${not empty respondidos}">
        <h2 class="subtitulo">Formulários Respondidos</h2>

        <div class="grid-formularios">
          <c:forEach var="form" items="${respondidos}">
            <div class="form-card respondido">
              <div class="form-titulo">${form.titulo}</div>

              <div class="identificacao">
                <c:choose>
                  <c:when test="${form.identificado}">
                    <span class="icone-identificado">👤</span>
                    <span>Identificado</span>
                  </c:when>
                  <c:otherwise>
                    <span class="icone-identificado">🕶️</span>
                    <span>Anônimo</span>
                  </c:otherwise>
                </c:choose>
              </div>

              <div class="circle-container">
                <svg class="progress-circle" viewBox="0 0 36 36">
                  <path
                    class="bg"
                    d="M18 2.0845
                      a 15.9155 15.9155 0 0 1 0 31.831
                      a 15.9155 15.9155 0 0 1 0 -31.831"
                  />

                  <path
                    class="progress"
                    data-dash="${form.percentualAcerto}"
                    d="M18 2.0845
                      a 15.9155 15.9155 0 0 1 0 31.831
                      a 15.9155 15.9155 0 0 1 0 -31.831"
                  />
                </svg>

                <div class="percentual-text">${form.percentualAcerto}%</div>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:if>
    </div>

    <script>
      document.querySelectorAll(".progress").forEach((el) => {
        const dash = parseFloat(el.dataset.dash) || 0;

        const pathLength = el.getTotalLength();

        el.style.strokeDasharray = pathLength;
        el.style.strokeDashoffset = pathLength * (1 - dash / 100);
      });
    </script>
  </body>
</html>
