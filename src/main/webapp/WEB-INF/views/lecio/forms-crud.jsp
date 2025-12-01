<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>${processo.nome}</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/forms-crud.css"
    />
  </head>
  <body>
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico - PROFESSOR</div>
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

    <div class="page-container">
      <h1>${processo.nome}</h1>
      <p class="muted">
        Turma: ${processo.turma != null ? processo.turma.codigoTurma : 'N/D'}
      </p>

      <c:if test="${not empty msgSuccess}">
        <div class="msg-success">${msgSuccess}</div>
      </c:if>
      <c:if test="${not empty msgError}">
        <div class="msg-error">${msgError}</div>
      </c:if>

      <h2>Formulários</h2>

      <div class="forms-grid">
        <c:choose>
          <c:when test="${not empty formularios}">
            <c:forEach var="f" items="${formularios}">
              <div class="form-card">
                <div class="form-card-body">
                  <div class="form-title">${f.titulo}</div>
                  <div class="form-meta">
                    <span class="identificado"
                      >${f.identificado ? '👤' : '⬛'}</span
                    >
                  </div>
                </div>

                <div class="form-actions">
                  <a
                    class="btn-view"
                    href="${pageContext.request.contextPath}/lecio/criar-forms?id_process=${processo.id}&id_form=${f.id}"
                    >Abrir</a
                  >

                  <form
                    method="post"
                    action="${pageContext.request.contextPath}/lecio/process"
                    onsubmit="return confirm('Confirma exclusão (soft delete) deste formulário?');"
                  >
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="idFormulario" value="${f.id}" />
                    <input type="hidden" name="id" value="${processo.id}" />
                    <button type="submit" class="btn-delete-small">
                      Excluir
                    </button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </c:when>
        </c:choose>

        <div class="form-card create-card">
          <a
            class="create-link"
            href="${pageContext.request.contextPath}/lecio/criar-forms?id_process=${processo.id}"
          >
            <div class="plus">＋</div>
            <div class="create-text">Criar novo formulário</div>
          </a>
        </div>
      </div>
    </div>
  </body>
</html>
